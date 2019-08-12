package com.zhiwang.zfm.common.request.refund;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties
@ApiModel(value = "确认退款入参", description = "确认退款入参")
public class ConfirmRefundRequest implements Serializable {

	private static final long serialVersionUID = 6241271439103375174L;
	
	@ApiModelProperty(value = "退款编号")
    private String refundId;

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

}
