package com.nyd.order.model.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liuqiu
 */
@Data
@ToString
public class ProductVo implements Serializable {

    /**
     * 金融产品code
     */
    private String productCode;
    /**
     * 日利率
     */
    private BigDecimal interestRate;
    /**
     * 借款金额
     */
    private BigDecimal  loanAmount;
    /**
     * 借款天数
     */
    private Integer borrowTime;
    /**
     * 是否推荐:0推荐，1不推荐
     */
    private String recommendFlag;
}
