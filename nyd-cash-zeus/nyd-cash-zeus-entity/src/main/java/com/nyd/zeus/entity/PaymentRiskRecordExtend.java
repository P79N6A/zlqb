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
@Table(name="t_payment_risk_record_extend")
public class PaymentRiskRecordExtend {

    //主键id
    @Id
    private Long id;
     
    private String orderNo;
    
    private String custName;
    
    private String appName;
    private String cellPhone;
    private String phone;
    
    private String smsType;
    

}
