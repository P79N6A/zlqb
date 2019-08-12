package com.nyd.capital.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/12/13
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAccountVo implements Serializable{
    private Integer idType; //证件类型  1 身份证
    private String idNo;  // 证件号码
    private String name;  // 姓名
    private String mobile; // 手机号
    private String cardNo; // 银行卡号
    private String smsCode; //短信验证码
    private String userId;
    private String p2pId;//开户id
}
