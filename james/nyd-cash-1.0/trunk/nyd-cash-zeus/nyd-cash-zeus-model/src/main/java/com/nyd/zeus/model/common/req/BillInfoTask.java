package com.nyd.zeus.model.common.req;

import java.io.Serializable;

import lombok.Data;

@Data
public class BillInfoTask implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String startTime;//开始时间
	
	private String endTime;//结束时间

	

}
