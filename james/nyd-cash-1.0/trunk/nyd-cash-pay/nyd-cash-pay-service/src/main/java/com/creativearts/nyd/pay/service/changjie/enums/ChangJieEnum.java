/**
 * Project Name:nyd-cash-pay-service
 * File Name:ChangJieEnum.java
 * Package Name:com.creativearts.nyd.pay.service.changjie.enums
 * Date:2018年9月13日上午10:55:23
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.service.changjie.enums;
/**
 * ClassName:ChangJieEnum <br/>
 * Date:     2018年9月13日 上午10:55:23 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public enum ChangJieEnum {
	TRADE_SUCCESS("TRADE_SUCCESS","交易成功"),
	TRADE_FINISHED("TRADE_FINISHED","交易完成"),
	TRADE_CLOSED("TRADE_CLOSED","交易关闭"),
	ACCEPT_STATUS_FAIL("F","请求失败"),
	IS_VALID("是","卡号有效"),
	ORIGINAL_RET_CODE("000000","交易返回成功"),
	CARD_TYPE_DC("DC","借记卡"),//这里的01和DC都代表借记卡                      
	CARD_TYPE_01("01","借记卡"),//这里的01和DC都代表借记卡 			00 –银行贷记卡;01 –银行借记卡
	IDCARD_TYPE_01("01","身份证"),
	ORDER_NAME("支付第三方风控报告费用","订单名称");

    private String msg;
    private String code;

    ChangJieEnum(String msg,String code){
    	this.msg=msg;
    	this.code=code;
    }

	public String getMsg() {
		return msg;
	}

	public String getCode() {
		return code;
	}
    
}

