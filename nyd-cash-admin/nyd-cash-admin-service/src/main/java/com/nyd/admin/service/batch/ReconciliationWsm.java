package com.nyd.admin.service.batch;

import lombok.Data;

/**
 * Cong Yuxiang
 * 2017/11/25
 **/
public @Data
class ReconciliationWsm {
    private String orderNo;
    private String amount;
    private String contractStartTime;
    private String contractEndTime;
    private String reconciliationDay;
    private String fundCode;
    private String remitStatus;
    private String flag;
    private String resultCode;
    private String amountOwn;
}
