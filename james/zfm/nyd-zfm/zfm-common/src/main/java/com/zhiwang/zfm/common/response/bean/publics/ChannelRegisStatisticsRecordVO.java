package com.zhiwang.zfm.common.response.bean.publics;

import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class ChannelRegisStatisticsRecordVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8521830085033657234L;
	@ApiModelProperty(value = "主键id")
	private String id; // 标识
	
	// 渠道Id
	@ApiModelProperty(value = "渠道Id")
	private String channelId; // 渠道名称
	
	// 渠道名称
	@ApiModelProperty(value = "渠道名称")
	private String channelName; // 渠道名称

	// 注册数量
	@ApiModelProperty(value = "注册数量")
	private Integer regNumber;

	// 申请量
	@ApiModelProperty(value = "申请量")
	private Integer applyNumber;

	// 通过量
	@ApiModelProperty(value = "通过量")
	private Integer adoptNumber;

	// 放款量
	@ApiModelProperty(value = "放款量")
	private Integer loanNumber;
	
	// 申请转化率
	@ApiModelProperty(value = "申请转化率")
	private Double applyConversionRate;		//申请转化率
	
	// 批核率
	@ApiModelProperty(value = "批核率")
	private Double approvalVerifyRate;		//批核率
	
	// 成交转化率
	@ApiModelProperty(value = "成交转化率")
	private Double dealConversionRate;		//成交转化率
	
	// 放款金额
	@ApiModelProperty(value = "放款金额")
	private Double loanMoney;			//放款金额

	// 坏账量
	@ApiModelProperty(value = "坏账量")
	private Integer badDebtNumber;
	
	// 入催量
	@ApiModelProperty(value = "入催量")
	private Integer intoOverdueNumber;		//入催量
	
	// 入催率
	@ApiModelProperty(value = "入催率")
	private Double intoOverdueRate;		//入催率
	
	// 逾期量
	@ApiModelProperty(value = "逾期量")
	private Integer overdueNumber;			//逾期量
	
	// 逾期率
	@ApiModelProperty(value = "逾期率")
	private Double overdueRate;		//逾期率

	// 统计时间
	@ApiModelProperty(value = "统计时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")// HH:mm:ss
	private java.util.Date statisticalTime; // 创建时间
	
	@ApiModelProperty(value = "父级渠道id")
	private String parentChannelId;		//父级渠道id
	
	@ApiModelProperty(value = "父级渠道名称")
	private String parentChannelName;		//父级渠道名称

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(Integer regNumber) {
		this.regNumber = regNumber;
	}

	public Integer getApplyNumber() {
		return this.applyNumber;
	}

	public void setApplyNumber(Integer applyNumber) {
		this.applyNumber = applyNumber;
	}

	public Integer getAdoptNumber() {
		return adoptNumber;
	}

	public void setAdoptNumber(Integer adoptNumber) {
		this.adoptNumber = adoptNumber;
	}

	public Integer getLoanNumber() {
		return loanNumber;
	}

	public void setLoanNumber(Integer loanNumber) {
		this.loanNumber = loanNumber;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Double getLoanMoney() {
		return loanMoney;
	}

	public void setLoanMoney(Double loanMoney) {
		this.loanMoney = loanMoney;
	}

	public Integer getBadDebtNumber() {
		return badDebtNumber;
	}

	public void setBadDebtNumber(Integer badDebtNumber) {
		this.badDebtNumber = badDebtNumber;
	}

	public java.util.Date getStatisticalTime() {
		return statisticalTime;
	}

	public void setStatisticalTime(java.util.Date statisticalTime) {
		this.statisticalTime = statisticalTime;
	}

	public String getParentChannelId() {
		return parentChannelId;
	}

	public void setParentChannelId(String parentChannelId) {
		this.parentChannelId = parentChannelId;
	}

	public String getParentChannelName() {
		return parentChannelName;
	}

	public void setParentChannelName(String parentChannelName) {
		this.parentChannelName = parentChannelName;
	}

}
