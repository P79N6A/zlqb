package com.nyd.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Dengw on 17/11/6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductOverdueFeeItemInfo implements Serializable {
    //金融产品code
    private String productCode;
    //滞纳金
    private BigDecimal overdueFine;
    //档位逾期费率计算天数
    private Integer gearOverdueFeeDays;
    //第一档预期费率
    private BigDecimal firstGearOverdueRate;
    //第二档预期费率
    private BigDecimal secondGearOverdueRate;
    //最大逾期费率
    private BigDecimal maxOverdueFeeRate;
}
