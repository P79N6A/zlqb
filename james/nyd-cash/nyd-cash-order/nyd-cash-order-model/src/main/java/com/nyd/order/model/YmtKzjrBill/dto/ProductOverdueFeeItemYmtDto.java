package com.nyd.order.model.YmtKzjrBill.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: zhangp
 * @Description: ymt 逾期记录配置表
 * @Date: 13:34 2018/7/7
 */
@Data
public class ProductOverdueFeeItemYmtDto implements Serializable {
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
