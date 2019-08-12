package com.zhiwang.zfm.common.response.bean.activity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhiwang.zfm.common.request.PageCommon;

import io.swagger.annotations.ApiModelProperty;

/**   
 * @ClassName:  InviteCustInfoListVO  
 * @Description:  邀请好友活动邀请人信息
 * @Author: taohui 
 * @CreateDate: 2018年12月1日 下午3:18:13 
 * @Version: v1.0  
 */
public class InviteCustInfoListVO extends PageCommon implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="客户id")
	private String custInfoId;			//客户id
	
	@ApiModelProperty(value="客户姓名")
	private String custName;			//客户姓名
	
	@ApiModelProperty(value="客户手机号")
	private String mobile;			//客户手机号
	
	@ApiModelProperty(value="邀请好友总数量")
	private Integer inviteNum;			//邀请好友总数量
	
	@ApiModelProperty(value="邀请好友申请订单总数量")
	private Integer applayNum;			//邀请好友申请订单总数量
	
	@ApiModelProperty(value="邀请好友放款订单总数量")
	private Integer loanNum;			//邀请好友放款订单总数量
	
	@ApiModelProperty(value="邀请好友还款订单总数量")
	private Integer repaymentNum;			//邀请好友还款订单总数量
	
	@ApiModelProperty(value="邀请好友复贷订单总数量")
	private Integer repeatNum;			//邀请好友复贷订单总数量
	
	@ApiModelProperty(value="邀请好友奖励总金额")
	private Double inviteMoney;			//邀请好友奖励总金额
	
	@ApiModelProperty(value="邀请好友奖励账户可用余额")
	private Double inviteActiveMoney;			//邀请好友奖励账户可用余额
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value="注册时间")
	private java.util.Date createTime;			//注册时间

	public String getCustInfoId() {
		return custInfoId;
	}

	public void setCustInfoId(String custInfoId) {
		this.custInfoId = custInfoId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getInviteNum() {
		return inviteNum;
	}

	public void setInviteNum(Integer inviteNum) {
		this.inviteNum = inviteNum;
	}

	public Double getInviteMoney() {
		return inviteMoney;
	}

	public void setInviteMoney(Double inviteMoney) {
		this.inviteMoney = inviteMoney;
	}

	public Double getInviteActiveMoney() {
		return inviteActiveMoney;
	}

	public void setInviteActiveMoney(Double inviteActiveMoney) {
		this.inviteActiveMoney = inviteActiveMoney;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public Integer getApplayNum() {
		return applayNum;
	}

	public void setApplayNum(Integer applayNum) {
		this.applayNum = applayNum;
	}

	public Integer getLoanNum() {
		return loanNum;
	}

	public void setLoanNum(Integer loanNum) {
		this.loanNum = loanNum;
	}

	public Integer getRepaymentNum() {
		return repaymentNum;
	}

	public void setRepaymentNum(Integer repaymentNum) {
		this.repaymentNum = repaymentNum;
	}

	public Integer getRepeatNum() {
		return repeatNum;
	}

	public void setRepeatNum(Integer repeatNum) {
		this.repeatNum = repeatNum;
	}
}
