package com.nyd.admin.model.fundManageModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Peng
 * @create 2018-01-02 11:12
 * 资金详情页面
 **/
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundDetailModel implements Serializable{

    //资产ID
    private String fundId;
    //投资金额（单期投资金额）
    private BigDecimal investmentAmount;
    //投资期限
    private String investmentTerm;
    //利率
    private BigDecimal returnRate;
    //到期收益
    private BigDecimal expiryProfit;
    //是否续投
    private String continueFlag;
    //续投方式
    private String continueType;
    //到期日
    private String expiryDate;




}
