package com.nyd.order.model.refund.vo;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 退款处理列表查询出参
 *
 */
@JsonIgnoreProperties
@Data
@ApiModel(value = "退款处理列表查询出参", description = "退款处理列表查询出参")
public class RefundListVo implements Serializable {
	
	private static final long serialVersionUID = 5141967892224455323L;

	@ApiModelProperty(value = "主键标识")
    private String id;

	@ApiModelProperty(value = "退款时间")
    private String refundDate;

	@ApiModelProperty(value = "退款结果")
    private String status;

	@ApiModelProperty(value = "客服姓名")
    private String customerName;
    
	@ApiModelProperty(value = "系统用户id")
    private String sysUserId;

	@ApiModelProperty(value = "申请退款金额")
    private String refundAmount;

	@ApiModelProperty(value = "用户id")
    private String userId;
	
	@ApiModelProperty(value = "客户姓名")
    private String name;

	@ApiModelProperty(value = "手机号码")
    private String phone;

	@ApiModelProperty(value = "贷款编号")
    private String orderNo;
	
	@ApiModelProperty(value = "订单状态")
    private String OrderStatus;

	@ApiModelProperty(value = "当前页")
    private String pageNo;

	@ApiModelProperty(value = "每页条数")
    private String pageSize;

}
