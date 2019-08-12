package com.creativearts.nyd.collectionPay.model.zzl.helibao.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.nyd.pay.api.annotation.zzl.SignExclude;


/**
 * Created by heli50 on 2017/4/14.
 */
@ApiModel(description="进入订单查询接口")
public class QueryOrderVo{
	
	
	@ApiModelProperty(value="交易类型",hidden=true)
    private String p1_bizType;
	
	@ApiModelProperty(value="商户订单号")
    private String p2_orderId;
	
	@ApiModelProperty(value="商户编号",hidden=true)
    private String p3_customerNumber;
	
	@ApiModelProperty(value="签名方式",hidden=true)
    @SignExclude
    private String signatureType;

	public String getP1_bizType() {
		return p1_bizType;
	}

	public void setP1_bizType(String p1_bizType) {
		this.p1_bizType = p1_bizType;
	}

	public String getP2_orderId() {
		return p2_orderId;
	}

	public void setP2_orderId(String p2_orderId) {
		this.p2_orderId = p2_orderId;
	}

	public String getP3_customerNumber() {
		return p3_customerNumber;
	}

	public void setP3_customerNumber(String p3_customerNumber) {
		this.p3_customerNumber = p3_customerNumber;
	}

	public String getSignatureType() {
		return signatureType;
	}

	public void setSignatureType(String signatureType) {
		this.signatureType = signatureType;
	}

    
}
