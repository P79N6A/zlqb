package com.nyd.zeus.model.payment;

import java.util.Date;

import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@Table(name = "t_paychannel_temp_flow")
public class PaychannelTempFlow {

	// 支付渠道code
	private String payChannelCode;
	// 业务类型
	private String businessType;
	// 请求时间
	private Date requestTime;
	// 响应时间
	private Date responseTime;
	// 请求信息
	private String requestText;
	// 响应信息
	private String responseText;
	// 流水号
	private String seriNo;

}
