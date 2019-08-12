package com.nyd.zeus.model;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhujx on 2017/11/18.
 */
public class PaymentRiskFlowVo implements Serializable{

	private static final long serialVersionUID = -8673995535008404726L;
	
	// 初始状态  S：成功；F：失败 P: 处理中
	@ApiModelProperty("S：成功；F：失败 P: 处理中")
    private String status;
    // 请求时间
	@ApiModelProperty(" 请求时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date requestTime;
    // 响应时间
	@ApiModelProperty(" 响应时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date responseTime;
	@ApiModelProperty(" 流水号")
    private String seriNo;
	@ApiModelProperty(" 订单编号")
    private String orderNo;
	@ApiModelProperty(" 金额")
    private String money;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	public Date getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}
	public String getSeriNo() {
		return seriNo;
	}
	public void setSeriNo(String seriNo) {
		this.seriNo = seriNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	@Override
	public String toString() {
		return "PaymentRiskFlowVo [status=" + status + ", requestTime=" + requestTime + ", responseTime=" + responseTime
				+ ", seriNo=" + seriNo + ", orderNo=" + orderNo + ", money=" + money + "]";
	}
    

}
