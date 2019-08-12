package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryOrderVo implements Serializable{
	  private String P1_bizType;
	  private String P2_customerNumber;
	  private String P3_orderId;
	  private String P4_timestamp;
	  private String sign;

}
