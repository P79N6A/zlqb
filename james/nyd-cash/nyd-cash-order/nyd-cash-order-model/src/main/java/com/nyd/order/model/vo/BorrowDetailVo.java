package com.nyd.order.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Dengw on 2017/11/20
 * 借款详情返回对象
 */
@Data
public class BorrowDetailVo {
    //订单号（借款编号）
    private String orderNo;
    //借款金额
    private BigDecimal loanAmount;
    //借款天数
    private Integer borrowTime;
    //借款申请时间
    private String loanTime;
    //放款时间
    private String payTime;
    //约定还款时间
    private String promiseRepaymentTime;
    //实际还款时间
    private String actualRepaymentTime;
    //利息
    private BigDecimal interest;
    //综合费用
    private BigDecimal syntheticalFee;
    //应还总金额
    private BigDecimal repayTotalAmount;
    //订单状态-状态说明
    private Integer orderStatus;
    //放款渠道
    private String fundCode;
    //银行卡号
    private String bankAccount;
    //是否签约
    private Integer ifSign;
    //会员费
    private BigDecimal memberFee;
    //是否提示
    private Integer ifNotice;
    //是否开户(0-未开户,1-已开户)
    private String ifOpenPage;
}
