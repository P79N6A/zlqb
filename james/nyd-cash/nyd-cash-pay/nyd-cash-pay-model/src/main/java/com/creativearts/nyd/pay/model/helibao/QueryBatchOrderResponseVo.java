package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryBatchOrderResponseVo implements Serializable{
	private String rt1_bizType;//交易类型
	private String rt2_retCode;//返回码
	private String rt3_retMsg;//返回信息
	private String rt4_customerNumber;//商户编号
	private String rt5_batchNo;//批次号
	private String rt6_detail;//明细 订单号、金额、完成时间、状态
	private String sign;


}
