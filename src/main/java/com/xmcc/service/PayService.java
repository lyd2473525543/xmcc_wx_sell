package com.xmcc.service;

import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;
import com.xmcc.entity.OrderMaster;

public interface PayService {
    PayResponse create(OrderMaster orderMaster);

    OrderMaster findOrderById(String orderId);

    void weixinNotify(String notifyData);

    RefundResponse refund(OrderMaster orderMaster);
}
