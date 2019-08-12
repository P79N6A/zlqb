package com.nyd.zeus.model.attendance;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "贷中考勤入参", description = "贷中考勤入参")
public class TimeAttendance implements Serializable {

	private static final long serialVersionUID = -4685815992864001158L;
	
	private String id;
    @ApiModelProperty(value = "系统用户Id")
	private String sysUserId;
    @ApiModelProperty(value = "系统用户姓名")
	private String sysUserName;
    @ApiModelProperty(value = "星期一[0:出勤，1:休息]")
	private Integer monday;
    @ApiModelProperty(value = "星期二[0:出勤，1:休息]")
	private Integer tuesday;
    @ApiModelProperty(value = "星期三[0:出勤，1:休息]")
	private Integer wednesday;
    @ApiModelProperty(value = "星期四[0:出勤，1:休息]")
	private Integer thursday;
    @ApiModelProperty(value = "星期五[0:出勤，1:休息]")
	private Integer friday;
    @ApiModelProperty(value = "星期六[0:出勤，1:休息]")
	private Integer saturday;
    @ApiModelProperty(value = "星期日[0:出勤，1:休息]")
	private Integer sunday;
    @ApiModelProperty(value = "更新系统用户Id")
	private String updateUserId;
    @ApiModelProperty(value = "更新系统用户姓名")
	private String updateUserName;
    @ApiModelProperty(value = "状态 0：启用；1：停用")
	private Integer status;
    @ApiModelProperty(value = "添加时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createTime;
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date updateTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public Integer getMonday() {
		return monday;
	}
	public void setMonday(Integer monday) {
		this.monday = monday;
	}
	public Integer getTuesday() {
		return tuesday;
	}
	public void setTuesday(Integer tuesday) {
		this.tuesday = tuesday;
	}
	public Integer getWednesday() {
		return wednesday;
	}
	public void setWednesday(Integer wednesday) {
		this.wednesday = wednesday;
	}
	public Integer getThursday() {
		return thursday;
	}
	public void setThursday(Integer thursday) {
		this.thursday = thursday;
	}
	public Integer getFriday() {
		return friday;
	}
	public void setFriday(Integer friday) {
		this.friday = friday;
	}
	public Integer getSaturday() {
		return saturday;
	}
	public void setSaturday(Integer saturday) {
		this.saturday = saturday;
	}
	public Integer getSunday() {
		return sunday;
	}
	public void setSunday(Integer sunday) {
		this.sunday = sunday;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getUpdateUserName() {
		return updateUserName;
	}
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
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

}
