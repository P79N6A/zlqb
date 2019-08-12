package com.nyd.order.model.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class ProductOrderVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;
	
	private String orderNo;
	
	private String productCode;
	
	

}
