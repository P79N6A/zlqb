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
public class CapitalMessage implements Serializable{
	 	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		//订单号
		private String orderNo;
		//useId
		private String userId;
		//产品类型 nyd ymt
		private String productType;
		//借款类型 0-单期 1-多期
		private String borrowType;
}
