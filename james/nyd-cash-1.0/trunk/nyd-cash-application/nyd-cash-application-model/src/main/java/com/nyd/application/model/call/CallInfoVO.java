package com.nyd.application.model.call;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 客户通讯录实体
 */
@Data
public class CallInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	// 添加时间
	private Date createTime;
	// 更新时间
	private Date updateTime;
	// 客户id
	private String custInfoId;
	// 手机号
	private String phoneNo;
	//
	private String name;
	//
	private String callNo;
	//
	private String hour;
	//
	private String versionName;
	//
	private String phoneOs;
	//
	private String callTime;
	//
	private String deviceId;
	//
	private String duration;

	private String type;
	private String calltime;
	private String appName;

}
