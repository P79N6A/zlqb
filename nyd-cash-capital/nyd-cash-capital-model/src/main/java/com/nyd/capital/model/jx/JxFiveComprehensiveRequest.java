package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;

/**
 * 五合一接口请求实体类
 * @author  cm
 */
@Data
public class JxFiveComprehensiveRequest implements Serializable{

    //版本(配置中)
    private String version;

    //备注：见接口名称括号内容(配置中)
    private String txCode;

    //第三方标识
//    private String appId;

    //机构号(配置中)
    private String instCode;

    //渠道(配置中)
    private String channel;

    //请求年月日(格式:YYYYMMDD)
    private String txDate;

    //请求时分秒(格式:HHMMSS)
    private String txTime;

    //6位随机数
    private String seqNo;

    //(可以不传)
    private String acqRes;

    //手机号
    private String mobile;

    //姓名
    private String realName;

    //身份证
    private String idCardNumber;

    //回调地址
    private String returnUrl;

    //银行卡号
    private String bankCardNumber;

    //适配平台(非必传)
    private String platform;

    //签名
    private String sign;






}
