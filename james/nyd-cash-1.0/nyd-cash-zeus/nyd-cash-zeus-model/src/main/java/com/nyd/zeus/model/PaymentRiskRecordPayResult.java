package com.nyd.zeus.model;

import java.math.BigDecimal;
import java.util.Date;

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
public class PaymentRiskRecordPayResult {

	// 状态值
	private String status;
	// 扣款金额
	private BigDecimal money;
	// 流水号
	private String seriNo;
	// 请求时间
	private Date requestTime;
	// 响应时间
	private Date responseTime;
	// 请求数据
	private String requestText;
	// 响应数据
	private String responseText;
	
	
	

}
