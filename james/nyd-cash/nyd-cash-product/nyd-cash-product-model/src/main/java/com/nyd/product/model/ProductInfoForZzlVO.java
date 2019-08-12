package com.nyd.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Dengw on 17/11/6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductInfoForZzlVO implements Serializable {
	
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	  private String id;
	  private String productCode;//产品
	  private String productName;//产品名称
	  private String period;//产品金额
	  private String money;//借款金额
	  private Date createTime;
	  private BigDecimal repayInterest;//'应还利息',
	  private BigDecimal managerFee;//'平台服务费',
	  private int deleteFlag;// '是否已删除 0：正常；1：已删除',
	  private String appName;//'马甲包名称（BI统计用字段）',
	  private BigDecimal penaltyRate;//'罚息率',
	  private BigDecimal lateFee;//'滞纳金',
}
