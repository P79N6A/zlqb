package com.nyd.order.model.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CreatePayOrderDto {
    /**
     * 支付类型， 1表示代扣
     */
    private Integer payType;
    /**
     * 支付渠道编号
     */
    private String payChannelCode;
    /**
     * 支付渠道编号
     */
    private String payChannelCodeBx;
    /**
     * 客户身份证号
     */
    private String idNumber;
    /**
     * 客户手机号
     */
    private String mobile;
    /**
     * 客户银行卡号
     */
    private String bankcardNo;
    /**
     * 支付金额
     */
    private Double payAmount;
    /**
     * appName
     */
    private String merchantCode;
    /**
     * 业务订单类型
     */
    private String businessOrderType;
    /**
     * 业务订单号
     */
    private String businessOrderNo;
    /**
     * 回调url
     */
    private String callbackUrl;
    /**
     * 保险code
     */
    private String divideCode;
    /**
     * 拆分代扣code(no_split)
     */
    private String splitCode;

}
