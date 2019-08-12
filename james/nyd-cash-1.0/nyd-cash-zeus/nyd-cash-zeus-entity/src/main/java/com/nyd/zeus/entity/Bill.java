package com.nyd.zeus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/18.
 * 账单表
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_bill")
public class Bill {

    //主键id
    @Id
    private Long id;

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
    
  
    //账单状态
    private String billStatus;

    //应实收金额
    private BigDecimal receivableAmount;

    //剩余应还款金额
    private BigDecimal waitRepayAmount;

    //已还款金额
    private BigDecimal alreadyRepayAmount;

     //是否已删除
    private Integer deleteFlag;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;
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
