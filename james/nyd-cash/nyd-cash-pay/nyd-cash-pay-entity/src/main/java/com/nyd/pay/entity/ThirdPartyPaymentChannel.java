/**
 * Project Name:nyd-cash-pay-entity
 * File Name:OrderChannel.java
 * Package Name:com.nyd.pay.entity
 * Date:2018年9月12日上午10:14:26
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
 * ClassName:OrderChannel <br/>
 * Date:     2018年9月12日 上午10:14:26 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_order_channel")
public class ThirdPartyPaymentChannel {

	@Id
	/**主键*/
	private Long id;
	
	/**用户编号*/
	private String userId;
	
	/**源订单号,根据交易类型来填写：即还款和退款的源订单号*/
	private String sourceOrderId;
	
	/**造艺生成的交易号*/
	private String transId;
	
	/**第三方订单号*/
	private String thirdPaymentSerialNo;
	
	/**银行流水号*/
	private String bankSerialNo;
	
	/**真实姓名*/
	private String realName;
	
	/**身份证号*/
	private String cardNo;
	
	/**银行卡号*/
	private String bankNo;
	
	/**银行卡预留手机号*/
	private String phoneNo;
	
	/**第三方渠道编号*/
	private String channelCode;
	
	/**订单状态： 0：处理中，1：失败, 2：成功*/
	private Integer status;
	
	/**订单类型 1:充值现金券,2:支付评估费,3:还款*/
	private Integer orderType;
	
	/**支付金额*/
	private BigDecimal amount;
	
	/**实际支付金额*/
	private BigDecimal actualAmount;
	
	/**请求时间*/
	private Date reqTime;
	
	/**响应时间*/
	private Date resTime;
	
	/**是否已删除 0：正常；1：已删除*/
	private Integer deleteFlag;
	
	/**添加时间*/
	private Date createTime;
	
	/**修改时间*/
	private Date updateTime;
	
	/**最后修改人*/
	private String updateBy;
	
	/**备注*/
	private String remarks;
	
	//优惠券id
    private String couponId;

    //现金券id
    private String cashId;

    //现金券使用金额
    private BigDecimal couponUseFee;
	
}

