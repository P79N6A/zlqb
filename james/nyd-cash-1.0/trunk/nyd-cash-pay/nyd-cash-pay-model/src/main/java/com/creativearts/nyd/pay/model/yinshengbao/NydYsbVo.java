package com.creativearts.nyd.pay.model.yinshengbao;

import com.creativearts.nyd.pay.model.NydPayVo;
import lombok.Data;

import java.math.BigDecimal;
/**
 * chaiming
 */

/**
 * 前台传过来的参数
 */

@Data
public class NydYsbVo extends NydPayVo{


    //他人代付
    private String name;
    private String mobile;
    private String idCard;

    private String sourceType;

    //会员类型
    private String memberType;

    private String productCode;

    //优惠券id
    private String couponId;

    //现金券id
    private String cashId;

    //现金券使用金额
    private BigDecimal couponUseFee;


    //短信验证码
    private String vericode;
    
    //渠道名称（马甲包来源）
    private String appName;
}
