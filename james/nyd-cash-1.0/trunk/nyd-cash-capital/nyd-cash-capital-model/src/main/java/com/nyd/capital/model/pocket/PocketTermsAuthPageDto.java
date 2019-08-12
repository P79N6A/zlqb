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
public class PocketTermsAuthPageDto {
    /**
     * 身份证号
     */
    private String idNumber;
    /**
     * 还款最高金额(单位分)
     */
    private String repayMaxAmt;
    /**
     * 缴费最高金额(单位分)
     */
    private String paymentMaxAmt;
    /**
     * 缴费授权签约到期日:YYYYmmdd 格式 例如：20180422
     */
    private String paymentDeadline;
    /**
     * 还款授权签约到期日:YYYYmmdd 格式 例如：20180422
     */
    private String repayDeadline;
    /**
     * 同步跳转地址:不能长于256个字符，否则银行校验失败
     */
    private String retUrl;
    /**
     * 回调地址:没有为空(可不传)
     */
    private String notifyUrl;
    /**
     * 是否需要url:1或者0 地址只能使用一次，并且3分钟内有效
     */
    private String isUrl;
}
