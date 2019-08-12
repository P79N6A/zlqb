package com.creativearts.nyd.pay.model.baofoo;

import lombok.Data;
import lombok.ToString;

/**
 * @author liuqiu
 */
@Data
@ToString
public class BaoFooPreparebindVo {
    /**
     * 终端号
     */
    private String terminal_id;
    /**
     * 商户号
     */
    private String member_id;
    /**
     * 银行卡号
     */
    private String acc_no;
    /**
     * 银行卡类型
     */
    private String card_type;
    /**
     * 持卡人姓名
     */
    private String card_holder;
    /**
     * 证件类型:01-身份证
     */
    private String id_card_type;
    /**
     * 证件号码
     */
    private String id_card;
    /**
     * 银行卡预留手机号
     */
    private String mobile;
    /**
     * 交易日期
     */
    private String trade_date;
    /**
     * 用户ID
     */
    private String user_id;
    /**
     * 请求流水号:"TSN"+System.currentTimeMillis()
     */
    private String trans_serial_no;

}
