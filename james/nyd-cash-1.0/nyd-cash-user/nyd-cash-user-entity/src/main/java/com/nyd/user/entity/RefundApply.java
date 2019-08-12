package com.nyd.user.entity;

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
@Table(name="t_refund_apply")
public class RefundApply implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    private Long id;
    //申请单号
    private String requestNo;
    //申请类型ref为退款申请，comp为投诉
    private String typeCode;
    //快递单号
    private String trackNo;
    //用户ID
    private String userId;
    //用户姓名
    private String userName;
    //身份证号
    private String idCard;
    //身份证号
    private BigDecimal refundAmount;
    //审核状态
    private Integer status;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
    //投诉内容
    private String message;
    //电话
    private String phone;
}
