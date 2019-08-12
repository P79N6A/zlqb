package com.nyd.zeus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;


import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/18.
 * 账单表
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_payment_risk_record")
public class PaymentRiskRecord {

    //主键id
    @Id
    private Long id;
     
    // 初始状态  0：正常（扣款中）；1：已经扣款结束 -1 处理中
    private Integer status;
    // 创建时间
    private String requestTime;
    // 更新时间
    private String updateTime;
    // 请求数据
    private String requestText;
    // 响应数据
    private String responseText;
    // 应还金额
    private BigDecimal shouldMoney;
    // 剩余金额
    private BigDecimal remainMoney;
    // 订单编号
    private String orderNo;
    // 流水号
    private String seriNo;
    // 风控结果 1 成功 0 失败
    private Integer riskStatus;
    // 备份金额
    private BigDecimal backupMoney;
    // 风控时间
    private String riskTime;
    // 通知状态 0 未通知 1 通知
    private Integer noticeStatus;
    

}
