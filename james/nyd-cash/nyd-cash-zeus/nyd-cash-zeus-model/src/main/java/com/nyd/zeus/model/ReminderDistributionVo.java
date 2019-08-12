package com.nyd.zeus.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by zhujx on 2017/11/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReminderDistributionVo implements Serializable {

	@ApiModelProperty("最近一次提醒")
	private String remindMsg;
	@ApiModelProperty("受理人员")
	private String receiveUserName;
	@ApiModelProperty("客户姓名")
	private String userName;
	@ApiModelProperty("手机号码")
	private String userMobile;
	@ApiModelProperty("申请日期")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private String applyTime;
	@ApiModelProperty("放款日期")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private String loanTime;
	@ApiModelProperty("放款产品")
	private String productName;
	@ApiModelProperty("注册渠道")
	private String source;
	@ApiModelProperty("借款期次")
	private Integer loanNum;
	@ApiModelProperty("放款金额")
	private BigDecimal repayPrinciple;
	@ApiModelProperty("已还金额")
	private BigDecimal alreadyRepayAmount;
	@ApiModelProperty("剩余金额")
	private BigDecimal waitRepayAmount;
	@ApiModelProperty("距还款日")
	private Integer lessDays;
	@ApiModelProperty("应还日期")
	private String promiseRepaymentDate;
	@ApiModelProperty("应还本息")
	private BigDecimal curRepayAmount;
	@ApiModelProperty("信审人员")
	private String creditTrialUserName;
	@ApiModelProperty("贷款编号")
	private String orderNo;
	@ApiModelProperty("订单用户id")
	private String userId;
}
