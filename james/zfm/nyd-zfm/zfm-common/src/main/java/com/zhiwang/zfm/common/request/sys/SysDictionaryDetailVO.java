package com.zhiwang.zfm.common.request.sys;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import com.zhiwang.zfm.common.request.PageCommon;


/**
 * 数据字典明细
 * 
 * @author huangzhenggang
 * @date 2018-05-14 15:00:04
 */
public class SysDictionaryDetailVO extends PageCommon implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	@ApiModelProperty(value="")
	private String id;
	//明细名称
	@ApiModelProperty(value="明细名称")
	private String name;
	//类型(大类)编码
	@ApiModelProperty(value="类型(大类)ID")
	private String dictionaryId;
	//大类编码
	@ApiModelProperty(value="大类编码")
	private String code;
	//类型(明细)编码
	@ApiModelProperty(value="类型(明细)编码")
	private String detailCode;
	//状态(1:可用 0:停用)
	@ApiModelProperty(value="状态(1:可用 0:停用)")
	private Integer status;
	//明细值
	@ApiModelProperty(value="明细值")
	private String price;
	//排序码
	@ApiModelProperty(value="排序码")
	private Long reorder;
	
	public Long getReorder() {
		return reorder;
	}
	public void setReorder(Long reorder) {
		this.reorder = reorder;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 设置：
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：明细名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：明细名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：类型(大类)编码
	 */
	public void setDictionaryId(String dictionaryId) {
		this.dictionaryId = dictionaryId;
	}
	/**
	 * 获取：类型(大类)编码
	 */
	public String getDictionaryId() {
		return dictionaryId;
	}
	/**
	 * 设置：类型(明细)编码
	 */
	public void setDetailCode(String detailCode) {
		this.detailCode = detailCode;
	}
	/**
	 * 获取：类型(明细)编码
	 */
	public String getDetailCode() {
		return detailCode;
	}
	/**
	 * 设置：状态(1:可用 0:停用)
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态(1:可用 0:停用)
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：明细值
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	/**
	 * 获取：明细值
	 */
	public String getPrice() {
		return price;
	}
}
