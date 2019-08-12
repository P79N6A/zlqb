package com.zhiwang.zfm.common.request.sys;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import com.zhiwang.zfm.common.request.PageCommon;


/**
 * 数据字典
 * 
 * @author huangzhenggang
 * @date 2018-05-14 15:00:04
 */
public class SysDictionaryVO extends PageCommon implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	@ApiModelProperty(value="主键")
	private String id;
	//类型(大类)名称
	@ApiModelProperty(value="类型(大类)名称")
	private String name;
	//类型(大类)编码
	@ApiModelProperty(value="类型(大类)编码")
	private String code;
	//大类状态(1:可用 0:停用)
	@ApiModelProperty(value="大类状态(1:可用 0:停用)")
	private Integer status;

	/**
	 * 设置：主键
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：类型(大类)名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：类型(大类)名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：类型(大类)编码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：类型(大类)编码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：大类状态(1:可用 0:停用)
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：大类状态(1:可用 0:停用)
	 */
	public Integer getStatus() {
		return status;
	}
}
