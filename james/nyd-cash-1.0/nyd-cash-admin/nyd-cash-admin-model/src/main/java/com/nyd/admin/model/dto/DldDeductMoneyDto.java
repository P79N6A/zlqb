package com.nyd.admin.model.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DldDeductMoneyDto implements Serializable{
	
	private String payOrderNo;
	private Double withholdAmount;
	private String source;

}
