package com.nyd.zeus.service.batch;

import lombok.Data;

import java.util.Date;

/**
 * Cong Yuxiang
 * 2017/11/29
 **/
public @Data class ReconciliationWsmPo {
    private String orderNo;
    private Double amount;
    private Date contractStartTime;
    private Date contractEndTime;
    private Date reconciliationDay;
    private String fundCode;
    private String remitStatus;
    private String flag;
    private String resultCode;
    private Double amountOwn;
}
