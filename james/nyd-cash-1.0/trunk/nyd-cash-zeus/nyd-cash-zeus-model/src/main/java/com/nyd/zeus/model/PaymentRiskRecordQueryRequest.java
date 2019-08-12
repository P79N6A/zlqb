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
public class PaymentRiskRecordQueryRequest {

	// 流水号
	private String seriNo;
	
	// 
	private String channelCode;
	
	private String orderNo;
	

}
