package com.xmcc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("分页参数实体类")
public class PageDto {
    @NotBlank(message = "买家微信openid不能为空")
    @ApiModelProperty(value = "买家微信openid",dataType = "String")
    private String openid;
    @ApiModelProperty(value = "页数",dataType = "Integer")
    private Integer page = 0;
    @ApiModelProperty(value = "每页页数",dataType = "Integer")
    private Integer size = 10;
}
