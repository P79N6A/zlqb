package com.creativearts.nyd.pay.model.helibao;

import com.creativearts.nyd.pay.model.annotation.RequireField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2017/12/18
 **/
@Data
public class NydHlbVo implements Serializable{

    private String platformType;
    private String userId;//

    private String billNo;//还款时 用到
    @RequireField
    private String bankNo;
    @RequireField
    private BigDecimal amount;
    private String name;
    private String idCard;

    private String sourceType;

    //会员类型
    private String memberType;

    private String productCode;

    //手机号码(此手机号码要与绑定的银行卡的手机号一致)
    private String phone;

    //短信验证码
//    private String messageCode;

    //优惠券id
    private String couponId;

    //现金券id
    private String cashId;

    //现金券使用金额
    private BigDecimal couponUseFee;

    /**以下字段是空中金融还款所需字段****/
    //资产编号
    private String assetCode;

    //子订单号
    private String orderSno;

    //渠道名称（BI统计需要）
    private String appName;
}
