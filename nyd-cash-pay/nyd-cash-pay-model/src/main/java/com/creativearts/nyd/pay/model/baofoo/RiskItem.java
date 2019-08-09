package com.creativearts.nyd.pay.model.baofoo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/12/12
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RiskItem implements Serializable{
    private String goodsCategory;
    private String userLoginId;
    private String userEmail;
    private String userMobile;
    private String registerUserName;
    private String identifyState;
    private String userIdNo;
    private String registerTime;
    private String registerIp;
    private String chName;
    private String chIdNo;
    private String chCardNo;
    private String chMobile;
    private String chPayIp;
    private String deviceOrderNo;
}
