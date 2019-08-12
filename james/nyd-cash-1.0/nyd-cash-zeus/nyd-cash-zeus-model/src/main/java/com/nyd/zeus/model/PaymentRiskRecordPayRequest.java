package com.nyd.zeus.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *   风控还款统一处理
 * @param PaymentRiskRecordPayResult
 * @return
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRiskRecordPayRequest implements Serializable{

	
	// 渠道code
	@ApiModelProperty("渠道code")
	private String channelCode;
	/*
	 * // 银行名称
	 * 
	 * @ApiModelProperty("银行名称") private String bankName; // 银行卡号
	 * 
	 * @ApiModelProperty("银行卡号") private String bankNo; // 银行账户名称
	 * 
	 * @ApiModelProperty("银行账户名称") private String bankAccountName; // 银行卡手机号
	 * 
	 * @ApiModelProperty("银行卡手机号") private String bankMobile; // 银行code
	 * 
	 * @ApiModelProperty("银行code") private String bankCode; // 身份证号
	 * 
	 * @ApiModelProperty("身份证号") private String ic; // 扣款金额
	 * 
	 * @ApiModelProperty("扣款金额") private BigDecimal money;
	 */
	

}
