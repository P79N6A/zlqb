package com.nyd.zeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

/**
 * Created by zhujx on 2017/11/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRiskRecordVo implements Serializable{

    //主键id
    private Long id;
     
    // 初始状态  0：正常（扣款中）；1：已经扣款结束 -1 处理中
    private Integer status;
    // 创建时间
    private String requestTime;
    // 更新时间
    private String updateTime;
    // 风控时间
    private String riskTime;
    // 请求数据
    private String requestText;
    // 响应数据
    private String responseText;
    // 应还金额
    private BigDecimal shouldMoney;
    // 剩余金额
    private BigDecimal remainMoney;
    // 最近一次扣款（异步交易用）
    private BigDecimal recentMoney;
    // 当日失败次数（异步交易用）
    private Integer failNum;
    // 处理日期（异步交易用）
    private String dealTime;
    // 订单编号
    private String orderNo;
    // 流水号
    private String seriNo;
    // 风控结果 1 成功 0 失败
    private Integer riskStatus;
    // 备份金额
    private BigDecimal backupMoney;
    // 通知状态 0 未通知 1 通知
    private Integer noticeStatus;
}
