package com.zhiwang.zfm.common.request.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *auther wanghu
 *date 2019年3月7日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("生命周期请求参数")
public class ReportChannelLifeCycleReqVo {
	
	@ApiModelProperty(value = "注册时间(yyyy-MM-dd)")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date runDate;
	
	@ApiModelProperty(value = "平台编码")
	private String platCode;			//平台编码
	
	@ApiModelProperty(value = "渠道id")
	private String channelId;			//渠道id
	
	@ApiModelProperty(value = "类型(XD,FD,ZQ)")
	private String type;

	public Date getRunDate() {
		return runDate;
	}

	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}

	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
