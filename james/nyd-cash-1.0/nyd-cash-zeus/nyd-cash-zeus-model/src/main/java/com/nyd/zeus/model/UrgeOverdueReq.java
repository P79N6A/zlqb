package com.nyd.zeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

import com.dianping.cat.message.Trace;
import com.nyd.zeus.model.common.PageCommon;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhujx on 2017/11/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UrgeOverdueReq extends PageCommon implements Serializable{
     
    //  客户姓名
	@ApiModelProperty("客户姓名")
    private String custName;
    // 手机号码
	@ApiModelProperty("手机号码")
    private String mobile;
    // 注册渠道
	@ApiModelProperty("注册渠道")
    private String source;
    // 产品名称
	@ApiModelProperty("产品名称")
    private String productName;
    // 贷款编号
	@ApiModelProperty("贷款编号")
    private String loanNo;
	
	 // 应还日期开始
	@ApiModelProperty("应还日期开始")
	private String promiseRepaymentStart;
	
	 // 应还日期结束
	@ApiModelProperty("应还日期开始")
	private String promiseRepaymentEnd;
	
	@ApiModelProperty("贷后专员")
	private String receiveUserName;
	
	@ApiModelProperty("订单状态：1:待催收 2催收中 3 承若还款 4已结清")
	private String urgeStatus;
	
	@ApiModelProperty("分配时间开始")
	private String createTimeStart;
	
	@ApiModelProperty("分配时间结束")
	private String createTimeEnd;
    
	@ApiModelProperty(hidden = true)
	private String userId;
	@ApiModelProperty(hidden = true)
	private String payStatus;//1贷中2贷后
}
