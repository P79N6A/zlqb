package com.creativearts.nyd.collectionPay.model.zzl.helibao.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by heli50 on 2017/4/14.
 */

@ApiModel(description="商户用户注册")
public class MerchantUserVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="交易类型",hidden=true)
    private String p1_bizType;
	
	@ApiModelProperty(value="商户编号",hidden=true)
    private String p2_customerNumber;
	
	@ApiModelProperty(value="商户订单号",hidden=true)
    private String p3_orderId;
	
	@ApiModelProperty(value="用户姓名")
    private String p4_legalPerson;
	
	@ApiModelProperty(value="身份证号")
    private String p5_legalPersonID;
	
	@ApiModelProperty(value="手机号")
    private String p6_mobile;
	
	@ApiModelProperty(value="账户类型")
    private String p7_business;
	
	@ApiModelProperty(value="时间戳",hidden=true)
    private String p8_timestamp;
	
	@ApiModelProperty(value="拓展参数 类 P9Ext",hidden=true)
    private String p9_ext;

	public String getP1_bizType() {
		return p1_bizType;
	}

	public void setP1_bizType(String p1_bizType) {
		this.p1_bizType = p1_bizType;
	}

	public String getP2_customerNumber() {
		return p2_customerNumber;
	}

	public void setP2_customerNumber(String p2_customerNumber) {
		this.p2_customerNumber = p2_customerNumber;
	}

	public String getP3_orderId() {
		return p3_orderId;
	}

	public void setP3_orderId(String p3_orderId) {
		this.p3_orderId = p3_orderId;
	}

	public String getP4_legalPerson() {
		return p4_legalPerson;
	}

	public void setP4_legalPerson(String p4_legalPerson) {
		this.p4_legalPerson = p4_legalPerson;
	}

	public String getP5_legalPersonID() {
		return p5_legalPersonID;
	}

	public void setP5_legalPersonID(String p5_legalPersonID) {
		this.p5_legalPersonID = p5_legalPersonID;
	}

	public String getP6_mobile() {
		return p6_mobile;
	}

	public void setP6_mobile(String p6_mobile) {
		this.p6_mobile = p6_mobile;
	}

	public String getP7_business() {
		return p7_business;
	}

	public void setP7_business(String p7_business) {
		this.p7_business = p7_business;
	}

	public String getP8_timestamp() {
		return p8_timestamp;
	}

	public void setP8_timestamp(String p8_timestamp) {
		this.p8_timestamp = p8_timestamp;
	}

	public String getP9_ext() {
		return p9_ext;
	}

	public void setP9_ext(String p9_ext) {
		this.p9_ext = p9_ext;
	}

    
}
