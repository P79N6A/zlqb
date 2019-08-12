/**
 * Project Name:nyd-cash-pay-model
 * File Name:CjBusinessZftQuickPayMentResuest.java
 * Package Name:com.creativearts.nyd.pay.model.business.changjie
 * Date:2018年9月11日下午4:30:35
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.business.changjie;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * ClassName:畅捷直接支付业务请求参数 <br/>
 * Date:     2018年9月11日 下午4:30:35 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class ZftQuickPayMentVo implements Serializable{

	private static final long serialVersionUID = -6706114134934438622L;

	/**用户编号*/
	private String userId;
	
	/**手机操作系统*/
	private String platformType;
	
	/**支付类型:repayNyd */
	private String sourceType;
	
	/**账单编号(用户订单号)*/
	private String billNo;
	
	/**金额*/
	private BigDecimal amount;
	
	/**用户姓名*/
	private String name;
	
	/**身份证号*/
	private String idCard;
	
	/**银行卡号*/
	private String bankNo;
	
	/**银行预留手机号*/
	private String mobile;
	
	//优惠券id
    private String couponId;

    //现金券id
    private String cashId;

    //现金券使用金额
    private BigDecimal couponUseFee;
}

