package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;

/**
 *获取验证码请求实体类
 * @author cm
 */
@Data
public class JxGetCheckCodeRequest implements Serializable {

    //版本
    private String version;

    //备注：见接口名称括号内容
    private String txCode;

    //第三方标识
//    private String appId;

    //机构号
    private String instCode;

    //渠道
    private String channel;

    //请求年月日(格式:YYYYMMDD)
    private String txDate;

    //请求时分秒(格式:HHMMSS)
    private String txTime;

    //6位随机数
    private String seqNo;

    //
    private String acqRes;

    //会员Id
    private String memberId;

    //操作类型
    private String kind;

    //手机号(Kind=Registration时必填)
    private String mobile;

    //银行卡号(Kind=BankCard时必填)
    private String bankCardNumber;

    //sign
    private String sign;


}
