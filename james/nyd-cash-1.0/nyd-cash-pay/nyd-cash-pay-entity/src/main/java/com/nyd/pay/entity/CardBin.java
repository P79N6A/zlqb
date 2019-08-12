/**
 * Project Name:nyd-cash-pay-entity
 * File Name:CardBin.java
 * Package Name:com.nyd.pay.entity
 * Date:2018年9月18日下午8:26:04
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * ClassName:CardBin <br/>
 * Date:     2018年9月18日 下午8:26:04 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_card_bin")
public class CardBin {
	@Id
	/**主键*/
	private Long id;
	
	/**银行编码*/
	private String bankCode;
	
	/**卡类型：CC=贷记卡，DC=借记卡，SCC=准贷记卡，PC=预付卡*/
	private String cardType;
	
	/**卡种名称*/
	private String cardName;
	
	/**卡Bin*/
	private String binNo;
	
	/**卡号长度*/
	private Integer cardLength;
	
	/**发卡行代码*/
	private String bankNo;
	
	/**是否已删除 0：正常；1：已删除*/
	private int deleteFlag;
	
	/**添加时间*/
	private Date createTime;
	
	/**修改时间*/
	private Date updateTime;
	
	/**最后修改人*/
	private String updateBy;
	
	/**备注*/
	private String remarks;
}

