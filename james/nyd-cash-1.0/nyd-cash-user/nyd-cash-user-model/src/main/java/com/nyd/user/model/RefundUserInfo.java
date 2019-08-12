package com.nyd.user.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author zhangdk
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefundUserInfo extends BaseInfo implements Serializable {
	
	private String userName;
	
	private String accountNumber;
	
	private String orderNo;
	
	private BigDecimal refundAmonut;
	
	private int ifRemove;
	
	private String updateBy;

}
