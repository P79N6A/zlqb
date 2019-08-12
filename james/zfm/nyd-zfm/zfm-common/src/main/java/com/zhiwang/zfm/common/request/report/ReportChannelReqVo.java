package com.zhiwang.zfm.common.request.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.text.ParseException;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhiwang.zfm.common.util.DateUtils;

/**
 *auther wanghu
 *date 2019年3月4日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("渠道统计")
public class ReportChannelReqVo extends ReportCommonReqVO{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "接入开始日期(yyyy-MM-dd)")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date channelBegin;
	@ApiModelProperty(value = "接入结束时间(yyyy-MM-dd)")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date channelEnd;
	
	@ApiModelProperty(value = "注册时间(yyyy-MM-dd)")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date runDate;
	
	@ApiModelProperty(value = "平台编码")
	private String platCode;			//平台编码
	
	@ApiModelProperty(value = "渠道id")
	private String channelId;			//渠道id
	
	@ApiModelProperty(value = "渠道名称")
	private String channelName;			//渠道名称
	
	@ApiModelProperty(value = "连接id")
	private String linkId;			//连接id
	
	@ApiModelProperty(value = "链接名称")
	private String linkName;

	public Date getChannelBegin() {
		return channelBegin;
	}

	public void setChannelBegin(Date channelBegin) throws ParseException {
		if (null != channelBegin)
			this.channelBegin = DateUtils.formatDate(DateUtils.format(channelBegin, DateUtils.STYLE_2) + " 00:00:00",
					DateUtils.DATE_TIME_PATTERN);
	}

	public Date getChannelEnd() {
		return channelEnd;
	}

	public void setChannelEnd(Date channelEnd) throws ParseException {
		if (null != channelEnd)
			this.channelEnd = DateUtils.formatDate(DateUtils.format(channelEnd, DateUtils.STYLE_2) + " 23:59:59",
					DateUtils.DATE_TIME_PATTERN);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

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
	
	
	

}
