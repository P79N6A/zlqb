package com.nyd.user.model.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserBankInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String accountName;
	private String bankAccount;
	private String accountType;
	private String reservedPhone;
	private String bankName;
	private String defaultFlag;
	private Integer deleteFlag;
	private String createTime;
	private String updateTime;
	private String accountIc;
	private Integer soure;
	private String protocolNo;
	private String merchantNumber;
	private String hlbUserId;
	private String bankCode;
	private String updateBy;
	
}
