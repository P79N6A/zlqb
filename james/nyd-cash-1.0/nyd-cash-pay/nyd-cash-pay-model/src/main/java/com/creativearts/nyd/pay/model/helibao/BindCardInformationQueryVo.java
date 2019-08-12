package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

/**
 * 绑定银行卡信息查询请求参数
 */
@Data
public class BindCardInformationQueryVo implements Serializable {

    //交易类型
    private String P1_bizType = "BankCardbindList";

    //商户编号
    private String P2_customerNumber;

    //用户id
    private String P3_userId;

    //绑定id
    private String P4_bindId;

    //时间戳
    private String P6_timestamp;

    //签名
    private String sign;
}
