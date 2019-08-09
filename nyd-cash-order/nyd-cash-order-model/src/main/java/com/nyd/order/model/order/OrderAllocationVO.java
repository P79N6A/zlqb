package com.nyd.order.model.order;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@JsonIgnoreProperties
@ApiModel(value = "orderAllocation", description = "分配切信审订单")
@Data
public class OrderAllocationVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "信审人员Id") 
	private String UserId;//
	@ApiModelProperty(value = "信审人员名称")
	private String userName;
	@ApiModelProperty(value = "订单Id")
	private List<String> orderNo;// 

}
