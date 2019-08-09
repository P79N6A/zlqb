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
public class PocketComplianceBorrowPageDto {
    /**
     * 订单唯一标识ID
     */
    private String orderId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证
     */
    private String idNumber;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 银行卡号
     */
    private String cardNo;
    /**
     * 借款用途:1旅行、2教育、3装修、4租房、5个人消费、6经营周转、7购车、8医美
     */
    private String loanPurpose;
    /**
     * 贷款方式:0:按天,1:按月,2:按年
     */
    private String loanMethod;
    /**
     * 贷款期限:需要配合loanMethod来定 比如贷款3个月,则loanMethod为1,loanTerm为3
     */
    private String loanTerm;
    /**
     * 还款来源:1 个人收入、2经营活动
     */
    private String repayerType;
    /**
     * 借款金额
     */
    private String loanMoney;
    /**
     * 服务费
     */
    private String counterFee;
    /**
     * 借款利息
     */
    private String loanInterests;
    /**
     * 总计应还
     */
    private String planRepaymentMoney;
    /**
     * 综合借款成本:36表示综合借款成本36%
     */
    private String borrowCost;
    /**
     * 同步跳转地址
     */
    private String returnUrl;
    /**
     * 回调地址:传地址则进行回调 不传默认不回调
     */
    private String notifyUrl;
}
