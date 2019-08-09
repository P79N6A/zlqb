package com.nyd.settlement.model.po.repay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Peng
 * @create 2018-01-18 11:19
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RepayPo {
    //用户Id
    private String userId;
    //还款编号
    private String repayNo;
    //账单编号
    private String billNo;
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
    //支付宝 用户id
    private String userZfbId;
    //用户支付宝号
    private String userZfbName;
    //还款状态  0：成功；1：失败
    private String repayStatus;
    //还款方式
    private Integer repayType;
    //实际入账日
    private Date actualRecordedTime;
    //第三方手续费
    private BigDecimal thirdProcedureFee;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //最后修改人
    private String updateBy;
    //减免类型
    private String derateType;
    //减免金额
    private BigDecimal derateAmountl;
    //备注
    private String remark;
    //合同金额
    private BigDecimal loanAmount;
    //订单号
    private String orderNo;
    //手机号
    private String mobile;

}
