package com.nyd.capital.model.pocket;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author liuqiu
 */
@Data
@ToString
public class PocketWithdrawRequest implements Serializable {

    /**
     * 项目名 kd_xqb
     */
    private String project_name;

    /**
     * 项目密码
     */
    private String pwd;

    /**
     * 放款订单号，固定30位
     */
    private String yur_ref;

    /**
     * 用户id(最长9位)
     */
    private int user_id;

    /**
     * 姓名
     */
    private String real_name;

    /**
     * 银行id
     */
    private int bank_id;

    /**
     * 银行卡号
     */
    private String card_no;

    /**
     * 扣款金额（分)
     */
    private String money;

    /**
     * 扣款手续费（分）
     */
    private String fee;

    /**
     * 备注
     */
    private String pay_summary;

    /**
     * 签名
     */
    private String sign;
}
