package com.nyd.capital.model.pocket;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 口袋理财打款银行卡信息
 * @author liuqiu
 */
@Data
@ToString
public class PocketReceiveCard implements Serializable {

    /**
     * 打款银行卡ID，详细查看附页
     * 打款银行字典
     */
    private int bank_id;

    /**
     * 银行卡号
     */
    private String card_no;

    /**
     * 银行卡预留手机号
     */
    private String phone;

    /**
     * 持卡人姓名
     */
    private String name;

    /**
     * 开户行地址(非必传)
     */
    private String bank_address;

}
