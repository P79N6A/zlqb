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
public class PocketAccountOpenEncryptPageDto {
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 性别:M 男性 F 女性
     */
    private String gender;
    /**
     * 同步跳转地址:不能长于256个字符，否则银行校验失败
     */
    private String retUrl;
    /**
     * 回调地址:没有为空(可不传)
     */
    private String notifyUrl;
    /**
     * 所属行业:(可不传)
     */
    private String industry;
    /**
     * 工作性质:(可不传)
     */
    private String jobNature;
    /**
     * 还款来源:个人收入(可不传)
     */
    private String repaymentMoneySource;
    /**
     * 年收入:(可不传)
     */
    private String annualIncome;
    /**
     * 负债情况:无(可不传)
     */
    private String debtState;
    /**
     * 是否需要url:1或者0 地址只能使用一次，并且3分钟内有效(可不传)
     */
    private String isUrl;
}
