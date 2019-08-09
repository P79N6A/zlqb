package com.nyd.order.model.refund.vo;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@JsonIgnoreProperties
@Data
@ApiModel(value = "退款处理记录合计查询", description = "退款处理记录合计查询")
public class SumAmountVo implements Serializable {

	private static final long serialVersionUID = -4593977279315464727L;
	
	@ApiModelProperty(value = "申请退款金额")
    private String refundAmount;

	@ApiModelProperty(value = "客户姓名")
    private String realRefundAmount;

}
