package com.xmcc.service.impl;

import com.xmcc.common.ProductEnums;
import com.xmcc.common.ResultEnums;
import com.xmcc.common.ResultResponse;
import com.xmcc.dto.ProductCategoryDto;
import com.xmcc.dto.ProductInfoDto;
import com.xmcc.entity.ProductCategory;
import com.xmcc.entity.ProductInfo;
import com.xmcc.repository.ProductCategoryRepository;
import com.xmcc.repository.ProductInfoRepository;
import com.xmcc.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public ResultResponse findAll() {
        //查询所有分类
        List<ProductCategory> all = productCategoryRepository.findAll();
        //将all转换成dto
        List<ProductCategoryDto> collect
                = all.stream().map(productCategory -> ProductCategoryDto.build(productCategory)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)){
            return ResultResponse.fail();
        }
        //获取类目编号集合
        List<Integer> list
                = collect.stream().map(productCategoryDto -> productCategoryDto.getCategoryType()).collect(Collectors.toList());
        //根据类目集合查询商品列表
        List<ProductInfo> productInfoList = productInfoRepository.findAllByProductStatusAndCategoryTypeIn(ResultEnums.PRODUCT_UP.getCode(), list);
        //对collect集合进行遍历 取出每个商品的类目编号，设置到对应的目录中
        //将productInfo设置到foods中
        //过滤：不同的type进行不同的封装
        //将productInfo转成dto
        List<ProductCategoryDto> dtoList = collect.parallelStream().map(productCategoryDto -> {
            productCategoryDto.setProductInfoDtoList(productInfoList.stream()
                    .filter(productInfo -> productInfo.getCategoryType() == productCategoryDto.getCategoryType())
                    .map(productInfo -> ProductInfoDto.build(productInfo)).collect(Collectors.toList()));
            return productCategoryDto;
        }).collect(Collectors.toList());

        return ResultResponse.success(dtoList);
    }
}
