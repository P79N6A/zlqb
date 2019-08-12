package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/12/7
 **/
@Data
public class ReconResultDto implements Serializable{
    private String orderNo;
    private Double amount;
    //合同起始日
    private String contractStartTime;
    //合同截止日
    private String contractEndTime;

    private String remitStatus;
    private String fundName;
    private String reconciliationDay;
    private Double amountOwn;
    private String resultStatus;
}
