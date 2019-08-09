package com.nyd.user.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ActivityCashDto implements Serializable{
	
	 private List<ActivityFeeDto> list;
	 
	 private BigDecimal totalAmount;//总金额 
	
	

}
