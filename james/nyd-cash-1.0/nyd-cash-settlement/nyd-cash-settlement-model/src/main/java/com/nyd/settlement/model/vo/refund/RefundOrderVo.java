package com.nyd.settlement.model.vo.refund;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author hwei
 * @create 2018-01-16 15:15
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RefundOrderVo {
    //产品类型
    private String productType;
    //标签
    private String testStatus;
    //资金渠道
    private String fundCode;
    //订单编号
    private String orderNo;
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
    //会员费
    private BigDecimal memberFee;

}
