package com.nyd.order.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hwei on 2018/10/22.
 */
@Data
@ToString
public class WithholdCallBackInfo implements Serializable{
    private String payOrderNo;
    private String withholdOrderNo;
    //代扣支付金额
    private double amount;
    //成功支付金额总和
    private double sumAmount;
    //1 表示支付成功 2 表示支付失败
    private int result;
}
