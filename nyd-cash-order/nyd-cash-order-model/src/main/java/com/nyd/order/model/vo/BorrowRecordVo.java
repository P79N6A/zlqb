package com.nyd.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by Dengw on 2017/11/15
 * 借款记录返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BorrowRecordVo {
    //订单编号
    private String orderNo;
    //借款金额
    private BigDecimal loanAmount;
    //借款时长
    private Integer borrowTime;
    //订单状态
    private Integer orderStatus;
    //借款时间
    private String loanTime;
    //是否签约
    private Integer ifSign;
    
    //是否为ymt的订单 非ymt 0 是ymt 1
    private Integer ifYmt;
    
    private String reportUrl;
    
    private Integer reportFlag;
    
    private String reportKey;
}
