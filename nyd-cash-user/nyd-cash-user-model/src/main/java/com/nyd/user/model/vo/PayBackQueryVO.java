package com.nyd.user.model.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 订单详情-客户信息
 */
@Data
public class PayBackQueryVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "B001-还款中,B002-逾期中,B003-已结清")
    private String billStatus;
	
	@ApiModelProperty(value = "距离还款天数，未逾期使用")
	private int remainDays;
	@ApiModelProperty(value = "逾期天数，逾期使用")
	private int overdueDays;
	
	@ApiModelProperty(value = "最后还款日（时间戳）")
    private long promiseRepaymentDate;
	@ApiModelProperty(value = "剩余应还金额")
    private BigDecimal waitRepayAmount;
	@ApiModelProperty(value = "本金")
    private BigDecimal repayPrinciple;
	@ApiModelProperty(value = "利息")
    private BigDecimal repayInterest;
	@ApiModelProperty(value = "平台服务费")
	private BigDecimal managerFee;
	@ApiModelProperty(value = "罚息")
	private BigDecimal penaltyFee;
	@ApiModelProperty(value = "违约金")
	private BigDecimal lateFee;
	
	@ApiModelProperty(value = "银行卡号")
    private String bankAccount;
	@ApiModelProperty(value = "持卡人姓名")
    private String accountName;
	@ApiModelProperty(value = "身份证证件号")
	private String idNumber;
	@ApiModelProperty(value = "银行卡预留手机号")
    private String reservedPhone;
	
}
