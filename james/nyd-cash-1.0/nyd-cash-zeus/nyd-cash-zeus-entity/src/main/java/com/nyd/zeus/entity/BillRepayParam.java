package com.nyd.zeus.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_bill_repay_param")
public class BillRepayParam {
	
	private String id;	private Integer referenceHour;//'还款时间标准',
	private BigDecimal referenceMoney;//'剩余应还金额
	private BigDecimal latefeeMax;//'滞纳金最大值',
	
	
}
