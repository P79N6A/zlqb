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
 **/
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundInfoModel implements Serializable{
    //
    private String fundId;
    //姓名
    private String name;
    //证件号码
    private String idNumber;
    //投资金额
    private BigDecimal investmentAmount;
    //入账时间
    private String accountDate;
    //回款时间
    private String backDate;
    //总收益
    private BigDecimal totalProfit;
    //回款金额（总金额）
    private BigDecimal backAmount;
    //状态（逻辑判断）
    private String status;

}
