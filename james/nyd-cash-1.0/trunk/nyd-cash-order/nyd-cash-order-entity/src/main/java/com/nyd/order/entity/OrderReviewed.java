package com.nyd.order.entity;

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
@Table(name="t_order_reviewed")
public class OrderReviewed {
	
	  private String id;//'标识',
	  private String userId;//'用户id',
	  private String orderNo;//'订单号',
	  private String reviewedId;//'审核人id',
	  private String reviewedName;//'审核人姓名',
	  private Date reviewedTime;//'审核时间',
	  private String remark;//'备注',

}
