package com.zhiwang.zfm.common.request.cust;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhiwang.zfm.common.request.report.ReportCommonReqVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("客户统计分页查询请求")
public class CustReportReqVO extends ReportCommonReqVO {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "渠道名称（模糊查询）")
	private String channelName;

	@ApiModelProperty(value = "渠道编码集合")
	private List<String> channelCodeList;

	@ApiModelProperty(value = "订单状态集合")
	private List<Integer> orderStatusList;

	@ApiModelProperty(value = "订单状态（0申请中,1审批中,2待提现,3放款中,4还款中,5已结清,6失效,7拒单,8放款失败,9风控处理中,不送-全部）")
	private Integer orderStatus;

	@ApiModelProperty(value = "姓名（模糊查询）")
	private String name;

	@ApiModelProperty(value = "注册手机号")
	private String mobile;

	@ApiModelProperty(value = "身份证号")
	private String ic;

	@ApiModelProperty(value = "1-正常结清，2-提前结清，3-逾期，4-展期，5-复贷，不送-全部")
	private String queryType;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public List<String> getChannelCodeList() {
		return channelCodeList;
	}

	public void setChannelCodeList(List<String> channelCodeList) {
		this.channelCodeList = channelCodeList;
	}

	public List<Integer> getOrderStatusList() {
		return orderStatusList;
	}

	public void setOrderStatusList(List<Integer> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIc() {
		return ic;
	}

	public void setIc(String ic) {
		this.ic = ic;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

}
