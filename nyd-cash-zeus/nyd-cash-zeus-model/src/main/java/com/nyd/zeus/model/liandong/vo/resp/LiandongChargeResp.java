package com.nyd.zeus.model.liandong.vo.resp;

import java.io.Serializable;

import lombok.Data;

@Data
public class LiandongChargeResp implements Serializable {

	private static final long serialVersionUID = 1L;

	// 返回码
	/**
	 * 0001 处理中
	 * 0000 请求成功
	 */
	private String ret_code;

	// 返回描述
	private String ret_msg;
	/**
	 * 1-支付中
		3-失败
		4-成功 
	 */
	// 交易状态
	private String trade_state;

	// 金额
	private String amount;

	// 订单日期
	private String mer_date;

	// 商户订单号
	private String order_id;
	
	// 手续费
	private String fee;

}
