package com.creativearts.nyd.pay.model.baofoo;


import lombok.Data;
import lombok.ToString;


/**
 * @author liuqiu
 */
@Data
@ToString
public class SelectBankCardDto {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * app名称
     */
    private String appName;
}
