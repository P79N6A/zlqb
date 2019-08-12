package com.zhiwang.zfm.common.response.bean.report;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("还款统计查询返回")
public class ReportReimbursementStatisticsRespVo {

	@ApiModelProperty(value = "主键id")
	private String id;			//主键id
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private java.util.Date createTime;			//创建时间
	@ApiModelProperty(value = "统计日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd" )
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private java.util.Date statisticalTime;			//统计日期
	@ApiModelProperty(value = "归属平台编码")
	private String platCode;			//归属平台编码
	@ApiModelProperty(value = "归属平台名称")
	private String platName;			//归属平台名称
	@ApiModelProperty(value = "归属渠道编码",hidden=true)
	private String channelCode;			//归属渠道编码
	@ApiModelProperty(value = "归属渠道名称",hidden=true)
	private String channelName;			//归属渠道名称
	@ApiModelProperty(value = "应还账单金额")
	private java.math.BigDecimal receivableBillAmount;			//应还账单金额
	@ApiModelProperty(value = "应还账单数")
	private Integer receivableBillCount;			//应还账单数
	@ApiModelProperty(value = "实还金额")
	private java.math.BigDecimal actualRepaymentAmount;			//实还金额
	@ApiModelProperty(value = "结清账单数")
	private Integer settleBillCount;			//结清账单数
	@ApiModelProperty(value = "展期账单数")
	private Integer extensionBillCount;			//展期账单数
	@ApiModelProperty(value = "待收金额")
	private java.math.BigDecimal collectedAmount;			//待收金额
	@ApiModelProperty(value = "待收账单数")
	private Integer collectedBillCount;			//待收账单数
	@ApiModelProperty(value = "逾期数")
	private Integer overdueCount;			//逾期数
	@ApiModelProperty(value = "逾期率")
	private String overdueRate;		//逾期率	
	@ApiModelProperty(value = "提前还款金额")
	private java.math.BigDecimal earlyRepaymentAmount;			//提前还款金额
	@ApiModelProperty(value = "提前还款数")
	private Integer earlyRepaymentCount;			//提前还款数
	@ApiModelProperty(value = "正常还款金额")
	private java.math.BigDecimal normalRepaymentAmount;			//正常还款金额
	@ApiModelProperty(value = "正常还款数")
	private Integer normalRepaymentCount;			//正常还款数
	@ApiModelProperty(value = "当天还款数")
	private Integer currentDayRepaymentCount;			//当天还款数
	@ApiModelProperty(value = "逾期还款金额")
	private java.math.BigDecimal overdueRepaymentAmount;			//逾期还款金额
	@ApiModelProperty(value = "逾期还款数")
	private Integer overdueRepaymentCount;			//逾期还款数
	@ApiModelProperty(value = "提前还款率")
	private String earlyRepaymentRate;		//提前还款率
	@ApiModelProperty(value = "正常还款率")	
	private String normalRepaymentRate;		//正常还款率
	@ApiModelProperty(value = "备注")
	private String remark;			//备注
	public String getId() {
	    return this.id;
	}
	public void setId(String id) {
	    this.id=id;
	}
	public java.util.Date getCreateTime() {
	    return this.createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
	    this.createTime=createTime;
	}
	public java.util.Date getStatisticalTime() {
	    return this.statisticalTime;
	}
	public void setStatisticalTime(java.util.Date statisticalTime) {
	    this.statisticalTime=statisticalTime;
	}
	public String getPlatCode() {
	    return this.platCode;
	}
	public void setPlatCode(String platCode) {
	    this.platCode=platCode;
	}
	public String getPlatName() {
	    return this.platName;
	}
	public void setPlatName(String platName) {
	    this.platName=platName;
	}
	public String getChannelCode() {
	    return this.channelCode;
	}
	public void setChannelCode(String channelCode) {
	    this.channelCode=channelCode;
	}
	public String getChannelName() {
	    return this.channelName;
	}
	public void setChannelName(String channelName) {
	    this.channelName=channelName;
	}
	public java.math.BigDecimal getReceivableBillAmount() {
	    return this.receivableBillAmount;
	}
	public void setReceivableBillAmount(java.math.BigDecimal receivableBillAmount) {
	    this.receivableBillAmount=receivableBillAmount;
	}
	public Integer getReceivableBillCount() {
	    return this.receivableBillCount;
	}
	public void setReceivableBillCount(Integer receivableBillCount) {
	    this.receivableBillCount=receivableBillCount;
	}
	public java.math.BigDecimal getActualRepaymentAmount() {
	    return this.actualRepaymentAmount;
	}
	public void setActualRepaymentAmount(java.math.BigDecimal actualRepaymentAmount) {
	    this.actualRepaymentAmount=actualRepaymentAmount;
	}
	public Integer getSettleBillCount() {
	    return this.settleBillCount;
	}
	public void setSettleBillCount(Integer settleBillCount) {
	    this.settleBillCount=settleBillCount;
	}
	public Integer getExtensionBillCount() {
	    return this.extensionBillCount;
	}
	public void setExtensionBillCount(Integer extensionBillCount) {
	    this.extensionBillCount=extensionBillCount;
	}
	public java.math.BigDecimal getCollectedAmount() {
	    return this.collectedAmount;
	}
	public void setCollectedAmount(java.math.BigDecimal collectedAmount) {
	    this.collectedAmount=collectedAmount;
	}
	public Integer getCollectedBillCount() {
	    return this.collectedBillCount;
	}
	public void setCollectedBillCount(Integer collectedBillCount) {
	    this.collectedBillCount=collectedBillCount;
	}
	public Integer getOverdueCount() {
	    return this.overdueCount;
	}
	public void setOverdueCount(Integer overdueCount) {
	    this.overdueCount=overdueCount;
	}
	public java.math.BigDecimal getEarlyRepaymentAmount() {
	    return this.earlyRepaymentAmount;
	}
	public void setEarlyRepaymentAmount(java.math.BigDecimal earlyRepaymentAmount) {
	    this.earlyRepaymentAmount=earlyRepaymentAmount;
	}
	public Integer getEarlyRepaymentCount() {
	    return this.earlyRepaymentCount;
	}
	public void setEarlyRepaymentCount(Integer earlyRepaymentCount) {
	    this.earlyRepaymentCount=earlyRepaymentCount;
	}
	public java.math.BigDecimal getNormalRepaymentAmount() {
	    return this.normalRepaymentAmount;
	}
	public void setNormalRepaymentAmount(java.math.BigDecimal normalRepaymentAmount) {
	    this.normalRepaymentAmount=normalRepaymentAmount;
	}
	public Integer getNormalRepaymentCount() {
	    return this.normalRepaymentCount;
	}
	public void setNormalRepaymentCount(Integer normalRepaymentCount) {
	    this.normalRepaymentCount=normalRepaymentCount;
	}
	public Integer getCurrentDayRepaymentCount() {
	    return this.currentDayRepaymentCount;
	}
	public void setCurrentDayRepaymentCount(Integer currentDayRepaymentCount) {
	    this.currentDayRepaymentCount=currentDayRepaymentCount;
	}
	public java.math.BigDecimal getOverdueRepaymentAmount() {
	    return this.overdueRepaymentAmount;
	}
	public void setOverdueRepaymentAmount(java.math.BigDecimal overdueRepaymentAmount) {
	    this.overdueRepaymentAmount=overdueRepaymentAmount;
	}
	public Integer getOverdueRepaymentCount() {
	    return this.overdueRepaymentCount;
	}
	public void setOverdueRepaymentCount(Integer overdueRepaymentCount) {
	    this.overdueRepaymentCount=overdueRepaymentCount;
	}
	public String getRemark() {
	    return this.remark;
	}
	public void setRemark(String remark) {
	    this.remark=remark;
	}
	public String getOverdueRate() {
		return overdueRate;
	}
	public void setOverdueRate(String overdueRate) {
		this.overdueRate = overdueRate;
	}
	public String getEarlyRepaymentRate() {
		return earlyRepaymentRate;
	}
	public void setEarlyRepaymentRate(String earlyRepaymentRate) {
		this.earlyRepaymentRate = earlyRepaymentRate;
	}
	public String getNormalRepaymentRate() {
		return normalRepaymentRate;
	}
	public void setNormalRepaymentRate(String normalRepaymentRate) {
		this.normalRepaymentRate = normalRepaymentRate;
	}

}
