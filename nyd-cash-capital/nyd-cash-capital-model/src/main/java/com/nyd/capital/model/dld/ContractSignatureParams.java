package com.nyd.capital.model.dld;

import java.io.Serializable;

import lombok.Data;

@Data
public class ContractSignatureParams implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -494568679264201346L;
	private String loanOrderNo;
	private String contractNumber;
}
