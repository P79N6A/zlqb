package com.nyd.capital.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author zhangdk
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Risk2CapitalMessage implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	// 消息id -订单号
	private String messageId;
	// 电话
	private String phone;
	// 用户
	private String userId;
	// 订单号
	private String orderNo;
	// 产品类型
	private String productType;
	// 产品类型
	private String borrowType;
	// 决策结果 1 0
	private String riskStatus;
	// 拒绝原因
	private String riskReason;

	private String ip;
	private String queueName;
	private String systemName;
}
