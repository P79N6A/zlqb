package com.zhiwang.zfm.common.response.bean.report;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("催收公司查询返回")
public class ReportCollectionCompanyStatisticsRespVo {

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
	@ApiModelProperty(value = "客户类型0:新客,1:老客")
	private Integer custType;			//客户类型0:新客,1:老客

	@ApiModelProperty(value = "客户类型")
	private String custTypeName;			//客户类型0:新客,1:老客

	@ApiModelProperty(value = "催收公司id")
	private String receiveSysUserId;			//催收公司id
	@ApiModelProperty(value = "催收公司name")
	private String receiveSysUserName;			//催收公司name
	@ApiModelProperty(value = "分配数")
	private Integer receivableBillCount;			//分配数
	@ApiModelProperty(value = "在催数")
	private Integer inCollectionCount;			//在催数
	@ApiModelProperty(value = "解决数")
	private Integer solveBillCount;			//解决数
	@ApiModelProperty(value = "入催数",hidden=true)
	private Integer settleBillCount;			//入催数
	@ApiModelProperty(value = "实际应还数(入催率基数)",hidden=true)
	private Integer actualReceivableBillCount;			//实际应还数(入催率基数)
	@ApiModelProperty(value = "解决率")
	private String solveBillRate;			//解决率
	@ApiModelProperty(value = "逾期1天客户数")
	private Integer overdueOneDayCustCount;			//逾期1天客户数
	@ApiModelProperty(value = "逾期1天解决数")
	private Integer overdueOneDaySolveCount;			//逾期1天解决数
	@ApiModelProperty(value = "逾期1天解决率")
	private String overdueOneDaySolveRate;			//逾期1天解决率
	@ApiModelProperty(value = "逾期1天订单数")
	private Integer overdueOneDayOrderCount;			//逾期1天订单数
	@ApiModelProperty(value = "逾期2天客户数")
	private Integer overdueTwoDayCustCount;			//逾期2天客户数
	@ApiModelProperty(value = "逾期2天解决数")
	private Integer overdueTwoDaySolveCount;			//逾期2天解决数
	@ApiModelProperty(value = "逾期2天解决率")
	private String overdueTwoDaySolveRate;			//逾期2天解决率
	@ApiModelProperty(value = "逾期2天订单数")
	private Integer overdueTwoDayOrderCount;			//逾期2天订单数
	@ApiModelProperty(value = "逾期3天客户数")
	private Integer overdueThreeDayCustCount;			//逾期3天客户数
	@ApiModelProperty(value = "逾期3天解决数")
	private Integer overdueThreeDaySolveCount;			//逾期3天解决数
	@ApiModelProperty(value = "逾期3天解决率")
	private String overdueThreeDaySolveRate;			//逾期3天解决率
	@ApiModelProperty(value = "逾期3天订单数")
	private Integer overdueThreeDayOrderCount;			//逾期3天订单数
	@ApiModelProperty(value = "逾期4天客户数")
	private Integer overdueFourDayCustCount;			//逾期4天客户数
	@ApiModelProperty(value = "逾期4天解决数")
	private Integer overdueFourDaySolveCount;			//逾期4天解决数
	@ApiModelProperty(value = "逾期4天解决率")
	private String overdueFourDaySolveRate;			//逾期4天解决率
	@ApiModelProperty(value = "逾期4天订单数")
	private Integer overdueFourDayOrderCount;			//逾期4天订单数
	@ApiModelProperty(value = "逾期5天客户数")
	private Integer overdueFiveDayCustCount;			//逾期5天客户数
	@ApiModelProperty(value = "逾期5天解决数")
	private Integer overdueFiveDaySolveCount;			//逾期5天解决数
	@ApiModelProperty(value = "逾期5天解决率")
	private String overdueFiveDaySolveRate;			//逾期5天解决率
	@ApiModelProperty(value = "逾期5天订单数")
	private Integer overdueFiveDayOrderCount;			//逾期5天订单数
	@ApiModelProperty(value = "逾期6天客户数")
	private Integer overdueSixDayCustCount;			//逾期6天客户数
	@ApiModelProperty(value = "逾期6天解决数")
	private Integer overdueSixDaySolveCount;			//逾期6天解决数
	@ApiModelProperty(value = "逾期6天解决率")
	private String overdueSixDaySolveRate;			//逾期6天解决率
	@ApiModelProperty(value = "逾期6天订单数")
	private Integer overdueSixDayOrderCount;			//逾期6天订单数
	@ApiModelProperty(value = "逾期7天客户数")
	private Integer overdueSevenDayCustCount;			//逾期7天客户数
	@ApiModelProperty(value = "逾期7天解决数")
	private Integer overdueSevenDaySolveCount;			//逾期7天解决数
	@ApiModelProperty(value = "逾期7天解决率")
	private String overdueSevenDaySolveRate;			//逾期7天解决率
	@ApiModelProperty(value = "逾期7天订单数")
	private Integer overdueSevenDayOrderCount;			//逾期7天订单数
	@ApiModelProperty(value = "逾期>7天客户数")
	private Integer overdueThanSevenDayCustCount;			//逾期>7天客户数
	@ApiModelProperty(value = "逾期>7天解决数")
	private Integer overdueThanSevenDaySolveCount;			//逾期>7天解决数
	@ApiModelProperty(value = "逾期>7天解决率")
	private String overdueThanSevenDaySolveRate;			//逾期>7天解决率
	@ApiModelProperty(value = "逾期>7天订单数")
	private Integer overdueThanSevenDayOrderCount;			//逾期>7天订单数
	@ApiModelProperty(value = "备注")
	private String remark;			//备注
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
	public Integer getCustType() {
		return custType;
	}
	public void setCustType(Integer custType) {
		this.custType = custType;
	}
	public String getCustTypeName() {
		return custTypeName;
	}
	public void setCustTypeName(String custTypeName) {
		this.custTypeName = custTypeName;
	}
	public String getReceiveSysUserId() {
		return receiveSysUserId;
	}
	public void setReceiveSysUserId(String receiveSysUserId) {
		this.receiveSysUserId = receiveSysUserId;
	}
	public String getReceiveSysUserName() {
		return receiveSysUserName;
	}
	public void setReceiveSysUserName(String receiveSysUserName) {
		this.receiveSysUserName = receiveSysUserName;
	}
	public Integer getReceivableBillCount() {
		return receivableBillCount;
	}
	public void setReceivableBillCount(Integer receivableBillCount) {
		this.receivableBillCount = receivableBillCount;
	}
	public Integer getInCollectionCount() {
		return inCollectionCount;
	}
	public void setInCollectionCount(Integer inCollectionCount) {
		this.inCollectionCount = inCollectionCount;
	}
	public Integer getSolveBillCount() {
		return solveBillCount;
	}
	public void setSolveBillCount(Integer solveBillCount) {
		this.solveBillCount = solveBillCount;
	}
	public Integer getSettleBillCount() {
		return settleBillCount;
	}
	public void setSettleBillCount(Integer settleBillCount) {
		this.settleBillCount = settleBillCount;
	}
	public Integer getActualReceivableBillCount() {
		return actualReceivableBillCount;
	}
	public void setActualReceivableBillCount(Integer actualReceivableBillCount) {
		this.actualReceivableBillCount = actualReceivableBillCount;
	}
	public String getSolveBillRate() {
		return solveBillRate;
	}
	public void setSolveBillRate(String solveBillRate) {
		this.solveBillRate = solveBillRate;
	}
	public Integer getOverdueOneDayCustCount() {
		return overdueOneDayCustCount;
	}
	public void setOverdueOneDayCustCount(Integer overdueOneDayCustCount) {
		this.overdueOneDayCustCount = overdueOneDayCustCount;
	}
	public Integer getOverdueOneDaySolveCount() {
		return overdueOneDaySolveCount;
	}
	public void setOverdueOneDaySolveCount(Integer overdueOneDaySolveCount) {
		this.overdueOneDaySolveCount = overdueOneDaySolveCount;
	}
	public String getOverdueOneDaySolveRate() {
		return overdueOneDaySolveRate;
	}
	public void setOverdueOneDaySolveRate(String overdueOneDaySolveRate) {
		this.overdueOneDaySolveRate = overdueOneDaySolveRate;
	}
	public Integer getOverdueOneDayOrderCount() {
		return overdueOneDayOrderCount;
	}
	public void setOverdueOneDayOrderCount(Integer overdueOneDayOrderCount) {
		this.overdueOneDayOrderCount = overdueOneDayOrderCount;
	}
	public Integer getOverdueTwoDayCustCount() {
		return overdueTwoDayCustCount;
	}
	public void setOverdueTwoDayCustCount(Integer overdueTwoDayCustCount) {
		this.overdueTwoDayCustCount = overdueTwoDayCustCount;
	}
	public Integer getOverdueTwoDaySolveCount() {
		return overdueTwoDaySolveCount;
	}
	public void setOverdueTwoDaySolveCount(Integer overdueTwoDaySolveCount) {
		this.overdueTwoDaySolveCount = overdueTwoDaySolveCount;
	}
	public String getOverdueTwoDaySolveRate() {
		return overdueTwoDaySolveRate;
	}
	public void setOverdueTwoDaySolveRate(String overdueTwoDaySolveRate) {
		this.overdueTwoDaySolveRate = overdueTwoDaySolveRate;
	}
	public Integer getOverdueTwoDayOrderCount() {
		return overdueTwoDayOrderCount;
	}
	public void setOverdueTwoDayOrderCount(Integer overdueTwoDayOrderCount) {
		this.overdueTwoDayOrderCount = overdueTwoDayOrderCount;
	}
	public Integer getOverdueThreeDayCustCount() {
		return overdueThreeDayCustCount;
	}
	public void setOverdueThreeDayCustCount(Integer overdueThreeDayCustCount) {
		this.overdueThreeDayCustCount = overdueThreeDayCustCount;
	}
	public Integer getOverdueThreeDaySolveCount() {
		return overdueThreeDaySolveCount;
	}
	public void setOverdueThreeDaySolveCount(Integer overdueThreeDaySolveCount) {
		this.overdueThreeDaySolveCount = overdueThreeDaySolveCount;
	}
	public String getOverdueThreeDaySolveRate() {
		return overdueThreeDaySolveRate;
	}
	public void setOverdueThreeDaySolveRate(String overdueThreeDaySolveRate) {
		this.overdueThreeDaySolveRate = overdueThreeDaySolveRate;
	}
	public Integer getOverdueThreeDayOrderCount() {
		return overdueThreeDayOrderCount;
	}
	public void setOverdueThreeDayOrderCount(Integer overdueThreeDayOrderCount) {
		this.overdueThreeDayOrderCount = overdueThreeDayOrderCount;
	}
	public Integer getOverdueFourDayCustCount() {
		return overdueFourDayCustCount;
	}
	public void setOverdueFourDayCustCount(Integer overdueFourDayCustCount) {
		this.overdueFourDayCustCount = overdueFourDayCustCount;
	}
	public Integer getOverdueFourDaySolveCount() {
		return overdueFourDaySolveCount;
	}
	public void setOverdueFourDaySolveCount(Integer overdueFourDaySolveCount) {
		this.overdueFourDaySolveCount = overdueFourDaySolveCount;
	}
	public String getOverdueFourDaySolveRate() {
		return overdueFourDaySolveRate;
	}
	public void setOverdueFourDaySolveRate(String overdueFourDaySolveRate) {
		this.overdueFourDaySolveRate = overdueFourDaySolveRate;
	}
	public Integer getOverdueFourDayOrderCount() {
		return overdueFourDayOrderCount;
	}
	public void setOverdueFourDayOrderCount(Integer overdueFourDayOrderCount) {
		this.overdueFourDayOrderCount = overdueFourDayOrderCount;
	}
	public Integer getOverdueFiveDayCustCount() {
		return overdueFiveDayCustCount;
	}
	public void setOverdueFiveDayCustCount(Integer overdueFiveDayCustCount) {
		this.overdueFiveDayCustCount = overdueFiveDayCustCount;
	}
	public Integer getOverdueFiveDaySolveCount() {
		return overdueFiveDaySolveCount;
	}
	public void setOverdueFiveDaySolveCount(Integer overdueFiveDaySolveCount) {
		this.overdueFiveDaySolveCount = overdueFiveDaySolveCount;
	}
	public String getOverdueFiveDaySolveRate() {
		return overdueFiveDaySolveRate;
	}
	public void setOverdueFiveDaySolveRate(String overdueFiveDaySolveRate) {
		this.overdueFiveDaySolveRate = overdueFiveDaySolveRate;
	}
	public Integer getOverdueFiveDayOrderCount() {
		return overdueFiveDayOrderCount;
	}
	public void setOverdueFiveDayOrderCount(Integer overdueFiveDayOrderCount) {
		this.overdueFiveDayOrderCount = overdueFiveDayOrderCount;
	}
	public Integer getOverdueSixDayCustCount() {
		return overdueSixDayCustCount;
	}
	public void setOverdueSixDayCustCount(Integer overdueSixDayCustCount) {
		this.overdueSixDayCustCount = overdueSixDayCustCount;
	}
	public Integer getOverdueSixDaySolveCount() {
		return overdueSixDaySolveCount;
	}
	public void setOverdueSixDaySolveCount(Integer overdueSixDaySolveCount) {
		this.overdueSixDaySolveCount = overdueSixDaySolveCount;
	}
	public String getOverdueSixDaySolveRate() {
		return overdueSixDaySolveRate;
	}
	public void setOverdueSixDaySolveRate(String overdueSixDaySolveRate) {
		this.overdueSixDaySolveRate = overdueSixDaySolveRate;
	}
	public Integer getOverdueSixDayOrderCount() {
		return overdueSixDayOrderCount;
	}
	public void setOverdueSixDayOrderCount(Integer overdueSixDayOrderCount) {
		this.overdueSixDayOrderCount = overdueSixDayOrderCount;
	}
	public Integer getOverdueSevenDayCustCount() {
		return overdueSevenDayCustCount;
	}
	public void setOverdueSevenDayCustCount(Integer overdueSevenDayCustCount) {
		this.overdueSevenDayCustCount = overdueSevenDayCustCount;
	}
	public Integer getOverdueSevenDaySolveCount() {
		return overdueSevenDaySolveCount;
	}
	public void setOverdueSevenDaySolveCount(Integer overdueSevenDaySolveCount) {
		this.overdueSevenDaySolveCount = overdueSevenDaySolveCount;
	}
	public String getOverdueSevenDaySolveRate() {
		return overdueSevenDaySolveRate;
	}
	public void setOverdueSevenDaySolveRate(String overdueSevenDaySolveRate) {
		this.overdueSevenDaySolveRate = overdueSevenDaySolveRate;
	}
	public Integer getOverdueSevenDayOrderCount() {
		return overdueSevenDayOrderCount;
	}
	public void setOverdueSevenDayOrderCount(Integer overdueSevenDayOrderCount) {
		this.overdueSevenDayOrderCount = overdueSevenDayOrderCount;
	}
	public Integer getOverdueThanSevenDayCustCount() {
		return overdueThanSevenDayCustCount;
	}
	public void setOverdueThanSevenDayCustCount(Integer overdueThanSevenDayCustCount) {
		this.overdueThanSevenDayCustCount = overdueThanSevenDayCustCount;
	}
	public Integer getOverdueThanSevenDaySolveCount() {
		return overdueThanSevenDaySolveCount;
	}
	public void setOverdueThanSevenDaySolveCount(Integer overdueThanSevenDaySolveCount) {
		this.overdueThanSevenDaySolveCount = overdueThanSevenDaySolveCount;
	}
	public String getOverdueThanSevenDaySolveRate() {
		return overdueThanSevenDaySolveRate;
	}
	public void setOverdueThanSevenDaySolveRate(String overdueThanSevenDaySolveRate) {
		this.overdueThanSevenDaySolveRate = overdueThanSevenDaySolveRate;
	}
	public Integer getOverdueThanSevenDayOrderCount() {
		return overdueThanSevenDayOrderCount;
	}
	public void setOverdueThanSevenDayOrderCount(Integer overdueThanSevenDayOrderCount) {
		this.overdueThanSevenDayOrderCount = overdueThanSevenDayOrderCount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
