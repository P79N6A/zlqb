package com.nyd.capital.model.dld;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoanOrderQueryParams implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5666482077872103035L;
	private String merOrderNo;
}
