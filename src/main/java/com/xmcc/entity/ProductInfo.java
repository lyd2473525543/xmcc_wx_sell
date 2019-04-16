package com.xmcc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_info")
@Entity
@DynamicUpdate
public class ProductInfo implements Serializable {
    @Id
    private String productId;
    private String productName;// 名字
    private BigDecimal productPrice;//单价
    private Integer productStock;//库存
    private String productDescription; // 描述
    private String productIcon;   //小图
    private Integer productStatus;//状态, 0正常1下架
    private Integer categoryType;//类目编号
    private Date createTime;
    private Date updateTime;
}
