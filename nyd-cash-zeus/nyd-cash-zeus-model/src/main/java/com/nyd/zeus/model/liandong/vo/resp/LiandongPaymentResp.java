package com.nyd.zeus.model.liandong.vo.resp;

import java.io.Serializable;

import lombok.Data;

@Data
public class LiandongPaymentResp implements Serializable {

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
	 * 查询订单返回ret_code=0000说明订单查询成功，订单状态以trade_state为准
	 * 注意：只有ret_code=0000以下枚举的交易状态（trade_state）值才有意义。 WAIT_BUYER_PAY 等待支付
	 * TRADE_SUCCESS 支付成功 TRADE_FAIL 支付失败 TRADE_CANCEL 订单已撤销 TRADE_CLOSE 订单过期关闭
	 */
	// 交易状态
	private String trade_state;

	// 金额
	private String amount;

	// 订单日期
	private String mer_date;

	// 商户订单号
	private String order_id;

}
