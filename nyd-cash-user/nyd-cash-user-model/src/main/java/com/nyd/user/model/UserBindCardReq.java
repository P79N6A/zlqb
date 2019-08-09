package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 绑卡信息
 * 
 * @author zhangdk
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserBindCardReq {
	private String userId;// 用户标识
	private String cardNo;// 银行卡号
	private String idcardNo;// 身份证号
	private String username;
	private String bankCode;
	private String bankName;
	private String phone;
	private String agreementTag;//是否签署代扣协议

	private String channelCode;

	/**
	 * 代扣渠道:baofoo
	 */
	private String payChannelCode;
	private String payChannelCodeBx;
	/**
	 * 付款或借款绑卡标识P支付绑卡 null或L为借款绑卡
	 */
	private String payOrLoanFlag;
    private String appName;
}

