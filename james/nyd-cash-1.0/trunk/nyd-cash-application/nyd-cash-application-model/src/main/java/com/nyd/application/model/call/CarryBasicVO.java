package com.nyd.application.model.call;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 查询客户第三方通话详单
 */
@Data
public class CarryBasicVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;	//姓名
	private String name;	//姓名
	private String mobile;  
	private String userId;  
	//private String address;  
	//private Date lastModifyTime;
	private String time;			//通话时间，格式：yyyy-MM-dd HH:mm:ss
	private int duration;			//通话时长(单位:秒)
	private String dialType;		//呼叫类型. DIAL-主叫; DIALED-被叫
	private String peerNumber;		//对方通话号码
	private String location;		//通话地(自己的)
	private int fee;			//通话费(单位:分)
	
}
