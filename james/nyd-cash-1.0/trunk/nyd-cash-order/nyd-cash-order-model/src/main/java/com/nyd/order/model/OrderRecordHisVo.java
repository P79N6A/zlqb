package com.nyd.order.model;


import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by zlq on 2019/07/18
 * 历史申请记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderRecordHisVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//订单ID
	private String orderNo;
	//申请日期
	private String applyDate;
	//申请结果
	private String applyResult;
	//审批人
	private String approvingOfficer;
	//审核意见
	private String applicationOpinion;
	//逾期天数
	private String overdueDays;
	//到期时间
	private String endTime;
	//实际还款时间
	private String actualRepayTime;

}
