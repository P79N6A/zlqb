package com.nyd.product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.data.annotation.Id;

import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_product_zzl")
public class ProductZzl {

      @Id
      private String id;
	  private String productCode;//产品期数
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
