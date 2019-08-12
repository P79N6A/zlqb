package com.zhiwang.zfm.common.response.bean.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *auther wanghu
 *date 2019年3月4日
 */
@ApiModel("渠道统计返回")
public class ReportChannelLinkStatisticsRespVo {
	
	@ApiModelProperty(value = "标识")
	private String id;			//
	
	@ApiModelProperty(value = "统计日期")
	private String runDate;			//统计日期
	
	@ApiModelProperty(value = "平台编号")
	private String platCode;			//平台编号
	
	@ApiModelProperty(value = "平台名称")
	private String platName;			//平台名称
	
	@ApiModelProperty(value = "渠道id")
	private String channelId;			//渠道id
	
	@ApiModelProperty(value = "渠道名称")
	private String channelName;			//渠道名称
	
	@ApiModelProperty(value = "渠道进入时间")
	private String channelCreateTime;			//渠道进入时间
	
	@ApiModelProperty(value = "连接id")
	private String linkId;			//连接id
	
	@ApiModelProperty(value = "链接名称")
	private String linkName;			//链接名称
	
	@ApiModelProperty(value = "链接进入时间")
	private String linkCreateTime;			//链接进入时间
	
	@ApiModelProperty(value = "注册数目")
	private Integer registerCount;			//注册数目
	
	@ApiModelProperty(value = "申请数")
	private Integer applyCount;			//申请数
	
	@ApiModelProperty(value = "通过数目")
	private Integer passCount;			//通过数目
	
	@ApiModelProperty(value = "放款数")
	private Integer loanCount;			//放款数
	
	@ApiModelProperty(value = "入催数")
	private Integer urgeCount;			//入催数
	
	@ApiModelProperty(value = "逾期数")
	private Integer overCount;			//逾期数
	
	@ApiModelProperty(value = "累计还款数")
	private Integer yesterdayCount;			//累计还款数
	
	@ApiModelProperty(value = "当天还款数")
	private Integer todayCount;			//当天还款数
	
	@ApiModelProperty(value = "申请转化率")
	private String applyRate;
	
	@ApiModelProperty(value = "批核率")
	private String passRate;
	
	@ApiModelProperty(value = "成交转化率")
	private String loanRate;
	
	@ApiModelProperty(value = "入催率")
	private String urgeRate;
	
	@ApiModelProperty(value = "逾期率")
	private String overRate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

	public String getPlatName() {
		return platName;
	}

	public void setPlatName(String platName) {
		this.platName = platName;
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

	public String getChannelCreateTime() {
		return channelCreateTime;
	}

	public void setChannelCreateTime(String channelCreateTime) {
		this.channelCreateTime = channelCreateTime;
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

	public String getLinkCreateTime() {
		return linkCreateTime;
	}

	public void setLinkCreateTime(String linkCreateTime) {
		this.linkCreateTime = linkCreateTime;
	}

	public Integer getRegisterCount() {
		return registerCount;
	}

	public void setRegisterCount(Integer registerCount) {
		this.registerCount = registerCount;
	}

	public Integer getApplyCount() {
		return applyCount;
	}

	public void setApplyCount(Integer applyCount) {
		this.applyCount = applyCount;
	}

	public Integer getPassCount() {
		return passCount;
	}

	public void setPassCount(Integer passCount) {
		this.passCount = passCount;
	}

	public Integer getLoanCount() {
		return loanCount;
	}

	public void setLoanCount(Integer loanCount) {
		this.loanCount = loanCount;
	}

	public Integer getUrgeCount() {
		return urgeCount;
	}

	public void setUrgeCount(Integer urgeCount) {
		this.urgeCount = urgeCount;
	}

	public Integer getOverCount() {
		return overCount;
	}

	public void setOverCount(Integer overCount) {
		this.overCount = overCount;
	}

	public Integer getYesterdayCount() {
		return yesterdayCount;
	}

	public void setYesterdayCount(Integer yesterdayCount) {
		this.yesterdayCount = yesterdayCount;
	}

	public Integer getTodayCount() {
		return todayCount;
	}

	public void setTodayCount(Integer todayCount) {
		this.todayCount = todayCount;
	}

	public String getApplyRate() {
		return applyRate;
	}

	public void setApplyRate(String applyRate) {
		this.applyRate = applyRate;
	}

	public String getPassRate() {
		return passRate;
	}

	public void setPassRate(String passRate) {
		this.passRate = passRate;
	}

	public String getLoanRate() {
		return loanRate;
	}

	public void setLoanRate(String loanRate) {
		this.loanRate = loanRate;
	}

	public String getUrgeRate() {
		return urgeRate;
	}

	public void setUrgeRate(String urgeRate) {
		this.urgeRate = urgeRate;
	}

	public String getOverRate() {
		return overRate;
	}

	public void setOverRate(String overRate) {
		this.overRate = overRate;
	}
	
	

}
