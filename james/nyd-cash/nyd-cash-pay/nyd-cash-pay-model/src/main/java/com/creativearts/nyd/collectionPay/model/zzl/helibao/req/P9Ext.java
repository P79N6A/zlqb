package com.creativearts.nyd.collectionPay.model.zzl.helibao.req;

import io.swagger.annotations.ApiModelProperty;

public class P9Ext {
	
	@ApiModelProperty(value="公司名称")
	private String companyName;
	
	@ApiModelProperty(value="统一社会信用代码")
	private String unifiedCodeCert;
	
	@ApiModelProperty(value="商户订单号")
	private String P3_orderId;
	
	@ApiModelProperty(value="地址")
	private String address;
	
	@ApiModelProperty(value="性别")
	private String sex;
	
	@ApiModelProperty(value="出生日期")
	private String birthDay;
	
	@ApiModelProperty(value="学历")
	private String education;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUnifiedCodeCert() {
		return unifiedCodeCert;
	}

	public void setUnifiedCodeCert(String unifiedCodeCert) {
		this.unifiedCodeCert = unifiedCodeCert;
	}

	public String getP3_orderId() {
		return P3_orderId;
	}

	public void setP3_orderId(String p3_orderId) {
		P3_orderId = p3_orderId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	
	
}
