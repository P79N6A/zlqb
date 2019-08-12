package com.zhiwang.zfm.common.request.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhiwang.zfm.common.request.PageCommon;


/**
 * Created by  on 2019/07/17
 * 借款返回对象
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("订单列表分页查询请求")
public class OrderlistVo extends PageCommon{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//订单编号
	@ApiModelProperty(value = "订单编号")
    private String orderNo;
    //订单状态
	@ApiModelProperty(value = "订单状态")
    private String orderStarus;
    //产品期数
	@ApiModelProperty(value = "产品期数")
    private String productPeriods;
    //用户姓名
	@ApiModelProperty(value = "用户姓名")
    private String custName;
    //用户手机号
	@ApiModelProperty(value = "用户手机号")
    private String mobile;
    //注册来源
	@ApiModelProperty(value = "注册来源")
    private String source;
    //申请时间
	@ApiModelProperty(value = "申请时间")
    private String createTime;
    //产品名称
	@ApiModelProperty(value = "产品名称")
    private String procuctName;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderStarus() {
		return orderStarus;
	}
	public void setOrderStarus(String orderStarus) {
		this.orderStarus = orderStarus;
	}
	public String getProductPeriods() {
		return productPeriods;
	}
	public void setProductPeriods(String productPeriods) {
		this.productPeriods = productPeriods;
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getProcuctName() {
		return procuctName;
	}
	public void setProcuctName(String procuctName) {
		this.procuctName = procuctName;
	}
    
    
}
