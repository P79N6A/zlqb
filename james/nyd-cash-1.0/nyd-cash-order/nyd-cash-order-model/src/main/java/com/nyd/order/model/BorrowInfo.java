package com.nyd.order.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BorrowInfo implements Serializable{
    //订单id
    private String orderNo;
    //用户ID
    private String userId;
    //综合费用
    private BigDecimal syntheticalFee;
    //实际借款金额
    private BigDecimal realLoanAmount;
    //应还总金额
    private BigDecimal repayTotalAmount;
    //借款时间（天数）
    private Integer borrowTime;
    //约定还款时间
    private String promiseRepaymentTime;
    //利息
    private BigDecimal interest;
    //借款申请时间
    private String loanTime;
    //放款时间
    private String payTime;
    //订单状态
    private Integer orderStatus;
	//账单状态
	private String billStatus;
	//管理费
    private BigDecimal managerFee;
	//剩余应还款金额
    private BigDecimal waitRepayAmount;
}
