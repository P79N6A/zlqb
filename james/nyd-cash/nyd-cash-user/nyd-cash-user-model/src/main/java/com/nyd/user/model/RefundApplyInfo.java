package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款申请
 * @author zhangdk
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefundApplyInfo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String userId;
    //申请单号
    private String requestNo;
    //申请类型ref为退款申请，comp为投诉
    private String typeCode;
    //用户姓名
    private String userName;
    //身份证号
    private String idCard;
    //快递单号
    private String trackNo;
    //添加时间
    private Date createTime;
    //退款金额
    private BigDecimal refundAmount;
    //审核状态
    private Integer status;
    //修改人
    private String updateBy;
    //投诉内容
    private String message;
    //电话
    private String phone;
    
    private String deviceId;
    private String accountNumber;
}
