package com.xmcc.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.BestPayService;
import com.xmcc.common.Constant;
import com.xmcc.common.OrderEnums;
import com.xmcc.common.PayEnums;
import com.xmcc.entity.OrderMaster;
import com.xmcc.exception.CustomException;
import com.xmcc.repository.OrderMasterRepository;
import com.xmcc.service.PayService;
import com.xmcc.utils.BigDecimalUtil;
import com.xmcc.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private BestPayService bestPayService;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    public PayResponse create(OrderMaster orderMaster) {
        PayRequest payRequest = new PayRequest();
        //微信用户OPenid
        payRequest.setOpenid(orderMaster.getBuyerOpenid());
        //订单金额
        payRequest.setOrderAmount(orderMaster.getOrderAmount().doubleValue());
        //订单ID
        payRequest.setOrderId(orderMaster.getOrderId());
        //订单名字
        payRequest.setOrderName(Constant.ORDER_NAME);
        //支付类型
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("微信支付的请求:{}", JsonUtil.object2string(payRequest));
        PayResponse response = bestPayService.pay(payRequest);
        log.info("微信支付的返回结果为:{}", JsonUtil.object2string(response));
        return response;
    }

    @Override
    public OrderMaster findOrderById(String orderId) {
        Optional<OrderMaster> byId = orderMasterRepository.findById(orderId);
        if (!byId.isPresent()){
            throw new CustomException(OrderEnums.ORDER_NOT_EXITS.getMsg());
        }
        OrderMaster orderMaster = byId.get();
        return orderMaster;
    }

    @Override
    public void weixinNotify(String notifyData) {
        //调用API进行验证
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        //查询订单
        OrderMaster orderById = findOrderById(payResponse.getOrderId());
        //比较金额
        if (BigDecimalUtil.equals2(orderById.getOrderAmount()
                ,new BigDecimal(String.valueOf(payResponse.getOrderAmount())))){
            log.error("微信支付回调，订单金额不一致.微信:{},数据库:{}",payResponse.getOrderAmount(),orderById.getOrderAmount());
            throw new CustomException(OrderEnums.AMOUNT_CHECK_ERROR.getMsg());
        }
        //判断支付状态
        if (!(orderById.getPayStatus() == PayEnums.WAIT.getCode())){
            throw new CustomException(PayEnums.STATUS_ERROR.getMsg());
        }
        //比较结束以后 完成订单支付状态的修改
        orderById.setPayStatus(PayEnums.FINISH.getCode());
        orderMasterRepository.save(orderById);
    }

    @Override
    public RefundResponse refund(OrderMaster orderMaster) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderAmount(orderMaster.getOrderAmount().doubleValue());
        refundRequest.setOrderId(orderMaster.getOrderId());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("微信退款请求：{}",refundRequest);
        //执行退款
        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        log.info("微信退款响应：{}",refundResponse);
        return refundResponse;
    }
}
