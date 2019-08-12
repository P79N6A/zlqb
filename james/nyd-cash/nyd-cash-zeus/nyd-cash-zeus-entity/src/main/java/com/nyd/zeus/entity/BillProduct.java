package com.nyd.zeus.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_bill_product")
public class BillProduct {
	
	 //主键id
    @Id
    private String id;
	private String productCode;//用户Id，逻辑主键',
	private String productName;//产品名称
	private String userId;//'用户Id，逻辑主键',
	private String orderNo;//'订单编号',
	private String period;//'产品期数',
	private String money;//'产品金额',
	private Date createTime;//'添加时间',
	private BigDecimal repayInterest;//'应还利息',
	private BigDecimal managerFee;//平台服务费',
	private int deleteFlag;//'是否已删除 0：正常；1：已删除',
	private String appName;//'马甲包名称（BI统计用字段）',
	private BigDecimal penaltyRate;//'罚息率',
	private BigDecimal lateFee;//'滞纳金',
	

}
