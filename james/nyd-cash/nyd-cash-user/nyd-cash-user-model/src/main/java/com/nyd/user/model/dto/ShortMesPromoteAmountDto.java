package com.nyd.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShortMesPromoteAmountDto {
	
	private String idCardNo;//身份证号
	
	private String limitAmount;//提升额度
	
	private String batchNo;
	
	private String limitStatus;
	
	private String limitType;
	
}
