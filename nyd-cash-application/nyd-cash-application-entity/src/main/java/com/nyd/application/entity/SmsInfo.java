package com.nyd.application.entity;

import java.util.Date;

import javax.persistence.Table;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * 通话记录表
 * @author admin
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_sms_info")
public class SmsInfo {
	
	
	    @Id
	    private Long id;
	    //添加时间
	    private Date createTime;
	    //更新时间
	    private Date updateTime;
	    //手机号
	    private String phoneNo;
	    //
	    private String deviceId;	
	    //
	    private String phoneOs;	 
	    //
	    private String versionName;
	    //

	    private String content;
   
	    private String devicePhoneNo;
	    //
	    private String time;
	    private String type;
	    private String callNo;

	    private String userId;
	    //客户id
	    
	  

}
