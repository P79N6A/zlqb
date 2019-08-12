package com.nyd.zeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BillInfo implements Serializable{

    //用户id
    private String userId;

    //账单编号
    private String billNo;

    //订单编号
    private String orderNo;

    //第三方订单
    private String ibankOrderNo;

    //本次期数
    private Integer curPeriod;

    //总期数
    private Integer periods;

    //约定还款日期
    private Date promiseRepaymentDate;

    //实际结清时间
    private Date actualSettleDate;

    //本期应还金额（本息和）
    private BigDecimal curRepayAmount;

    //本期应还本金
    private BigDecimal repayPrinciple;

    //本期应还利息
    private BigDecimal repayInterest;

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

    //综合费用
    private BigDecimal syntheticalFee;

    //账单状态
    private String billStatus;

    //优惠券减免金额
    private BigDecimal couponDerateAmount;

    //应实收金额
    private BigDecimal receivableAmount;

    //剩余应还款金额
    private BigDecimal waitRepayAmount;

    //已还款金额
    private BigDecimal alreadyRepayAmount;

    //剩余还款本金
    private BigDecimal waitRepayPrinciple;

    //是否测试申请
    private Integer testStatus;
    //马甲名称
    private String appName;
    
    private BigDecimal managerFee;//平台服务费',
    
    private BigDecimal penaltyRate;//'罚息率',
    private BigDecimal lateFee;// '滞纳金',
    private BigDecimal penaltyFee;//'总罚息费',
    private int overdueDays;//'逾期天数',
    
    private int urgeStatus;//0默认值 1:待催收 2催收中 3 承若还款',
    //应还利息
    private BigDecimal shouldInterest;  
}
