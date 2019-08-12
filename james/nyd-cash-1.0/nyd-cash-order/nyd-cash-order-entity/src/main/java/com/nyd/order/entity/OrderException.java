package com.nyd.order.entity;

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
 * Created by zhujx on 2017/11/8.
 * 订单
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_order_exception")
public class OrderException implements Serializable {
    //主键id
    @Id
    private Long id;
    //订单编号
    private String orderNo;
    //用户ID
    private String userId;
    //手机号
    private String accountNumber;
    //真实姓名
    private String realName;
    //借款金额
    private BigDecimal loanAmount;
    //用户银行账户
    private String bankAccount;
    //银行名称
    private String bankName;
    //异常订单状态
    private Integer orderStatus;
    //审核结果
    private String auditStatus;
    //资金渠道
    private String fundCode;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
    /**马甲名称*/
    private String appName;
}
