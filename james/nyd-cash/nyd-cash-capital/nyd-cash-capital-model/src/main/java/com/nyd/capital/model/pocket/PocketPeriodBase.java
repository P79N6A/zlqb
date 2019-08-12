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
public class PocketPeriodBase {
    /**
     * 年利率:百分比 例如7.5表示7.5%利率
     */
    private String apr;
    /**
     * 第几期
     */
    private String period;
    /**
     * 预期还款利息(单位:分)
     */
    private String planRepaymentInterest;
    /**
     * 预期还款金额(单位:分)
     */
    private String planRepaymentMoney;
    /**
     * 预期还款本金(单位:分)
     */
    private String planRepaymentPrincipal;
    /**
     * 预期还款时间戳
     */
    private String planRepaymentTime;
}
