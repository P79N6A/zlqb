package com.nyd.order.model;



import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nyd.order.model.common.PageCommon;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by zlq on 2019/07/17
 * 订单审核返回对象
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class OrderCheckVo  extends PageCommon {
	
	//private static final long serialVersionUID = 1L;
	//序号
    //private String seqNumber;
	//订单id
	@ApiModelProperty(value = "贷款编号")
	private String orderNo;
	
	@ApiModelProperty(value = "用户id")
	private String userId;
	
    //信审人员
	@ApiModelProperty(value = "信审人员")
    private String assignName;
    //借款期次
	@ApiModelProperty(value = "借款期次")
    private String  loanNumber;
    //客户姓名
	@ApiModelProperty(value = "客户姓名")
    private String userName;
    //手机号码
	@ApiModelProperty(value = "手机号码")
    private String mobile;
    //注册渠道
	@ApiModelProperty(value = "注册渠道")
    private String channel;
    //申请时间
	@ApiModelProperty(value = "申请时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loanTime;
    //放款产品
	@ApiModelProperty(value = "放款产品")
    private String appName;
    //分配时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty(value = "分配时间")
    private Date assignTime;
   
	@ApiModelProperty(value = "分配id")
	private String assignId;
	
	@ApiModelProperty(value = "订单状态")
	private Integer orderStatus;
	@ApiModelProperty(value="审核状态 1待审核 2审核通过 3审核拒绝")
	private Integer auditStatus;
	
}
