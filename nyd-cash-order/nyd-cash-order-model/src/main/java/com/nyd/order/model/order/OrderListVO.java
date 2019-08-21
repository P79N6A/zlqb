package com.nyd.order.model.order;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nyd.order.model.common.PageCommon;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@JsonIgnoreProperties
@ApiModel(value = "orderList", description = "未分配切信贷的订单列表")
@Data
public class OrderListVO extends PageCommon{
	 /**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	//订单id
	@ApiModelProperty(value = "订单编号")
    private String orderNo;
	@ApiModelProperty(value = "客户ID")
	private String userId;
	@ApiModelProperty(value = "客户姓名")
    private String userName;
	@ApiModelProperty(value = "手机号码")
    private String phone;//手机号码
	@ApiModelProperty(value = "借款批次")
    private Integer borrowPeriods;// 借款批次
    /**
     * 注册渠道
     */
	@ApiModelProperty(value = "注册渠道")
    private String channel;
	@ApiModelProperty(value = "申请时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date regTime;//申请时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty(value = "进入时间")
    private Date inTime;//进入时间
	@ApiModelProperty(value = "放款产品")
    private String loanProduct;//放款产品
	@ApiModelProperty(value = "分配状态 0为已分配，1为未分配")
	private Integer isExistAssignId;
	@ApiModelProperty(value = "信审人员（分配接收人）")
	private String assignName;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty(value = "分配时间")
	private Date assignTime;
	private String assignId;
}
