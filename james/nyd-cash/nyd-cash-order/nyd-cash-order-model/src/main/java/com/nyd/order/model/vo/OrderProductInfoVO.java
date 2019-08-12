package com.nyd.order.model.vo;

import java.io.Serializable;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 订单详情-产品信息
 */
@Data
public class OrderProductInfoVO implements Serializable {


	@ApiModelProperty(value = "订单编号")
	private String orderNo;
	@ApiModelProperty(value = "申请时间")
	private String applayTime;
	@ApiModelProperty(value = "申请金额")
	private String applayAmount;
	@ApiModelProperty(value = "申请期限")
	private String applayPeriods;
	@ApiModelProperty(value = "产品利率")
	private String productRate;
	@ApiModelProperty(value = "放款产品")
	private String prodcutName;
	@ApiModelProperty(value = "借款用途")
	private String purpose;
	@ApiModelProperty(value = "放款时间")
	private String loanTime;
	@ApiModelProperty(value = "放款金额")
	private String loanAmount;
	@ApiModelProperty(value = "应还日期")
	private String repaymentTime;
	@ApiModelProperty(value = "应还本金")
	private String shouldPrincipal;
	@ApiModelProperty(value = "应还利息")
	private String shouldInterest;
	@ApiModelProperty(value = "应还服务费")
	private String shouldServiceFee;
	@ApiModelProperty(value = "实收评估费用")
	private String assessmentAmount;
	@ApiModelProperty(value = "结清日期")
	private String settleTime;
	@ApiModelProperty(value = "逾期罚息")
	private String overdueInterest;
	@ApiModelProperty(value = "已还金额")
	private String alreadyAmount;
	@ApiModelProperty(value = "剩余应还")
	private String remainsAmount;
	@ApiModelProperty(value = "平台服务费")
	private String managerFee;
	@ApiModelProperty(value = "滞纳金")
	private String lateFee;
	
}
