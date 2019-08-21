package com.nyd.zeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;


/**
 * xingsheng on 2017/11/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRiskRequestLiandong implements Serializable{
     
	//单位分
	private String amount;

	//绑卡返回字段
	private String usr_pay_agreement_id;
	
	//绑卡返回字段
	private String usr_busi_agreement_id;
	
	// 流水号
	private String order_no;
	
	// 请求时间yyyyMMDD
	private String mer_date;

    
    
    
}
