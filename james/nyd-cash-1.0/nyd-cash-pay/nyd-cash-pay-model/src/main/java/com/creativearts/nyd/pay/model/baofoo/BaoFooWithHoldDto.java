package com.creativearts.nyd.pay.model.baofoo;

import lombok.Data;
import lombok.ToString;

/**
 * @author liuqiu
 */
@Data
@ToString
public class BaoFooWithHoldDto {
    /**
     * 卡号
     */
    private String cardNo;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 身份证号码
     */
    private String idNumber;
    /**
     * 还款金额
     */
    private String amount;
    /**
     * app名称
     */
    private String appName;
    /**
     * 账单编号
     */
    private String billNo;
    /**
     * 用户ID
     */
    private String userId;
}
