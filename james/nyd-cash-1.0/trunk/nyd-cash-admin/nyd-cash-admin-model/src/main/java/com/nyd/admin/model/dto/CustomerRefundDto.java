package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: wucx
 * @Date: 2018/11/2 18:02
 */
@Data
public class CustomerRefundDto implements Serializable {

    //用户编号
    private String userId;
    //申请单号
    private String requestNo;
    //姓名
    private String userName;
    //手机号
    private String accountNumber;
    //操作人
    private String operatorPerson;
    //退款金额
    private BigDecimal refundAmount;
    //审核状态
    private Integer status;
    //审核描述
    private String remark;
    //标记 1:客服操作  2:财务操作
    private Integer flag;
    //表示审核结果 1:通过 2:拒绝
    private Integer auditFlag;


}
