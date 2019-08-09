package com.nyd.settlement.model.vo.refund;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hwei
 * @create 2018-01-16 15:15
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AlreadyRefundOrderVo {
    //产品类型
    private String productType;
    //标签
    private String testStatus;
    //资金渠道
    private String fundCode;
    //订单编号
    private String orderNo;
    //账单号
    private String billNo;
    //会员费ID
    private Integer memberId;
    //姓名
    private String realName;
    //手机号
    private String mobile;
    //合同金额
    private BigDecimal realLoanAmount;
    //借款周期
    private Integer borrowTime;
    //放款时间
    private String payTime;
    //还款日期（实际结清时间）或者是 线下就是refund中的createTime
    private String repayTime;
    //还款渠道
    private String offlineRepayChannel;
    //应还本息（应实收金额）
    private BigDecimal receivableAmount;
    //会员费
    private BigDecimal memberFee;
    //减免金额
    private BigDecimal couponDerateAmount;
    //退款金额
    private BigDecimal refundAmount;
    //退款账户
    private String refundAccount;
    //退款流水
    private String refundFlow;
    //退款方式
    private String refundChannel;
    //退款时间
    private String refundTime;
    //修改人
    private String updateBy;
    //备注
    private String remark;

}
