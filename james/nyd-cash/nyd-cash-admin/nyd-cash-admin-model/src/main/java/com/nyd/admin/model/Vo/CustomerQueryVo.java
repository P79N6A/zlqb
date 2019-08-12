package com.nyd.admin.model.Vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class CustomerQueryVo implements Serializable {
    //客户姓名
    private String realName;

    //客户编号
    private String userId;

    //手机号码
    private String accountNumber;

    //身份证号
    private String idNumber;

    //客户等级
    private String preAuditLevel;

    //APP名称
    private String appName;

    //用户来源
    private String source;

    //注册时间
    private String registerTime;

    //账户余额
    private BigDecimal totalCount;

    //银行名称
    private String bankName;

    //银行卡号
    private String cardNo;

    //预留手机号
    private String reserveMobile;
}
