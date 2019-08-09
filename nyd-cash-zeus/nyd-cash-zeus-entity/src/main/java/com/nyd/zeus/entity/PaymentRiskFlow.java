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
@Table(name="t_payment_risk_flow")
public class PaymentRiskFlow {

    //主键id
    @Id
    private Long id;
     
    // 初始状态  S：成功；F：失败 P: 处理中
    private String status;
    // 请求时间
    private Date requestTime;
    // 响应时间
    private Date responseTime;
    // 请求数据
    private String requestText;
    // 响应数据
    private String responseText;
    
    private String seriNo;
    
    private String orderNo;
    
    private BigDecimal money;
    

}
