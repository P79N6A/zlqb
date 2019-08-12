package com.nyd.capital.model.pocket;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuqiu
 */
@Data
@ToString
public class PocketQueryOrderRequest implements Serializable {

    /**
     * 时间戳
     */
    private int timestamp;

    /**
     * 账号
     */
    private String account;

    /**
     * 身份证号
     */
    private String id_number;

    /**
     * 口袋理财平台订单ID
     */
    private int order_id;

    /**
     * 各厂商自己平台申请单号，
     * 与口袋平台订单ID选传一个即
     * 可
     */
    private String out_trade_no;

    /**
     * 签名
     */
    private String sign;
}
