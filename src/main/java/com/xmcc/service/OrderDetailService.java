package com.xmcc.service;

import com.xmcc.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {

    //批量插入订单项
    void batchInsert(List<OrderDetail> list);
}
