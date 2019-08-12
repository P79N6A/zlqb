package com.creativearts.nyd.collectionPay.model.zzl.changjie.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 畅捷代付查询（商户 -> 客户）
 * @author admin
 *
 */
@ApiModel(description="畅捷代付查询（划扣钱给客户）")
public class ChangJieQueryMerchantVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="原交易请求流水")
	private String orderno; //原交易请求流水
	
	@ApiModelProperty(value="渠道")
	private String code; //渠道

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
}
