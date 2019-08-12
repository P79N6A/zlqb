package com.nyd.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

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
public class RefundApplyDto{
    //用户ID
    private String userId;
    //申请单号
    private String requestNo;
    //申请类型ref为退款申请，comp为投诉
    private String typeCode;
    //用户姓名
    private String userName;
    //身份证号
    private String idCard;
    //身份证号
    private String trackNo;
    //添加时间
    private String createTime;
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
    //起始页
    private Integer pageNum;

    //每页查询数
    private Integer pageSize;
}
