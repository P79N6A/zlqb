package com.nyd.capital.model.dld;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderReportParams implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7445983194215488196L;
	private String pageIndex;
	private String startDate;
	private String endDate;
}
