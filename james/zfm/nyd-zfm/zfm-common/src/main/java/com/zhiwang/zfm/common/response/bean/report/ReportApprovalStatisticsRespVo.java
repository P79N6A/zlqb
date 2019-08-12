package com.zhiwang.zfm.common.response.bean.report;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("审批统计查询返回")
public class ReportApprovalStatisticsRespVo {

	@ApiModelProperty(value = "主键id")
	private String id; // 主键id
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private java.util.Date createTime; // 创建时间
	@ApiModelProperty(value = "统计日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private java.util.Date statisticalTime; // 统计日期
	@ApiModelProperty(value = "归属平台编码")
	private String platCode; // 归属平台编码
	@ApiModelProperty(value = "归属平台名称")
	private String platName; // 归属平台名称
	@ApiModelProperty(value = "归属渠道编码", hidden = true)
	private String channelCode; // 归属渠道编码
	@ApiModelProperty(value = "归属渠道名称", hidden = true)
	private String channelName; // 归属渠道名称

	@ApiModelProperty(value = "注册数")
	private Integer registrationCount; // 注册数
	@ApiModelProperty(value = "身份认证数")
	private Integer identityAuthenticationCount; // 身份认证数
	@ApiModelProperty(value = "个人信息认证数")
	private Integer personInfoAuthenticationCount; // 个人信息认证数
	@ApiModelProperty(value = "银行卡认证数")
	private Integer bankCardAuthenticationCount; // 银行卡认证数
	@ApiModelProperty(value = "运营商认证数")
	private Integer carrierOperatorAuthenticationCount; // 运营商认证数
	@ApiModelProperty(value = "申请数")
	private Integer applyCount; // 申请数
	@ApiModelProperty(value = "备注")
	private String remark; // 备注

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

	public Integer getRegistrationCount() {
		return registrationCount;
	}

	public void setRegistrationCount(Integer registrationCount) {
		this.registrationCount = registrationCount;
	}

	public Integer getIdentityAuthenticationCount() {
		return identityAuthenticationCount;
	}

	public void setIdentityAuthenticationCount(Integer identityAuthenticationCount) {
		this.identityAuthenticationCount = identityAuthenticationCount;
	}

	public Integer getPersonInfoAuthenticationCount() {
		return personInfoAuthenticationCount;
	}

	public void setPersonInfoAuthenticationCount(Integer personInfoAuthenticationCount) {
		this.personInfoAuthenticationCount = personInfoAuthenticationCount;
	}

	public Integer getBankCardAuthenticationCount() {
		return bankCardAuthenticationCount;
	}

	public void setBankCardAuthenticationCount(Integer bankCardAuthenticationCount) {
		this.bankCardAuthenticationCount = bankCardAuthenticationCount;
	}

	public Integer getCarrierOperatorAuthenticationCount() {
		return carrierOperatorAuthenticationCount;
	}

	public void setCarrierOperatorAuthenticationCount(Integer carrierOperatorAuthenticationCount) {
		this.carrierOperatorAuthenticationCount = carrierOperatorAuthenticationCount;
	}

	public Integer getApplyCount() {
		return applyCount;
	}

	public void setApplyCount(Integer applyCount) {
		this.applyCount = applyCount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
