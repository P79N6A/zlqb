package com.nyd.zeus.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRiskExcludeInfoVo implements Serializable{
	private static final long serialVersionUID = -706629751551095342L;
	@ApiModelProperty("贷款编号")
	private String orderNo;
}
