package com.nyd.zeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by zhujx on 2017/11/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRiskRequestCommon implements Serializable{
     
	// 渠道code
	private String channelCode;
	
	// 扣款金额
	private BigDecimal money;
	
	// 渠道code
	private String orderNo;
	
	
    // 银行卡号
    private String channelJson;
    
    private Date riskTime;
    
    private Integer riskStatus;
    
    
    
}
