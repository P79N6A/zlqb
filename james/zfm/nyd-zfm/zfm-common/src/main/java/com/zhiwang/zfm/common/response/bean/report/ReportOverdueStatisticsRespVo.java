package com.zhiwang.zfm.common.response.bean.report;

import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("逾期统计查询返回")
public class ReportOverdueStatisticsRespVo {

	@ApiModelProperty(value = "主键id")
	private String id;			//主键id
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private java.util.Date createTime;			//创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd" )
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	@ApiModelProperty(value = "统计日期")
	private java.util.Date statisticalTime;			//统计日期
	@ApiModelProperty(value = "归属平台编码")
	private String platCode;			//归属平台编码
	@ApiModelProperty(value = "归属平台名称")
	private String platName;			//归属平台名称
	@ApiModelProperty(value = "归属渠道编码",hidden=true)
	private String channelCode;			//归属渠道编码
	@ApiModelProperty(value = "归属渠道名称",hidden=true)
	private String channelName;			//归属渠道名称
	@ApiModelProperty(value = "应还账单数")
	private Integer receivableBillCount;			//应还账单数
	@ApiModelProperty(value = "应还订单数")
	private Integer receivableOrderCount;			//应还订单数
	@ApiModelProperty(value = "放款金额")
	private java.math.BigDecimal loanAmount;			//放款金额
	@ApiModelProperty(value = "入催数")
	private Integer settleBillCount;			//入催数
	@ApiModelProperty(value = "入催率")
	private String settleBillRate;			//入催率
	@ApiModelProperty(value = "逾期数")
	private Integer extensionBillCount;			//逾期数
	@ApiModelProperty(value = "逾期率")
	private String extensionBillRate;			//逾期率
	@ApiModelProperty(value = "总逾期待收金额")
	private java.math.BigDecimal allCollectedAmount;			//总逾期待收金额
	@ApiModelProperty(value = "逾期1天待收金额")
	private java.math.BigDecimal overdueOneDayCollectedAmount;			//逾期1天待收金额
	@ApiModelProperty(value = "逾期2天待收金额")
	private java.math.BigDecimal overdueTwoDayCollectedAmount;			//逾期2天待收金额
	@ApiModelProperty(value = "逾期3天待收金额")
	private java.math.BigDecimal overdueThreeDayCollectedAmount;			//逾期3天待收金额
	@ApiModelProperty(value = "逾期4天待收金额")
	private java.math.BigDecimal overdueFourDayCollectedAmount;			//逾期4天待收金额
	@ApiModelProperty(value = "逾期5天待收金额")
	private java.math.BigDecimal overdueFiveDayCollectedAmount;			//逾期5天待收金额
	@ApiModelProperty(value = "逾期6天待收金额")
	private java.math.BigDecimal overdueSixDayCollectedAmount;			//逾期6天待收金额
	@ApiModelProperty(value = "逾期7天待收金额")
	private java.math.BigDecimal overdueSevenDayCollectedAmount;			//逾期7天待收金额
	@ApiModelProperty(value = "逾期7天以上待收金额")
	private java.math.BigDecimal overdueThanSevenDayCollectedAmount;			//逾期7天以上待收金额
	@ApiModelProperty(value = "备注")
	private String remark;			//备注
	@ApiModelProperty(value = "逾期情况分布")
	private List<Map<String,Object>> overdueMapList;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public java.util.Date getStatisticalTime() {
		return statisticalTime;
	}
	public void setStatisticalTime(java.util.Date statisticalTime) {
		this.statisticalTime = statisticalTime;
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
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public Integer getReceivableBillCount() {
		return receivableBillCount;
	}
	public void setReceivableBillCount(Integer receivableBillCount) {
		this.receivableBillCount = receivableBillCount;
	}
	public Integer getReceivableOrderCount() {
		return receivableOrderCount;
	}
	public void setReceivableOrderCount(Integer receivableOrderCount) {
		this.receivableOrderCount = receivableOrderCount;
	}
	public java.math.BigDecimal getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(java.math.BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}
	public Integer getSettleBillCount() {
		return settleBillCount;
	}
	public void setSettleBillCount(Integer settleBillCount) {
		this.settleBillCount = settleBillCount;
	}
	public Integer getExtensionBillCount() {
		return extensionBillCount;
	}
	public void setExtensionBillCount(Integer extensionBillCount) {
		this.extensionBillCount = extensionBillCount;
	}
	public java.math.BigDecimal getAllCollectedAmount() {
		return allCollectedAmount;
	}
	public void setAllCollectedAmount(java.math.BigDecimal allCollectedAmount) {
		this.allCollectedAmount = allCollectedAmount;
	}
	public java.math.BigDecimal getOverdueOneDayCollectedAmount() {
		return overdueOneDayCollectedAmount;
	}
	public void setOverdueOneDayCollectedAmount(java.math.BigDecimal overdueOneDayCollectedAmount) {
		this.overdueOneDayCollectedAmount = overdueOneDayCollectedAmount;
	}
	public java.math.BigDecimal getOverdueTwoDayCollectedAmount() {
		return overdueTwoDayCollectedAmount;
	}
	public void setOverdueTwoDayCollectedAmount(java.math.BigDecimal overdueTwoDayCollectedAmount) {
		this.overdueTwoDayCollectedAmount = overdueTwoDayCollectedAmount;
	}
	public java.math.BigDecimal getOverdueThreeDayCollectedAmount() {
		return overdueThreeDayCollectedAmount;
	}
	public void setOverdueThreeDayCollectedAmount(java.math.BigDecimal overdueThreeDayCollectedAmount) {
		this.overdueThreeDayCollectedAmount = overdueThreeDayCollectedAmount;
	}
	public java.math.BigDecimal getOverdueFourDayCollectedAmount() {
		return overdueFourDayCollectedAmount;
	}
	public void setOverdueFourDayCollectedAmount(java.math.BigDecimal overdueFourDayCollectedAmount) {
		this.overdueFourDayCollectedAmount = overdueFourDayCollectedAmount;
	}
	public java.math.BigDecimal getOverdueFiveDayCollectedAmount() {
		return overdueFiveDayCollectedAmount;
	}
	public void setOverdueFiveDayCollectedAmount(java.math.BigDecimal overdueFiveDayCollectedAmount) {
		this.overdueFiveDayCollectedAmount = overdueFiveDayCollectedAmount;
	}
	public java.math.BigDecimal getOverdueSixDayCollectedAmount() {
		return overdueSixDayCollectedAmount;
	}
	public void setOverdueSixDayCollectedAmount(java.math.BigDecimal overdueSixDayCollectedAmount) {
		this.overdueSixDayCollectedAmount = overdueSixDayCollectedAmount;
	}
	public java.math.BigDecimal getOverdueSevenDayCollectedAmount() {
		return overdueSevenDayCollectedAmount;
	}
	public void setOverdueSevenDayCollectedAmount(java.math.BigDecimal overdueSevenDayCollectedAmount) {
		this.overdueSevenDayCollectedAmount = overdueSevenDayCollectedAmount;
	}
	public java.math.BigDecimal getOverdueThanSevenDayCollectedAmount() {
		return overdueThanSevenDayCollectedAmount;
	}
	public void setOverdueThanSevenDayCollectedAmount(java.math.BigDecimal overdueThanSevenDayCollectedAmount) {
		this.overdueThanSevenDayCollectedAmount = overdueThanSevenDayCollectedAmount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSettleBillRate() {
		return settleBillRate;
	}
	public void setSettleBillRate(String settleBillRate) {
		this.settleBillRate = settleBillRate;
	}
	public String getExtensionBillRate() {
		return extensionBillRate;
	}
	public void setExtensionBillRate(String extensionBillRate) {
		this.extensionBillRate = extensionBillRate;
	}
	public List<Map<String, Object>> getOverdueMapList() {
		return overdueMapList;
	}
	public void setOverdueMapList(List<Map<String, Object>> overdueMapList) {
		this.overdueMapList = overdueMapList;
	}

}
