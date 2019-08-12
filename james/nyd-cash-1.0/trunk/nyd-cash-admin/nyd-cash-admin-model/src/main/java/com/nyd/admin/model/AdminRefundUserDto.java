package com.nyd.admin.model;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class AdminRefundUserDto implements  Serializable{
	
	private String accountNumber;
	
	private String userName;
	
	private BigDecimal refundAmonut;//退款金额
	
	private int ifRemove;//是否移除
	
	 //起始页
    private Integer pageNum;

    //每页和数
    private Integer pageSize;
	

}
