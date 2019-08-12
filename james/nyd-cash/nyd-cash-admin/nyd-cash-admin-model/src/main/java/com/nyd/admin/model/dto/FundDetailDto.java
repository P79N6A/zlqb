package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by hwei on 2018/1/2.
 */
@Data
public class FundDetailDto implements Serializable{
    //投资期限
    private Integer investmentTerm;
    //利率
    private BigDecimal returnRate;
    //到期收益
    private BigDecimal expiryProfit;
    //是否续投
    private Integer continueFlag;
    //续投方式
    private Integer continueType;
}
