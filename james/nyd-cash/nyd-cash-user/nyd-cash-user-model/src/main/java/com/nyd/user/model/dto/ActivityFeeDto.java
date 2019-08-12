package com.nyd.user.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityFeeDto implements Serializable{
	
	 private String accountNumber;//手机号
	  
	  private String userId;//用户id
	  
	  private String activityType;//活动类型
	  
	  private String activityMoney;//活动金额
	  
	  private String activityLimitTime;//活动到期时间
	  
	  private String marks;//备用字段
	  
	  private String appName;
	  
	  private String deleteFlag;
	  
	  private String createTime;
	  
	  private String updateTime;
	  
	  private String updateBy;

}
