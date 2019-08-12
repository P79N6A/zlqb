package com.nyd.zeus.model.collection;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "添加催收记录入参", description = "添加催收记录入参")
public class CollectionRecord implements Serializable {

	private static final long serialVersionUID = -2462063161514333015L;

	@ApiModelProperty(value = "催收用户手机号")
    private String id;
	@ApiModelProperty(value = "订单号")
    private String orderNo;
	@ApiModelProperty(value = "用户Id")
    private String userId;
	@ApiModelProperty(value = "'催收手机号'")
    private String phone;
    @ApiModelProperty(value = "催收姓名")
    private String name;
    @ApiModelProperty(value = "是否承诺还款：0-是，1-否")
    private Integer isPromiseRepay;
    @ApiModelProperty(value = "添加催记催收人员id")
    private String sysUserId;
    @ApiModelProperty(value = "添加催记催收人员姓名")
    private String sysUserName;
    @ApiModelProperty(value = "联系人关系[0:本人,1:重要联系人,2:公司电话,3:通讯录联系人]")
    private Integer relationCode;
    @ApiModelProperty(value = "联系人关系说明")
    private String relationMsg;
    @ApiModelProperty(value = "催收备注")
    private String remark;
    @ApiModelProperty(value = "状态 0：启用；1：停用")
    private Integer status;
    @ApiModelProperty(value = "添加时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getIsPromiseRepay() {
		return isPromiseRepay;
	}
	public void setIsPromiseRepay(Integer isPromiseRepay) {
		this.isPromiseRepay = isPromiseRepay;
	}
	public String getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}
	public String getSysUserName() {
		return sysUserName;
	}
	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}
	public Integer getRelationCode() {
		return relationCode;
	}
	public void setRelationCode(Integer relationCode) {
		this.relationCode = relationCode;
	}
	public String getRelationMsg() {
		return relationMsg;
	}
	public void setRelationMsg(String relationMsg) {
		this.relationMsg = relationMsg;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public String toString() {
		return "CollectionRecord [id=" + id + ", orderNo=" + orderNo + ", userId=" + userId + ", phone=" + phone
				+ ", name=" + name + ", isPromiseRepay=" + isPromiseRepay + ", sysUserId=" + sysUserId
				+ ", sysUserName=" + sysUserName + ", relationCode=" + relationCode + ", relationMsg=" + relationMsg
				+ ", remark=" + remark + ", status=" + status + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}

}
