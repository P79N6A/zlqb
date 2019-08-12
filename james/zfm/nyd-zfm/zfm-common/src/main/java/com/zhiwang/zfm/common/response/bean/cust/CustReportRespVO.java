package com.zhiwang.zfm.common.response.bean.cust;

import java.io.Serializable;

import com.zhiwang.zfm.common.util.ArithUtil;

import io.swagger.annotations.ApiModelProperty;

public class CustReportRespVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "借款日期")
	private String loanDate; // 借款日期
	@ApiModelProperty(value = "平台编码")
	private String platCode; // 平台编码
	@ApiModelProperty(value = "平台名称")
	private String platName; // 平台名称
	@ApiModelProperty(value = "渠道编码")
	private String channelCode; // 渠道编码
	@ApiModelProperty(value = "渠道名称")
	private String channelName; // 渠道名称
	@ApiModelProperty(value = "订单id")
	private String orderId; // 订单id
	@ApiModelProperty(value = "借款期次")
	private String applyNum; // 借款期次
	@ApiModelProperty(value = "放款期次")
	private String loanNum; // 放款期次
	@ApiModelProperty(value = "展期期次")
	private String extensionNum; // 展期期次
	@ApiModelProperty(value = "累积借款期次")
	private String applyNumSum; // 累积借款期次
	@ApiModelProperty(value = "累积放款期次")
	private String loanNumSum; // 累积放款期次
	@ApiModelProperty(value = "累积展期期次")
	private String extensionNumSum; // 累积展期期次
	@ApiModelProperty(value = "放款额度")
	private String loanMoney; // 放款额度
	@ApiModelProperty(value = "订单状态")
	private String orderStatus; // 订单状态(0申请中,1审批中,2待提现,3放款中,4还款中,5已结清，6失效，7拒单 8放款失败 9风控处理中)
	@ApiModelProperty(value = "订单状态说明")
	private String orderStatusDesc; // 订单状态说明
	@ApiModelProperty(value = "应还日期")
	private String payTime; // 应还日期
	@ApiModelProperty(value = "还款状态(0待还,1结清,2逾期,3展期)")
	private String payControlStatus; // 还款状态(0待还,1结清,2逾期,3展期)
	@ApiModelProperty(value = "还款状态说明")
	private String payControlStatusDesc;
	@ApiModelProperty(value = "用户id")
	private String custInfoId; // 用户id
	@ApiModelProperty(value = "姓名")
	private String name; // 姓名
	@ApiModelProperty(value = "注册时间")
	private String registerTime; // 注册时间
	@ApiModelProperty(value = "注册手机号")
	private String mobile; // 注册手机号
	@ApiModelProperty(value = "身份证号")
	private String ic; // 身份证号
	@ApiModelProperty(value = "年龄")
	private String age; // 年龄
	@ApiModelProperty(value = "性别(1男,2女)")
	private String sex; // 性别(1男,2女)
	@ApiModelProperty(value = "性别说明")
	private String sexDesc; // 性别说明
	@ApiModelProperty(value = "状态(1有效,0失效)")
	private String custStatus; // 状态(1有效,0失效)
	@ApiModelProperty(value = "客户状态说明")
	private String custStatusDesc; // 客户状态说明
	@ApiModelProperty(value = "状态(0实名,1未实名有订单,2未实名无订单)")
	private String custReportStatus; // 状态(0实名,1未实名有订单,2未实名无订单)

	public String getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(String applyNum) {
		this.applyNum = applyNum;
	}

	public String getLoanNum() {
		return loanNum;
	}

	public void setLoanNum(String loanNum) {
		this.loanNum = loanNum;
	}

	public String getExtensionNum() {
		return extensionNum;
	}

	public void setExtensionNum(String extensionNum) {
		this.extensionNum = extensionNum;
	}

	public String getApplyNumSum() {
		return applyNumSum;
	}

	public void setApplyNumSum(String applyNumSum) {
		this.applyNumSum = applyNumSum;
	}

	public String getLoanNumSum() {
		return loanNumSum;
	}

	public void setLoanNumSum(String loanNumSum) {
		this.loanNumSum = loanNumSum;
	}

	public String getExtensionNumSum() {
		return extensionNumSum;
	}

	public void setExtensionNumSum(String extensionNumSum) {
		this.extensionNumSum = extensionNumSum;
	}

	public String getLoanMoney() {
		return loanMoney;
	}

	public void setLoanMoney(String loanMoney) {
		this.loanMoney = ArithUtil.formatValue(loanMoney);
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}

	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getPayControlStatus() {
		return payControlStatus;
	}

	public void setPayControlStatus(String payControlStatus) {
		this.payControlStatus = payControlStatus;
	}

	public String getPayControlStatusDesc() {
		return payControlStatusDesc;
	}

	public void setPayControlStatusDesc(String payControlStatusDesc) {
		this.payControlStatusDesc = payControlStatusDesc;
	}

	public String getCustInfoId() {
		return custInfoId;
	}

	public void setCustInfoId(String custInfoId) {
		this.custInfoId = custInfoId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
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

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSexDesc() {
		return sexDesc;
	}

	public void setSexDesc(String sexDesc) {
		this.sexDesc = sexDesc;
	}

	public String getCustStatus() {
		return custStatus;
	}

	public void setCustStatus(String custStatus) {
		this.custStatus = custStatus;
	}

	public String getCustStatusDesc() {
		return custStatusDesc;
	}

	public void setCustStatusDesc(String custStatusDesc) {
		this.custStatusDesc = custStatusDesc;
	}

	public String getCustReportStatus() {
		return custReportStatus;
	}

	public void setCustReportStatus(String custReportStatus) {
		this.custReportStatus = custReportStatus;
	}

}
