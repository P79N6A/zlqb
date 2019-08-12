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
public class PocketRepaymentBase {
    /**
     * 还款方式:0-等本等息 1-等额本息 2-一次性还款
     */
    private String repaymentType;
    /**
     * 总还款期数
     */
    private String period;
    /**
     * 还款利息(单位:分)
     */
    private String repaymentInterest;
    /**
     * 总还款金额(单位:分)
     */
    private String repaymentAmount;
    /**
     * 还款本金(单位:分)
     */
    private String repaymentPrincipal;
    /**
     * 最迟还款日期时间戮
     */
    private String repaymentTime;
}
