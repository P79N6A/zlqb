package com.nyd.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * Cong Yuxiang
 * 2017/12/7
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReconResult {
    private Long id;
    private String orderNo;
    private Double amount;
    //合同起始日
    private Date contractStartTime;
    //合同截止日
    private Date contractEndTime;

    private String remitStatus;
    private String fundCode;
    private Date reconciliationDay;
    private Double amountOwn;
    private String resultCode;

    private Date createTime;
    private Date updateTime;

    private Date startDate;
    private Date endDate;
}
