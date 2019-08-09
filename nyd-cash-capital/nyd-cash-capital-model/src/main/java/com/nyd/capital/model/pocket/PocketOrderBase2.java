package com.nyd.capital.model.pocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author liuqiu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PocketOrderBase2 {

    /**
     * 各厂商自己平台申请单号，请各自厂
     * 商确保必须唯一
     */
    private String outTradeNo;

    /**
     * 贷款金额，单位：分
     */
    private String moneyAmount;

    /**
     * 贷款方式(0:按天,1:按月,2:按年)
     */
    private String loanMethod;

    /**
     * 贷款期限,需要配合loan_method来
     * 定比如贷款3个月,则loan_method
     * 为1,loan_term为3
     */
    private String loanTerm;

    /**
     * 总共利息,单位：分
     */
    private String loanInterests;

    /**
     * 贷款年化率利率，最多2位小数，百分比
     * 例如7.5表示7.5%利率
     */
    private String apr;

    /**
     * 下单时间时间戳表示
     */
    private String orderTime;

    /**
     * 手续费,单位：分
     */
    private String counterFee;

    /**
     * 借款用途：1旅行、2教育、3装修、4租房、5个人消费、6经营周转、7购车、8医美
     */
    private String usageOfLoan;

    /**
     * 借款方式（1:借款人电子账户
     * 2:名义借款人）
     */
    private String lendPayType;
}
