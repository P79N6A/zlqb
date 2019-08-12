package com.nyd.application.model.call;

import java.io.Serializable;

import com.nyd.application.model.common.PageCommon;

import lombok.Data;

/**
 * 查询客户信息VO
 */
@Data
public class CustInfoQuery extends PageCommon implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ordrId;
	private String custInfoId;
	private String mobile;


	/**
	 * 通话记录-手机号码
	 */
	private String callNo;


	/**
	 * 通话记录-姓名
	 */
	private String name;

}
