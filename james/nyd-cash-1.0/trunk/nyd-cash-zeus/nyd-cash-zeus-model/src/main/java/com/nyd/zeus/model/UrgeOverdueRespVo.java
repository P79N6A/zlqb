package com.nyd.zeus.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by zhujx on 2017/11/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UrgeOverdueRespVo implements Serializable {

	
	@ApiModelProperty("贷后专员")
	private String receiveUserName;
	@ApiModelProperty("客户姓名")
	private String userName;
	@ApiModelProperty("手机号码")
	private String userMobile;
	@ApiModelProperty("借款日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd" )
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date applyTime;
	@ApiModelProperty("放款产品")
	private String productName;
	@ApiModelProperty("注册渠道")
	private String source;
	@ApiModelProperty("借款期次")
	private Integer periods;
	@ApiModelProperty("借款金额")
	private BigDecimal repayPrinciple;
	@ApiModelProperty("已还金额")
	private BigDecimal alreadyRepayAmount;
	@ApiModelProperty("剩余应还金额")
	private BigDecimal waitRepayAmount;
	@ApiModelProperty("应还日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd" )
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date promiseRepaymentDate;
	@ApiModelProperty("贷款编号")
	private String orderNo;
	@ApiModelProperty("用户id")
	private String userId;
	@ApiModelProperty("逾期天数")
	private Integer overdueDays;
	@ApiModelProperty("分配时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd" )
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date createTime;
	@ApiModelProperty("订单状态：1:待催收 2催收中 3 承若还款 4已结清")
	private String urgeStatusName;
	@ApiModelProperty(hidden=true)
	private String urgeStatus;
	
	
}
