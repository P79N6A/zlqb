package com.nyd.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: wucx
 * @Date: 2018/11/2 19:07
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_refund_audit_log")
public class RefundAuditLog {

    //主键id
    @Id
    private Long id;
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
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //最后修改人
    private String updateBy;
    //审核描述
    private String remark;
}
