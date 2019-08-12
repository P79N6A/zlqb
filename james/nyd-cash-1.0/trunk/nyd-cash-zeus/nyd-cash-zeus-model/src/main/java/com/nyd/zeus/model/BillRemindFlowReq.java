package com.nyd.zeus.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BillRemindFlowReq implements Serializable{

	@ApiModelProperty("订单编号")
    private String orderNo;
	
	@ApiModelProperty("提醒内容")
    private String remindMsg;

}
