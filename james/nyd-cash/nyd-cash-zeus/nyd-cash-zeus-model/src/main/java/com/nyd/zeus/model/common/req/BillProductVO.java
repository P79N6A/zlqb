package com.nyd.zeus.model.common.req;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class BillProductVO implements Serializable{
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
