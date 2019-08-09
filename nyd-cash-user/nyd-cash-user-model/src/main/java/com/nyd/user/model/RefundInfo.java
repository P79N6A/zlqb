package com.nyd.user.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import java.util.Date;
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
public class RefundInfo extends BaseInfo implements Serializable{
	
	private String userId;
	
	private String userName;
	
	private String accountNumber;
	
	private String refundNo;
	
	private String orderNo;
	
	private BigDecimal refundAmonut;
	//999初始化状态（申请未提交）1000处理中1001审核通过1002审核拒绝
	private Integer requestStatus;
	
	private String updateBy;
	
	private String appList;
	
	private Date createTime;
	
	private Integer pageSize;
	
	private Integer pageNum;
	
	private String  reason;
	
	private String appName;
	
	private  List<RefundAppInfo> appReasonList;

}
