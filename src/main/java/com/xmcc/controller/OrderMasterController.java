package com.xmcc.controller;

import com.google.common.collect.Maps;
import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.dto.PageDto;
import com.xmcc.service.OrderMasterService;
import com.xmcc.utils.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/order")
@Api(value = "订单相关接口",description = "完成订单的增删改查")
public class OrderMasterController {

    @Autowired
    private OrderMasterService orderMasterService;

    @RequestMapping("/create")
    @ApiOperation(value = "创建订单接口",httpMethod = "POST",response = ResultResponse.class)
    public ResultResponse createOrder(
            @Valid @ApiParam(name = "订单对象",value = "JSON",required = true) OrderMasterDto orderMasterDto
            , BindingResult bindingResult){
        Map<String, String> map = Maps.newHashMap();
        //判断是否有参数校验问题
        if (bindingResult.hasErrors()){
            List<String> errList = bindingResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(errList));
            //将参数校验的错误信息返回给前台
            return ResultResponse.fail(map);
        }
        return orderMasterService.insertOrder(orderMasterDto);
    }

    @RequestMapping("/list")
    @ApiOperation(value = "获取订单列表",httpMethod = "GET",response = ResultResponse.class)
    public ResultResponse getOrderList(
            @Valid @ApiParam(name = "分页对象",value = "JSON",required = true) PageDto pageDto
            , BindingResult bindingResult){
        Map<String, String> map = Maps.newHashMap();
        //判断是否有参数校验问题
        if (bindingResult.hasErrors()){
            List<String> errList = bindingResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(errList));
            //将参数校验的错误信息返回给前台
            return ResultResponse.fail(map);
        }
        return orderMasterService.findOrder(pageDto);
    }

    @RequestMapping("/detail")
    @ApiOperation(value = "获取订单详情",httpMethod = "GET",response = ResultResponse.class)
    public ResultResponse getDetailedOrder(String openid,String orderId){
        return orderMasterService.getDetailedOrder(openid,orderId);
    }

    @RequestMapping("/cancel")
    @ApiOperation(value = "取消订单",httpMethod = "POST",response = ResultResponse.class)
    public ResultResponse delOrder(String openid,String orderId){
        return orderMasterService.delOrder(openid,orderId);
    }
}
