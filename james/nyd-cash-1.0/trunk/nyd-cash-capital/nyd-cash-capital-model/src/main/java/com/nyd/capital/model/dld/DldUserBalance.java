package com.nyd.capital.model.dld;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 多来点账户余额信息
 * @author zhangdk
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DldUserBalance implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1666440085585432232L;
	/**
	 * 上一次账户余额
	 */
	private BigDecimal firstBalance ;
	/**
	 * 上一次查询时间
	 */
	private String queryTime;
	/**
	 * 预警值1
	 */
	private BigDecimal warnBalance;
	
}
