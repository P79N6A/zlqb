package com.nyd.order.model;

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
public class OrderExceptionInfo implements Serializable {
    //订单编号
    private String orderNo;
    //用户ID
    private String userId;
    //借款金额
    private BigDecimal loanAmount;
  //手机号
    private String accountNumber;
    //真实姓名
    private String realName;
    //用户银行账户
    private String bankAccount;
    //银行名称
    private String bankName;
    //异常订单状态
    private Integer orderStatus;
    //审核结果
    private Integer auditStatus;
    //资金渠道
    private String fundCode;
    //是否已删除
    private Integer deleteFlag;
    //打款失败时间
    private Date failTime;
    //打款失败时间
    private String failTimeStr;
    //修改人
    private String updateBy;
    //失败原因
    private String payFailReason;
    /**马甲名称*/
    private String appName;
}
