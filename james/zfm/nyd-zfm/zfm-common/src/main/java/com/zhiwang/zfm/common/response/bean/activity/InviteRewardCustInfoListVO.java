package com.zhiwang.zfm.common.response.bean.activity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhiwang.zfm.common.request.PageCommon;

import io.swagger.annotations.ApiModelProperty;

/**   
 * @ClassName:  InviteRewardConfigListVO  
 * @Description:  邀请好友奖励记录信息
 * @Author: taohui 
 * @CreateDate: 2018年12月1日 下午3:18:13 
 * @Version: v1.0  
 */
public class InviteRewardCustInfoListVO extends PageCommon implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="邀请客户id")
	private String custInfoId;			//邀请人客户id;
	
	@ApiModelProperty(value="被邀请客户id")
	private String invitedCustId;
	
	@ApiModelProperty(value="被邀请客户姓名")
	private String invitedCustName;
	
	@ApiModelProperty(value="被邀请客户手机号")
	private String invitedCustMobile;
	
	@ApiModelProperty(value="交易摘要 1 注册成功 2 提交申请成功 3放款成功 4 还款结清 5 复贷成功")
	private Integer rewardMark;
	
	@ApiModelProperty(value="交易金额")
	private Double money;			//交易金额
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value="交易时间")
	private java.util.Date createTime;	//创建时间
	
	@ApiModelProperty(value="查询开始时间")
	private String beginTime;  //开始时间
	
	@ApiModelProperty(value="查询结束时间")
	private String endTime;  //结束时间

	public String getInvitedCustId() {
		return invitedCustId;
	}

	public void setInvitedCustId(String invitedCustId) {
		this.invitedCustId = invitedCustId;
	}

	public String getInvitedCustName() {
		return invitedCustName;
	}

	public void setInvitedCustName(String invitedCustName) {
		this.invitedCustName = invitedCustName;
	}

	public String getInvitedCustMobile() {
		return invitedCustMobile;
	}

	public void setInvitedCustMobile(String invitedCustMobile) {
		this.invitedCustMobile = invitedCustMobile;
	}

	public Integer getRewardMark() {
		return rewardMark;
	}

	public void setRewardMark(Integer rewardMark) {
		this.rewardMark = rewardMark;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCustInfoId() {
		return custInfoId;
	}

	public void setCustInfoId(String custInfoId) {
		this.custInfoId = custInfoId;
	}
}
