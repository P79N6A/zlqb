package com.nyd.order.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Dengw on 2017/11/20
 * 借款确认接口参数
 */
@Data
public class BorrowConfirmDto implements Serializable{
    //用户userId
    private String userId;
    //金融产品code
    private String productCode;  //银码头必填
    //借款金额
    private BigDecimal loanAmount;
    //借款天数（时长）
    private Integer borrowTime;
    //借款用途
    private String loanPurpose;
    //利息
    private BigDecimal interest;
    //年化利率
    private BigDecimal annualizedRate;
    //到账金额
    private BigDecimal realLoanAmount;
    //应还总金额
    private BigDecimal repayTotalAmount;
    //银行卡号
    private String bankAccount;
    //银行名称
    private String bankName;
    //下单电话
    private String mobile;
    //会员类型
    private String type;
    //会员费
    private BigDecimal memberFee;
    //验证码
    private String msgCode;
    //综合费用
    private BigDecimal syntheticalFee;
    //账号
    private String accountNumber;
    //设备Id
    private String deviceId;
    //手机型号
    private String model;
    //手机品牌
    private String brand;
    //p2p账号Id
    private String p2pId;
    //fundCode
    private String fundCode;

    //操作系统
    private String os;
    //android版本
    private Integer version;
    //ios版本
    private String versionName;

    /**
     * 调用来源
     * 0 - 侬要贷, 1 - 银码头
     */
    private Integer channel;  //银码头必填

    /**
     * 银码头的来源订单号
     * 如果为空则为侬要贷自己的订单
     */
    private String ibankOrderNo;
    /**马甲名称*/
    private String appName;
}
