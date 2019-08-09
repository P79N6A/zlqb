package com.nyd.zeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nyd.zeus.model.common.PageCommon;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhujx on 2017/11/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReadyDistributionVo implements Serializable{
     
    //  客户姓名
	@ApiModelProperty("客户姓名")
    private String userName;
	// 手机号码
	@ApiModelProperty("订单编号")
	private String orderNo;
	
	@ApiModelProperty("订单用户id")
	private String userId;
    // 手机号码
	@ApiModelProperty("手机号码")
    private String userMobile;
    // 注册渠道
	@ApiModelProperty("申请日期")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date applyTime;
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	@ApiModelProperty("放款日期")
	private Date loanTime;
    // 产品名称D
	@ApiModelProperty("放款产品")
    private String productName;
	
	@ApiModelProperty("注册渠道")
	private String source;
    // 贷款编号
	@ApiModelProperty("借款期次")
    private Integer loanNum;
	
	@ApiModelProperty("放款金额")
	private Double repayPrinciple;
	
	@ApiModelProperty("应还日期")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date promiseRepaymentDate;
	
	@ApiModelProperty("距还款日")
	private Integer lessDays;
	
	@ApiModelProperty("信审人员")
	private String creditTrialUserName;
	
	@ApiModelProperty("应还本息")
	private BigDecimal curRepayAmount;
}
