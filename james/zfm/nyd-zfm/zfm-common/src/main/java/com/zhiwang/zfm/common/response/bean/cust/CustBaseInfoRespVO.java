package com.zhiwang.zfm.common.response.bean.cust;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class CustBaseInfoRespVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "姓名")
	private String name; // 姓名
	@ApiModelProperty(value = "民族")
	private String nation;
	@ApiModelProperty(value = "性别(1男,2女)")
	private String sex; // 性别(1男,2女)
	@ApiModelProperty(value = "性别说明")
	private String sexDesc; // 性别说明
	@ApiModelProperty(value = "身份证号")
	private String ic; // 身份证号
	@ApiModelProperty(value = "生日")
	private String birthday;
	@ApiModelProperty(value = "年龄")
	private String age; // 年龄
	@ApiModelProperty(value = "订单集合")
	private List<CustBaseInfoOrderRespVO> orderList;
	@ApiModelProperty(value = "联系人集合")
	private List<CustBaseInfoLinkRespVO> linkList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSexDesc() {
		return sexDesc;
	}

	public void setSexDesc(String sexDesc) {
		this.sexDesc = sexDesc;
	}

	public String getIc() {
		return ic;
	}

	public void setIc(String ic) {
		this.ic = ic;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public List<CustBaseInfoOrderRespVO> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<CustBaseInfoOrderRespVO> orderList) {
		this.orderList = orderList;
	}

	public List<CustBaseInfoLinkRespVO> getLinkList() {
		return linkList;
	}

	public void setLinkList(List<CustBaseInfoLinkRespVO> linkList) {
		this.linkList = linkList;
	}

}
