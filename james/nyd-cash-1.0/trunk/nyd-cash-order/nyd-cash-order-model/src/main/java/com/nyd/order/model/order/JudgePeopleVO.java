package com.nyd.order.model.order;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@JsonIgnoreProperties
@ApiModel(value = "JudgePeopleVO", description = "分配订单参数")
@Data
public class JudgePeopleVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "id")
	private String id ;
	@ApiModelProperty(value = "用户名称")
	private String name;
	
}
