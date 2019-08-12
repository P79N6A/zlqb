package com.creativearts.nyd.collectionPay.model.zzl.helibao.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by heli50 on 2017/4/14.
 */

@ApiModel(description="商户用户查询")
public class MerchantUserQueryVo {
	
	@ApiModelProperty(value="交易类型",hidden=true)
    private String p1_bizType;
	
	@ApiModelProperty(value="商户编号",hidden=true)
    private String p2_customerNumber;
	
	@ApiModelProperty(value="商户订单号")
    private String p3_orderId;
	
	@ApiModelProperty(value="用户编号")
    private String p4_userId;
	
	@ApiModelProperty(value="时间戳",hidden=true)
    private String p5_timestamp;

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

	public String getP4_userId() {
		return p4_userId;
	}

	public void setP4_userId(String p4_userId) {
		this.p4_userId = p4_userId;
	}

	public String getP5_timestamp() {
		return p5_timestamp;
	}

	public void setP5_timestamp(String p5_timestamp) {
		this.p5_timestamp = p5_timestamp;
	}
	
	

    
    
}
