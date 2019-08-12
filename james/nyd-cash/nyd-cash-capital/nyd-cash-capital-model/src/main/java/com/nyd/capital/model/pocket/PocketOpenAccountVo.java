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
public class PocketOpenAccountVo {

    /**
     * 电子账户
     */
    private String accountId;
    /**
     * 绑定银行卡
     */
    private String bindCard;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 是否绑卡:0 未绑卡 1已绑卡
     */
    private String isBindCard;
    /**
     * 身份证
     */
    private String idNo;
    /**
     * 还款最高金额(单位分)
     */
    private String repayMaxAmt;
    /**
     * 缴费最高金额(单位分)
     */
    private String paymentMaxAmt;
    /**
     * 缴费授权签约到期日:YYYYmmdd 格式
     */
    private String paymentDeadline;
    /**
     * 还款授权签约到期日:YYYYmmdd 格式
     */
    private String repayDeadline;
}
