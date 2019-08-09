package com.nyd.msg.model;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class SmsReplyEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id; 
	
	private String phone;
	
	private String content;
	
	private String createTime;
	
	private String deliverTime;

}
