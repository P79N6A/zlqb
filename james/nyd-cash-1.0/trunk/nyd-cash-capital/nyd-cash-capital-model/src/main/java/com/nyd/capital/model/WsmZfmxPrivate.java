package com.nyd.capital.model;

import com.nyd.capital.model.annotation.EncryptField;
import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/11/13
 **/
public @Data
class WsmZfmxPrivate implements Serializable{
    //1对公  2对私
    private String flag="2";
    //开户行编号
    private String khhbh;

    //姓名
    @EncryptField
    private String xm;
    //身份证号
    @EncryptField
    private String sfzh;

    //银行卡号
    @EncryptField
    private String yhkh;
    //金额
    private String value;

    ////是否是居间服务费标识，1-是，2-否
    private String jjfwf;

    //是否一笔打款标识，1-是，2-否
    private String ybdk;

}
