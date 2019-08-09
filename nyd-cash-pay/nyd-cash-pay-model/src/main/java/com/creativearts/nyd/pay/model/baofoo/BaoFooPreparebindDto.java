package com.creativearts.nyd.pay.model.baofoo;

import lombok.Data;
import lombok.ToString;

/**
 * @author liuqiu
 */
@Data
@ToString
public class BaoFooPreparebindDto {
    /**
     * 银行卡号
     */
    private String bankNo;
    /**
     * 银行卡预留手机号
     */
    private String mobile;
    /**
     * 用户ID
     */
    private String userId;

}
