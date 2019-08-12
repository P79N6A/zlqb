package com.zhiwang.zfm.common.request.sys;

import io.swagger.annotations.ApiModelProperty;

public class SysParameterVO {
	@ApiModelProperty(value = "id")	private String id;			//
	@ApiModelProperty(value = "提现大额值")	private java.math.BigDecimal withdrawCashAmount;			//体现大额值
	@ApiModelProperty(value = "拒单天数")	private Integer refuseDay;			//拒单天数
	@ApiModelProperty(value = "个人基本信息认证失效时间")	private Integer userinfoVerifyDay;			//个人基本信息认证失效时间(天)
	@ApiModelProperty(value = "运营商信息认证失效时间")	private Integer mobileVerifyDay;			//运营商信息认证失效时间(天)	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public java.math.BigDecimal getWithdrawCashAmount() {	    return this.withdrawCashAmount;	}	public void setWithdrawCashAmount(java.math.BigDecimal withdrawCashAmount) {	    this.withdrawCashAmount=withdrawCashAmount;	}	public Integer getRefuseDay() {	    return this.refuseDay;	}	public void setRefuseDay(Integer refuseDay) {	    this.refuseDay=refuseDay;	}	public Integer getUserinfoVerifyDay() {	    return this.userinfoVerifyDay;	}	public void setUserinfoVerifyDay(Integer userinfoVerifyDay) {	    this.userinfoVerifyDay=userinfoVerifyDay;	}	public Integer getMobileVerifyDay() {	    return this.mobileVerifyDay;	}	public void setMobileVerifyDay(Integer mobileVerifyDay) {	    this.mobileVerifyDay=mobileVerifyDay;	}
}
