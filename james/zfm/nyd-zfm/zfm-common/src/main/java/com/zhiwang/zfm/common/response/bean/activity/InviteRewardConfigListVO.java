package com.zhiwang.zfm.common.response.bean.activity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhiwang.zfm.common.request.PageCommon;

import io.swagger.annotations.ApiModelProperty;

/**   
 * @ClassName:  InviteRewardConfigListVO  
 * @Description:  邀请好友活动奖励规则配置信息
 * @Author: taohui 
 * @CreateDate: 2018年12月1日 下午3:18:13 
 * @Version: v1.0  
 */
public class InviteRewardConfigListVO extends PageCommon implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="配置信息id")
	private String configId;			//标识
	
	@ApiModelProperty(value="更新人")
	private String createSysUserName;			//更新人
	
	@ApiModelProperty(value="更新人id")
	private String createSysUserId;
	
	@ApiModelProperty(value="适用用户 0 注册用户 1 实名用户 2 成功借款用户")
	private Integer type;			//适用用户 0 注册用户 1 实名用户 2 成功借款用户
	
	@ApiModelProperty(value="可提现金额限制")
	private Double withdrawMoney;			//可提现金额限制
	
	@ApiModelProperty(value="注册奖励开关 0 关闭,1 开启")
	private Integer registerRewardStatus;			//注册奖励开关 0 关闭,1 开启
	
	@ApiModelProperty(value="注册成功奖励金额")
	private Double registerRewardMoney;			//注册成功奖励金额
	
	@ApiModelProperty(value="提交申请奖励开关 0 关闭,1 开启")
	private Integer applayRewardStatus;			//提交申请奖励开关 0 关闭,1 开启
	
	@ApiModelProperty(value="提交申请成功奖励金额")
	private Double applayRewardMoney;			//提交申请成功奖励金额
	
	@ApiModelProperty(value="放款奖励开关 0 关闭,1 开启")
	private Integer loanRewardStatus;			//放款奖励开关 0 关闭,1 开启
	
	@ApiModelProperty(value="放款成功奖励金额")
	private Double loanRewardMoney;			//放款成功奖励金额
	
	@ApiModelProperty(value="还款奖励开关 0 关闭,1 开启")
	private Integer repaymentRewardStatus;			//还款奖励开关 0 关闭,1 开启
	
	@ApiModelProperty(value="还款成功奖励金额")
	private Double repaymentRewardMoney;			//还款成功奖励金额
	
	@ApiModelProperty(value="复贷奖励开关 0 关闭,1 开启")
	private Integer repeatRewardStatus;			//复贷奖励开关 0 关闭,1 开启
	
	@ApiModelProperty(value="复贷成功奖励金额")
	private Double repeatRewardMoney;			//复贷成功奖励金额
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value="更新时间")
	private java.util.Date updateTime;			//更新时间
	
	@ApiModelProperty(value="活动规则")
	private String rule;			//活动规则
	
	@ApiModelProperty(value="奖励规则")
	private String rewardRule;			//奖励规则
	
	@ApiModelProperty(value="活动状态 0 关闭 1开启")
	private Integer status;			//活动状态 0 关闭 1开启
	
	@ApiModelProperty(value="奖励规则开关数组")
	private String[] statusString;
	public String getConfigId() {
		return configId;
	}
	public void setConfigId(String configId) {
		this.configId = configId;
	}
	public String getCreateSysUserId() {
	    return this.createSysUserId;
	}
	public void setCreateSysUserId(String createSysUserId) {
	    this.createSysUserId=createSysUserId;
	}
	public Integer getType() {
	    return this.type;
	}
	public void setType(Integer type) {
	    this.type=type;
	}
	public Double getWithdrawMoney() {
	    return this.withdrawMoney;
	}
	public void setWithdrawMoney(Double withdrawMoney) {
	    this.withdrawMoney=withdrawMoney;
	}
	public Integer getRegisterRewardStatus() {
	    return this.registerRewardStatus;
	}
	public void setRegisterRewardStatus(Integer registerRewardStatus) {
	    this.registerRewardStatus=registerRewardStatus;
	}
	public Double getRegisterRewardMoney() {
	    return this.registerRewardMoney;
	}
	public void setRegisterRewardMoney(Double registerRewardMoney) {
	    this.registerRewardMoney=registerRewardMoney;
	}
	public Integer getApplayRewardStatus() {
	    return this.applayRewardStatus;
	}
	public void setApplayRewardStatus(Integer applayRewardStatus) {
	    this.applayRewardStatus=applayRewardStatus;
	}
	public Double getApplayRewardMoney() {
	    return this.applayRewardMoney;
	}
	public void setApplayRewardMoney(Double applayRewardMoney) {
	    this.applayRewardMoney=applayRewardMoney;
	}
	public Integer getLoanRewardStatus() {
	    return this.loanRewardStatus;
	}
	public void setLoanRewardStatus(Integer loanRewardStatus) {
	    this.loanRewardStatus=loanRewardStatus;
	}
	public Double getLoanRewardMoney() {
	    return this.loanRewardMoney;
	}
	public void setLoanRewardMoney(Double loanRewardMoney) {
	    this.loanRewardMoney=loanRewardMoney;
	}
	public Integer getRepaymentRewardStatus() {
	    return this.repaymentRewardStatus;
	}
	public void setRepaymentRewardStatus(Integer repaymentRewardStatus) {
	    this.repaymentRewardStatus=repaymentRewardStatus;
	}
	public Double getRepaymentRewardMoney() {
	    return this.repaymentRewardMoney;
	}
	public void setRepaymentRewardMoney(Double repaymentRewardMoney) {
	    this.repaymentRewardMoney=repaymentRewardMoney;
	}
	public Integer getRepeatRewardStatus() {
	    return this.repeatRewardStatus;
	}
	public void setRepeatRewardStatus(Integer repeatRewardStatus) {
	    this.repeatRewardStatus=repeatRewardStatus;
	}
	public Double getRepeatRewardMoney() {
	    return this.repeatRewardMoney;
	}
	public void setRepeatRewardMoney(Double repeatRewardMoney) {
	    this.repeatRewardMoney=repeatRewardMoney;
	}
	public java.util.Date getUpdateTime() {
	    return this.updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
	    this.updateTime=updateTime;
	}
	public String getRule() {
	    return this.rule;
	}
	public void setRule(String rule) {
	    this.rule=rule;
	}
	public String getCreateSysUserName() {
		return createSysUserName;
	}
	public void setCreateSysUserName(String createSysUserName) {
		this.createSysUserName = createSysUserName;
	}
	public String getRewardRule() {
		return rewardRule;
	}
	public void setRewardRule(String rewardRule) {
		this.rewardRule = rewardRule;
	}
	public String[] getStatusString() {
		return statusString;
	}
	public void setStatusString(String[] statusString) {
		this.statusString = statusString;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
