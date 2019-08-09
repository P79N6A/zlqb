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
public class PaymentRiskRecordExtendVo implements Serializable{

    private Long id;
    
    private String orderNo;
    
    private String custName;
    
    private String appName;
    private String cellPhone;
    private String phone;
    
    private String smsType;
}
