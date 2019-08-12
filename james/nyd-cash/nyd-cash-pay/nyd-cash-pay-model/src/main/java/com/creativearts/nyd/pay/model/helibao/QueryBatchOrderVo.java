package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryBatchOrderVo implements Serializable{
	private String P1_bizType;
	private String P2_batchNo;
	private String P3_customerNumber;
	private String P4_orderId;
	private String sign;


}
