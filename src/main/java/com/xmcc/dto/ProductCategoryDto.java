package com.xmcc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xmcc.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryDto {

    @JsonProperty("name")
    private String categoryName;//类目名字
    @JsonProperty("type")
    private Integer categoryType;//类目编号
    @JsonProperty("foods")
//防止为null时被忽略
    public List<ProductInfoDto> productInfoDtoList;

    //转换成Dto
    public static ProductCategoryDto build(ProductCategory productCategory){
        ProductCategoryDto productCategoryDto = new ProductCategoryDto();
        BeanUtils.copyProperties(productCategory,productCategoryDto);
        return productCategoryDto;
    }
}
