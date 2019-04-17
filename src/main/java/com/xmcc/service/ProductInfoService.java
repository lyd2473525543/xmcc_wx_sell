package com.xmcc.service;

import com.xmcc.common.ResultResponse;
import com.xmcc.entity.ProductInfo;
import org.springframework.stereotype.Service;

@Service
public interface ProductInfoService {

    ResultResponse findAll();//获取所有商品信息

    ResultResponse<ProductInfo> findById(String productId);//根据id查询商品

    void updateProduct(ProductInfo productInfo);//修改商品库存的方法
}
