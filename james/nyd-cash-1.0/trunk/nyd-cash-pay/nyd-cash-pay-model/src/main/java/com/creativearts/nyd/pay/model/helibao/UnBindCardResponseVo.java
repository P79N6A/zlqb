package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class UnBindCardResponseVo implements Serializable {

    //交易类型
    private String rt1_bizType;

    //返回码
    private String rt2_retCode;

    //返回信息
    private String rt3_retMsg;

    //商户编号
    private String rt4_customerNumber;

    //签名
    private String sign;
}
