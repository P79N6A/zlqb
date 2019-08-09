package com.nyd.order.model.refund.vo;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

@JsonIgnoreProperties
@ApiModel(value = "确认退款出参", description = "确认退款出参")
public class ConfirmRefundVo implements Serializable {

	private static final long serialVersionUID = 6459199590184131096L;

}
