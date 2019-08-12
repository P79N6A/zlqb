package com.nyd.capital.model.dld;

import java.io.Serializable;

import lombok.Data;

@Data
public class UploadLoanProParams implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -387362969400191596L;
	private String loanOrderNo;
	private String repaymentProtocols;
}
