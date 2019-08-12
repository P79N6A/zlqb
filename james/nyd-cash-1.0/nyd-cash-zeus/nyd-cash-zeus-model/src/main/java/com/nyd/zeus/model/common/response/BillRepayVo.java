package com.nyd.zeus.model.common.response;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class BillRepayVo implements Serializable{
	
	private String id;			//标识
	private String billNo;			//账单编号
	private String orderNo;			//订单号
	private String userId;			//用户id
	private String outOrderNumber;			//对外订单号
	private String repayAmount;			//还款金额
	private String payType;			//还款方式(1:主动还款,2:跑批还款)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private java.util.Date createTime;			//交易时间
	private java.util.Date endTime;			//交易结束时间
	private String resultCode;			//交易状态(1:成功,2:失败,3:处理中)
	private String remark;			//备注

}
