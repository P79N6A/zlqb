package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;

@Data
public class JxUserDetail implements Serializable{

    //姓名
    private String realName;

    //手机号
    private String accountNumber;

    //银行卡号
    private String bankAccount;

    //身份证号
    private String idNumber;

}
