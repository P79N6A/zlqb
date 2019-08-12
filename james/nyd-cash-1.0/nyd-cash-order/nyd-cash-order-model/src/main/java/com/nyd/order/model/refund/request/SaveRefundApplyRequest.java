package com.nyd.order.model.refund.request;

import java.io.Serializable;

import lombok.Data;

/**
 * 添加退款申请记录
 * @author 
 *
 */
@Data
public class SaveRefundApplyRequest implements Serializable {
	
	private static final long serialVersionUID = 7067284048294496570L;

    private String id;
    private String orderNo;
    private String refundDate;
    private String realRefundDate;
    private String custId;
    private String phone;
    private String name;
    private String refundAmount;
    private String realRefundAmount;
    private String userId;
    private String userName;
    private String serialNum;
    private String applyRemarks;
    private String remarks;
    private String status;
    private String createTime;
    private String updateTime;
    private String handleUserId;
    private String handleUserName;
	
}
