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
 * Created by zhujx on 2017/11/21.
 * 还款信息
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_repay")
public class Repay {

    //主键id
    @Id
    private Long id;

    //用户id
    private String userId;

    //账单编号
    private String billNo;

    //还款编号
    private String repayNo;

    //还款时间
    private Date repayTime;

    //还款金额
    private BigDecimal repayAmount;

    //还款姓名
    private String repayName;

    //还款身份证号
    private String repayIdNumber;

    //还款账号
    private String repayAccount;

    //支付通道
    private String repayChannel;

    //还款状态
    private String repayStatus;

    //还款方式
    private String repayType;

    //支付宝用户ID
    private String userZfbId;

    //支付宝账户
    private String userZfbName;

    //实际入账时间
    private Date actualRecordedTime;

    //第三方手续费
    private BigDecimal thirdProcedureFee;

    //是否已删除
    private Integer deleteFlag;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;

    //优惠券id
    private String couponId;

    //现金券使用金额
    private BigDecimal couponUseFee;

    //渠道名称（马甲包来源）
    private String appName;
}
