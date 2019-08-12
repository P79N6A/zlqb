package com.nyd.application.model.call;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 魔蝎--运营商原始数据--语音详情--通话详情明细
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Calls implements Serializable {

	private static final long serialVersionUID = 3408531280599396813L;

	private String details_id;//详情唯一标识
	private String time;//通话时间，格式：yyyy-MM-dd HH:mm:ss	
	private String peer_number;//	对方通话号码
	private String location;//通话地(自己的)
	private String location_type;//	通话地类型. e.g.省内漫游
	private String duration;//通话时长(单位:秒)
	private String dial_type;//呼叫类型. DIAL-主叫; DIALED-被叫
	private String fee;//通话费(单位:分)


}
