package com.xmcc.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmcc.common.*;
import com.xmcc.dto.OrderDetailDto;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.dto.OrderMasterInfoDto;
import com.xmcc.dto.PageDto;
import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import com.xmcc.entity.ProductInfo;
import com.xmcc.exception.CustomException;
import com.xmcc.repository.OrderDetailRepository;
import com.xmcc.repository.OrderMasterRepository;
import com.xmcc.service.OrderDetailService;
import com.xmcc.service.OrderMasterService;
import com.xmcc.service.ProductInfoService;
import com.xmcc.utils.BigDecimalUtil;
import com.xmcc.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMasterServiceImpl implements OrderMasterService {

    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional
    public ResultResponse insertOrder(OrderMasterDto orderMasterDto) {
        //取出订单项 @valid:用于配合JSR303注解验证参数，只能用在controller层进行验证
        //validetor：在service层验证
        List<OrderDetailDto> items = orderMasterDto.getItems();
        //创建订单集合存在detail
        ArrayList<OrderDetail> orderDetailList = Lists.newArrayList();
        //创建订单总金额为0 涉及到钱的都用搞精度计算
        BigDecimal totalPrice = new BigDecimal("0");

        for (OrderDetailDto orderDetailDto:items) {
            //查询订单
            ResultResponse<ProductInfo> productInfoById = productInfoService.findById(orderDetailDto.getProductId());
            //判断productInfoById的code
            if (productInfoById.getCode() == ResultEnums.FAIL.getCode()){
                throw new CustomException(productInfoById.getMsg());
            }
            //获取查询的商品
            ProductInfo productInfo = productInfoById.getData();
            //比较库存，判断库存是否足够
            if (productInfo.getProductStock() < orderDetailDto.getProductQuantity()){
                throw new CustomException(ProductEnums.PRODUCT_NOT_ENOUGH.getMsg());
            }
            //创建订单项
            OrderDetail orderDetail = OrderDetail.builder().detailId(IDUtils.createIdbyUUID()).productIcon(productInfo.getProductIcon())
                    .productId(orderDetailDto.getProductId()).productName(productInfo.getProductName())
                    .productPrice(productInfo.getProductPrice()).productQuantity(orderDetailDto.getProductQuantity())
                    .build();
            //添加到集合中
            orderDetailList.add(orderDetail);
            //减少商品库存
            productInfo.setProductStock(productInfo.getProductStock() - orderDetailDto.getProductQuantity());
            //更新商品信息
            productInfoService.updateProduct(productInfo);
            //计算价格
            totalPrice = BigDecimalUtil.add(totalPrice,BigDecimalUtil.multi(productInfo.getProductPrice(),orderDetailDto.getProductQuantity()));
        }
        //生成订单id
        String orderId = IDUtils.createIdbyUUID();
        //构建订单信息
        OrderMaster orderMaster = OrderMaster.builder().buyerAddress(orderMasterDto.getAddress()).buyerName(orderMasterDto.getName())
                .buyerOpenid(orderMasterDto.getOpenid()).orderStatus(OrderEnums.NEW.getCode())
                .payStatus(PayEnums.WAIT.getCode()).buyerPhone(orderMasterDto.getPhone())
                .orderId(orderId).orderAmount(totalPrice).build();
        //将生成的订单id设置到订单项中
        List<OrderDetail> detailList = orderDetailList.stream().map(orderDetail -> {
            orderDetail.setOrderId(orderId);
            return orderDetail;
        }).collect(Collectors.toList());
        //插入订单项
        orderDetailService.batchInsert(detailList);
        //插入订单
        orderMasterRepository.save(orderMaster);
        HashMap<String, String> hashMap = Maps.newHashMap();
        //按照前台要求的数据结构传入
        hashMap.put("orderId",orderId);
        return ResultResponse.success(hashMap);
    }

    @Override
    public ResultResponse findOrder(PageDto pageDto) {
        Pageable pageable = PageRequest.of(pageDto.getPage(),pageDto.getSize());
        List<OrderMaster> orderMasterList = orderMasterRepository.findAllByBuyerOpenid(pageDto.getOpenid(), pageable);
        return ResultResponse.success(orderMasterList);
    }

    @Override
    public ResultResponse getDetailedOrder(String openid, String orderId) {
        //获取订单信息
        OrderMaster orderMaster = orderMasterRepository.findByBuyerOpenidAndOrderId(openid, orderId);
        //判断订单是否存在
        if (orderMaster == null){
            return ResultResponse.fail(OrderEnums.ORDER_NOT_EXITS.getMsg());
        }
        //根据订单id查询订单项信息
        List<OrderDetail> orderDetail = orderDetailRepository.findByOrderId(orderId);
        //转为dto
        OrderMasterInfoDto masterInfoDto = OrderMasterInfoDto.build(orderMaster);
        masterInfoDto.setOrderDetailList(orderDetail);
        return ResultResponse.success(masterInfoDto);
    }

    @Override
    public ResultResponse delOrder(String openid, String orderId) {
        //获取订单信息
        OrderMaster orderMaster = orderMasterRepository.findByBuyerOpenidAndOrderId(openid, orderId);
        //判断订单是否存在
        if (orderMaster == null){
            return ResultResponse.fail(OrderEnums.ORDER_NOT_EXITS.getMsg());
        }
        //判断订单的状态，等于1，订单以完成。等于2，订单以取消
        if (orderMaster.getOrderStatus() == OrderEnums.FINSH.getCode()){
            return ResultResponse.fail(OrderEnums.FINSH_CANCEL.getMsg());
        }
        if (orderMaster.getOrderStatus() == OrderEnums.CANCEL.getCode()){
            return ResultResponse.fail(OrderEnums.FINSH_CANCEL.getMsg());
        }
        //改变订单状态
        orderMaster.setOrderStatus(OrderEnums.CANCEL.getCode());
        //修改数据库
        orderMasterRepository.save(orderMaster);
        return ResultResponse.success(OrderEnums.CANCEL.getMsg());
    }
}
