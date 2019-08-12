/**
 * Project Name:nyd-cash-pay-model
 * File Name:AuthInfoQryResponse.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月7日下午3:36:58
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * ClassName:AuthInfoQryResponse <br/>
 * Date:     2018年9月7日 下午3:36:58 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class AuthInfoQryResponse extends ChangJieQuickPayPublicResponse implements Serializable {
	
	private static final long serialVersionUID = -3526547207994174711L;
	
	/**商户网站唯一订单号*/
	@JsonProperty("TrxId")
	private String trxId;
	
	/**用户标识*/
	@JsonProperty("MerUserId")
	private String merUserId;
	
	/**卡号前6位*/
	@JsonProperty("CardBegin")
	private String cardBegin;
	
	/**卡号后4位*/
	@JsonProperty("CardEnd")
	private String cardEnd;
	
	/**卡所属银行名称*/
	@JsonProperty("BankName")
	private String bankName;
	
	/**卡类型*/
	@JsonProperty("BkAcctTp")
	private String bkAcctTp;
	
	/**持卡人手机号*/
	@JsonProperty("MobNo")
	private String mobNo;
	
	/**卡状态*/
	@JsonProperty("BindingStatus")
	private String bindingStatus;
	
	/**最后更新时间*/
	@JsonProperty("ModifyTime")
	private String modifyTime;
	
	/**业务状态*/
	@JsonProperty("Status")
	private String status;
	
	/**业务返回码*/
	@JsonProperty("RetCode")
	private String retCode;
	
	/**返回描述*/
	@JsonProperty("RetMsg")
	private String retMsg;
	
	/**应用返回码*/
	@JsonProperty("AppRetcode")
	private String appRetcode;
	
	/**应用返回描述*/
	@JsonProperty("AppRetMsg")
	private String appRetMsg;
	
	/**扩展字段*/
	@JsonProperty("Extension")
	private String extension;

}

