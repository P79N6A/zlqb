package com.nyd.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Dengw on 2017/11/8
 * 订单详情信息
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class OrderDetailInfo implements Serializable{
    //订单id
    private String orderNo;
    //用户ID
    private String userId;
    //手机号
    private String mobile;
    //证件类型
    private String idType;
    //姓名
    private String realName;
    //身份证号码
    private String idNumber;
    //用户来源
    private String source;
    //金融产品Id
    private String productCode;
    //手机型号
    private String model;
    //手机品牌
    private String brand;
    //手机设备号
    private String deviceId;
    //是否有砍头息
    private Integer isBeheadInterest;
    //砍头息比例
    private BigDecimal beheadPercentage;
    //产品类型
    private Integer productType;
    //产品期数
    private Integer productPeriods;
    //日利率
    private BigDecimal interestRate;
    //快速信审费
    private BigDecimal fastAuditFee;
    //账户管理费
    private BigDecimal accountManageFee;
    //身份验证费
    private BigDecimal identityVerifyFee;
    //手机验证费
    private BigDecimal mobileVerifyFee;
    //银行卡验证费
    private BigDecimal bankVerifyFee;
    //征信验证费
    private BigDecimal creditServiceFee;
    //信息发布费
    private BigDecimal informationPushFee;
    //浮动服务费
    private BigDecimal slidingFee;
    //优惠券类别
    private Integer couponType;
    //是否已使用vip新口子
    private Integer clickVipFlag;
    //优惠券code
    private String couponCode;
    //优惠券减免金额
    private BigDecimal couponDerateAmount;
    //评估报告key
    private String assessKey;
}
