package com.zhiwang.zfm.entity.sys.query;

import com.zhiwang.zfm.common.page.BaseSearchForm;

public class SysParameterSearchForm extends BaseSearchForm {
	
		private String id;			//	private java.math.BigDecimal withdrawCashAmount;			//体现大额值	private Integer refuseDay;			//拒单天数	private java.util.Date createTime;			//创建时间	private java.util.Date updateTime;			//更新时间	private Integer userinfoVerifyDay;			//个人基本信息认证失效时间(天)	private Integer mobileVerifyDay;			//运营商信息认证失效时间(天)	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public java.math.BigDecimal getWithdrawCashAmount() {	    return this.withdrawCashAmount;	}	public void setWithdrawCashAmount(java.math.BigDecimal withdrawCashAmount) {	    this.withdrawCashAmount=withdrawCashAmount;	}	public Integer getRefuseDay() {	    return this.refuseDay;	}	public void setRefuseDay(Integer refuseDay) {	    this.refuseDay=refuseDay;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public java.util.Date getUpdateTime() {	    return this.updateTime;	}	public void setUpdateTime(java.util.Date updateTime) {	    this.updateTime=updateTime;	}	public Integer getUserinfoVerifyDay() {	    return this.userinfoVerifyDay;	}	public void setUserinfoVerifyDay(Integer userinfoVerifyDay) {	    this.userinfoVerifyDay=userinfoVerifyDay;	}	public Integer getMobileVerifyDay() {	    return this.mobileVerifyDay;	}	public void setMobileVerifyDay(Integer mobileVerifyDay) {	    this.mobileVerifyDay=mobileVerifyDay;	}
	
}
