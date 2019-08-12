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
public class PocketCreateOrderRequest implements Serializable {

    /**
     * 时间戳
     */
    private Integer timestamp;

    /**
     * 账号
     */
    private String account;

    /**
     * 身份证号
     */
    private String id_number;

    /**
     * 用户基本信息json
     */
    private String user_base;

    /**
     * 订单基本信息json
     */
    private String order_base;

    /**
     * 打款银行卡json
     */
    private String receive_card;

    /**
     * 扣款银行卡json（不传）
     */
    private String debit_card;

    /**
     * 总还款信息json（不传）
     */
    private String repayment_base;

    /**
     * 还款计划信息，债转可不传
     * 数组内的数据格式也为json（不传）
     */
    private List<String> period_base;

    /**
     * 其他信息json（不传）
     */
    private String other;

    /**
     * 签名
     */
    private String sign;
}
