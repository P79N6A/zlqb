package com.zhiwang.zfm.common.response.bean.activity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhiwang.zfm.common.request.PageCommon;

import io.swagger.annotations.ApiModelProperty;

/**   
 * @ClassName:  InviteRewardConfigListVO  
 * @Description:  邀请好友奖励流水记录信息
 * @Author: taohui 
 * @CreateDate: 2018年12月1日 下午3:18:13 
 * @Version: v1.0  
 */
public class InviteRewardSerialListVO extends PageCommon implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="流水记录id")
	private String serialId;			//标识
	
	@ApiModelProperty(value="邀请人客户id")
	private String custInfoId;			//邀请人客户id
	
	@ApiModelProperty(value="邀请关系id")
	private String connectId;			//邀请关系id
	
	@ApiModelProperty(value="交易金额")
	private Double money;			//交易金额
	
	@ApiModelProperty(value="交易后金额")
	private Double afterActiveMoney;			//交易后金额
	
	@ApiModelProperty(value="交易类型 0 支出 1 收入")
	private Integer type;			//交易类型 0 支出 1 收入
	
	@ApiModelProperty(value="交易摘要0 提现 1 注册成功 2 提交申请成功 3放款成功 4 还款结清 5 复贷成功")
	private Integer rewardMark;			//交易摘要 0 提现 1 注册成功 2 提交申请成功 3放款成功 4 还款结清 5 复贷成功
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value="交易时间")
	private java.util.Date createTime;			//创建时间

	@ApiModelProperty(value="查询开始时间")
	private String beginTime;  //开始时间
	
	@ApiModelProperty(value="查询结束时间")
	private String endTime;  //结束时间
	
	@ApiModelProperty(value="查询时间类型 1 今日  2 近三天  3 近一个月 4近三个月")
	private Integer timeType;  //查询时间类型
	
	@ApiModelProperty(value="被邀请客户手机号")
	private String invitedCustMobile;
	
	@ApiModelProperty(value="流水信息拼接,需要拼接得时候传true")
	private String serialInfo;  //流水信息拼接
	
	@ApiModelProperty(value="交易类型 0 支出 1 收入")
	private Integer status;			//交易状态 0 失败 1 成功  2处理中
	
	public String getInvitedCustMobile() {
		return invitedCustMobile;
	}

	public void setInvitedCustMobile(String invitedCustMobile) {
		this.invitedCustMobile = invitedCustMobile;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	public String getCustInfoId() {
		return custInfoId;
	}

	public void setCustInfoId(String custInfoId) {
		this.custInfoId = custInfoId;
	}

	public String getConnectId() {
		return connectId;
	}

	public void setConnectId(String connectId) {
		this.connectId = connectId;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getAfterActiveMoney() {
		return afterActiveMoney;
	}

	public void setAfterActiveMoney(Double afterActiveMoney) {
		this.afterActiveMoney = afterActiveMoney;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRewardMark() {
		return rewardMark;
	}

	public void setRewardMark(Integer rewardMark) {
		this.rewardMark = rewardMark;
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

	public Integer getTimeType() {
		return timeType;
	}

	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}

	public String getSerialInfo() {
		return serialInfo;
	}

	public void setSerialInfo(String serialInfo) {
		this.serialInfo = serialInfo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
