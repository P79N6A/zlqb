package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 17/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankInfo implements Serializable {
    //用户ID
    private String userId;
    //姓名
    private String accountName;
    //银行名称
    private String bankName;
    //卡类型
    private String accountType;
    //银行卡号
    private String bankAccount;
    //预留手机号
    private String reservedPhone;
    //短信验证码
    private String smsCode;

    //设备Id
    private String deviceId;
    //账号
    private String accountNumber;
	/**
	 * 付款或借款标识P支付 null或L为借款
	 */
	private String payOrLoanFlag;
    //平台编码
    private String appName;
    //用户身份证
	private String accountIc;
	//银行卡类型 1 畅捷 2 合利宝
    private Integer soure;
    //业务订单号
	private String protocolNo;
	//合利宝user_id
	private String hlbUserId;
	//商户单号
	private String merchantNumber;
	//渠道    changjie 常捷  xunlian 讯联
	private String channelCode;

	//绑卡协议号
	private String protocolId;

    // 业务协议号
    private String bizProtocolNo;
    // 支付协议号
    private String payProtocolNo;
}
