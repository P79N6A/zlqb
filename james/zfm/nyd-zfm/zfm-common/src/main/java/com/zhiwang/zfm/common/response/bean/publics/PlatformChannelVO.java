package com.zhiwang.zfm.common.response.bean.publics;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhiwang.zfm.common.request.PageCommon;

import io.swagger.annotations.ApiModelProperty;

public class PlatformChannelVO extends PageCommon implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="渠道id")
	private String id;			//标识
	@ApiModelProperty(value="名称")	private String name;			//名称
	@ApiModelProperty(value="渠道推广地址")	private String url;			//渠道推广地址
	@ApiModelProperty(value="渠道编码")	private String channelCode;			//渠道编码
	@ApiModelProperty(value="状态(1,可用 0不可用)")
	private Integer status;			//状态(1,可用 0不可用)
	private String statusMsg;			//状态值
	@ApiModelProperty(value="状态(1有效0删除)",hidden=true)
	private Integer delStatus;		//状态(1有效0删除)
	@ApiModelProperty(value="渠道是否开启扣量(1开启,0不开启)")	private Integer deductStatus;			//渠道是否开启扣量(1开启,0不开启)
	@ApiModelProperty(value="扣量比例")	private Double deductRate;			//扣量比例
	@ApiModelProperty(value="推广员用户的id")	private String promotionSysUserId;			//推广员用户的id
	@ApiModelProperty(value="推广员用户名")
	private String promotionSysUserName;			//推广员用户名
	@ApiModelProperty(value="创建人id")	private String createSysUserId;			//创建人id
	@ApiModelProperty(value="创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")	private Date createTime;			//创建时间
	@ApiModelProperty(value="更新人id")
	private String updateSysUserId;			//创建人id
	@ApiModelProperty(value="更新人")
	private String updateSysUserName;			//更新人
	@ApiModelProperty(value="修改时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")	private Date updateTime;			//修改时间
	@ApiModelProperty(value="父级渠道标识")
	private String pid;			//父级渠道标识
	@ApiModelProperty(value="父级渠道名称")
	private String pname;			//父级渠道名称
	@ApiModelProperty(value="联系人")
	private String linkmanName;			//联系人
	@ApiModelProperty(value="联系电话")
	private String phone;			//联系电话	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public String getUrl() {	    return this.url;	}	public void setUrl(String url) {	    this.url=url;	}	public String getChannelCode() {	    return this.channelCode;	}	public void setChannelCode(String channelCode) {	    this.channelCode=channelCode;	}	public Integer getStatus() {	    return this.status;	}	public void setStatus(Integer status) {	    this.status=status;	}	public Integer getDeductStatus() {	    return this.deductStatus;	}	public void setDeductStatus(Integer deductStatus) {	    this.deductStatus=deductStatus;	}	public Double getDeductRate() {	    return this.deductRate;	}	public void setDeductRate(Double deductRate) {	    this.deductRate=deductRate;	}	public String getPromotionSysUserId() {	    return this.promotionSysUserId;	}	public void setPromotionSysUserId(String promotionSysUserId) {	    this.promotionSysUserId=promotionSysUserId;	}	public String getCreateSysUserId() {	    return this.createSysUserId;	}	public void setCreateSysUserId(String createSysUserId) {	    this.createSysUserId=createSysUserId;	}	
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
	public Integer getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	public String getUpdateSysUserId() {
		return updateSysUserId;
	}
	public void setUpdateSysUserId(String updateSysUserId) {
		this.updateSysUserId = updateSysUserId;
	}
	public String getUpdateSysUserName() {
		return updateSysUserName;
	}
	public void setUpdateSysUserName(String updateSysUserName) {
		this.updateSysUserName = updateSysUserName;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getLinkmanName() {
		return linkmanName;
	}
	public void setLinkmanName(String linkmanName) {
		this.linkmanName = linkmanName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPromotionSysUserName() {
		return promotionSysUserName;
	}
	public void setPromotionSysUserName(String promotionSysUserName) {
		this.promotionSysUserName = promotionSysUserName;
	}
	
}
