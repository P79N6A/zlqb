/**
 * Project Name:nyd-cash-pay-model
 * File Name:OrderChannelInfo.java
 * Package Name:com.creativearts.nyd.pay.model.business.changjie
 * Date:2018年9月12日上午10:47:32
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.business.changjie;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * ClassName:OrderChannelInfo <br/>
 * Date:     2018年9月12日 上午10:47:32 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class ThirdPartyPaymentChannelInfo implements Serializable {

	private static final long serialVersionUID = -7660644482471859041L;

	/**主键*/
	private Integer id;
	
	/**用户编号*/
	private String userId;
	
	/**订单号*/
	private String orderId;
	
	/**第三方返回流水号*/
	private String transId;
	
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
}

