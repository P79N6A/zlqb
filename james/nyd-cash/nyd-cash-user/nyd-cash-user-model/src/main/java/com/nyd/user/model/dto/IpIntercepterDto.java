package com.nyd.user.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
public class IpIntercepterDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1666440085585432232L;
	
	/**
	 * 请求次数
	 */
	private int count ;
	/**
	 * 第一次请求时间
	 */
	private Date startTime;
	/**
	 * 次数内最后一次时间
	 */
	private Date endTime;
	/**
	 * 禁止登录截止时间
	 */
	private Date forbidenTime;
	/**
	 * 禁止标识
	 */
	private boolean forbidenFlag;
	
}
