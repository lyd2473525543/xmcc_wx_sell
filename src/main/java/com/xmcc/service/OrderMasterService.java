package com.xmcc.service;

import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.dto.PageDto;
import org.springframework.stereotype.Service;

@Service
public interface OrderMasterService {
    ResultResponse insertOrder(OrderMasterDto orderMasterDto);//插入订单

    ResultResponse findOrder(PageDto pageDto);//获取订单列表

    ResultResponse getDetailedOrder(String openid,String orderId);//获取订单详情

    ResultResponse delOrder(String openid,String orderId);//取消订单
}
