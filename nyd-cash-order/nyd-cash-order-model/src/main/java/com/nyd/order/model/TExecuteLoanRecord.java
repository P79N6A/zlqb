package com.nyd.order.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class TExecuteLoanRecord implements Serializable{
		private String id;			//	private String type;			//200007-0001 百联 200007-0010 富友协议支付	private String orderId;			//订单id	private String outTradeNo;			//0处理中 1 处理成功 2 处理失败	private Integer status;			//0处理中 1 处理成功 2 处理失败	private java.util.Date createTime;			//	private java.util.Date updateTime;			//	private String helibaoUserid;			//合利宝用户编号	private String helibaoOrderid;			//合利宝订单id
	private String userNo;	
}
