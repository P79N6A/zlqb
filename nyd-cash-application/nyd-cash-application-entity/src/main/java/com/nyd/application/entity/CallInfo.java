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
@Table(name = "t_call_info")
public class CallInfo {
	
	
	    @Id
	    private Long id;
	    //添加时间
	    private Date createTime;
	    //更新时间
	    private Date updateTime;
	    //客户id
	    private String custInfoId;
	    //注册手机号
	    private String phoneNo;
	    //姓名
	    private String name;
	    //通话-电话号码
	    private String callNo;
	    //通话创建时间 HH:mm:ss
	    private String hour;
	    //
	    private String versionName;
	    //
	    private String phoneOs;	    
	    //通话创建时间 yyyy-MM-dd HH:mm:ss
	    private String callTime;	    
	    //
	    private String deviceId;	   
	    //通话时长 单位s
	    private String duration;
	    
	    //通话类型（2 呼出，1 呼入,3 未接）
	    private String type;
	    private String appName;
	    
	  

}
