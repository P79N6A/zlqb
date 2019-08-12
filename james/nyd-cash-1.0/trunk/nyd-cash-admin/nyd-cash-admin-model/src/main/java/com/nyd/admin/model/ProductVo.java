package com.nyd.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Dengw on 2017/12/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductVo implements Serializable {
    //金融产品code
    private String productCode;
    //金融产品名称
    private String productName;
    //产品类型
    private String productType;
    //产品期数
    private Integer productPeriods;
    //日利率
    private BigDecimal interestRate;
    //金融产品使用的产品线
    private String business;
}
