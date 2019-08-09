package com.creativearts.nyd.pay.model.baofoo;

import lombok.Data;
import lombok.ToString;

/**
 * @author liuqiu
 */
@Data
@ToString
public class PayCallbackDTO {
    /**
     * 支付订单号
     */
    private String payOrderNo;

    /**
     * 代扣订单号
     */
    private String withholdOrderNo;
    /**
     * 代扣支付金额
     */
    private double amount;
    /**
     * 支付结果(1 表示支付成功 2 表示支付失败)
     */
    private Integer result;
}
