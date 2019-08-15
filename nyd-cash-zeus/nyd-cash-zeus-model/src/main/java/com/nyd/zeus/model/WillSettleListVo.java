package com.nyd.zeus.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dengqingfeng on 2019/8/13.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WillSettleListVo implements Serializable {
    private String userId;
    @ApiModelProperty("账单编号")
    private String billNo;
    @ApiModelProperty("借款期次")
    private String loanNum;
    private String billStatus;
    @ApiModelProperty("还款状态")
    private String statusName;
    @ApiModelProperty("应还日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd" )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date promiseRepaymentDate;
    @ApiModelProperty("客户姓名")
    private String userName;
    @ApiModelProperty("手机号码")
    private String userMobile;
    @ApiModelProperty("应还金额")
    private BigDecimal repayTotalAmount;
    @ApiModelProperty("剩余应还金额")
    private BigDecimal waitRepayAmount;
    @ApiModelProperty("放款日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd" )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date loanTime;
    @ApiModelProperty("贷款编号")
    private String orderNo;
    @ApiModelProperty("滞纳金")
    private BigDecimal lateFee;
    @ApiModelProperty("罚息")
    private BigDecimal penaltyFee;
    @ApiModelProperty("服务费")
    private BigDecimal managerFee;
    @ApiModelProperty("应还本金")
    private BigDecimal repayPrinciple;
    @ApiModelProperty("借款期限")
    private String curPeriod;
    @ApiModelProperty("逾期天数")
    private String overdueDays;
    @ApiModelProperty("应还利率")
    private BigDecimal repayInterest;
    @ApiModelProperty("已还总金额")
    private BigDecimal alreadyRepayAmount;
    @ApiModelProperty("银行卡号")
    private String bankAccount;
    @ApiModelProperty("银行名称")
    private String bankName;
    @ApiModelProperty("应还利息")
    private BigDecimal shouldInterest;

}
