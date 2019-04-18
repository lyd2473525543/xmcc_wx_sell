package com.xmcc.dto;

import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMasterInfoDto extends OrderMaster {
    private List<OrderDetail> orderDetailList;

    //转换成Dto
    public static OrderMasterInfoDto build(OrderMaster orderMaster){
        OrderMasterInfoDto orderMasterInfoDto = new OrderMasterInfoDto();
        BeanUtils.copyProperties(orderMaster,orderMasterInfoDto);
        return orderMasterInfoDto;
    }
}
