package com.nyd.application.model.call;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Table;

import org.springframework.data.annotation.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(callSuper = true)
@Data
@Table(name = "t_carry_calls")
@NoArgsConstructor
@AllArgsConstructor
public class CarryCalls implements Serializable{

	/**
	 * 语音详情
	 */
	private static final long serialVersionUID = 6480819659898366031L;
	@Id
	private Long id;
	private Long carryId;
	private String time;			//通话时间，格式：yyyy-MM-dd HH:mm:ss
	private int duration;			//通话时长(单位:秒)
	private String dialType;		//呼叫类型. DIAL-主叫; DIALED-被叫
	private String peerNumber;		//对方通话号码
	private String location;		//通话地(自己的)
	private String	locationCity;	//对方归属地
	private String locationType;	//通话地类型. e.g.省内漫游
	private int fee;			//通话费(单位:分)
	private String status;
	private Date createTime;
	private Date updateTime;
	
	private String queryValide;


	
}
