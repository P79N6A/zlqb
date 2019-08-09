package com.nyd.application.model.call;

import java.io.Serializable;

import lombok.Data;

/**
 * 客户通讯录实体
 */
@Data
public class CallRecordVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String orderId;

	private String custInfoId;

	private String mobile;

	private String name;

	private String callStaus;

	private String callLength;

	private String callTime;
	

	
}
