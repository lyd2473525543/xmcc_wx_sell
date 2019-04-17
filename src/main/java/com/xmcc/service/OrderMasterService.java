package com.xmcc.service;

import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderMasterDto;
import org.springframework.stereotype.Service;

@Service
public interface OrderMasterService {
    ResultResponse insertOrder(OrderMasterDto orderMasterDto);
}
