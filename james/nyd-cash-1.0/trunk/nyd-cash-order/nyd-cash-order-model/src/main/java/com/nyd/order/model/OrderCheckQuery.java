package com.nyd.order.model;


import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

import com.nyd.order.model.common.PageCommon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by zlq on 2019/07/17
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderCheckQuery extends PageCommon{
	
	//客户姓名
	@ApiModelProperty(value="客户姓名")
    private String userName;
    //手机号码
	@ApiModelProperty(value="手机号码")
    private String accountNumber;
    //信审人员
	@ApiModelProperty(value="信审人员")
    private String checkPersonnel;
    //注册渠道
	@ApiModelProperty(value="注册渠道")
    private String appName;
    
	@ApiModelProperty(value="分配开始时间")
    private String beginTime;
    
	@ApiModelProperty(value="分配结束时间")
    private String endTime;
	//
    private String mobile;
    
 
}
