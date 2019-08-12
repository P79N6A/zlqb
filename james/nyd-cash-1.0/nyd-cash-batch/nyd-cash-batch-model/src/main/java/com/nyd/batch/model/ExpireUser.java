package com.nyd.batch.model;

import com.nyd.batch.model.annotation.ExportConfig;
import lombok.Data;

/**
 * Cong Yuxiang
 * 2018/1/10
 **/
@Data
public class ExpireUser {
    @ExportConfig("姓名")
    private String name;
    @ExportConfig("手机号")
    private String mobile;
    @ExportConfig("借款金额")
    private String borrowAmount;
    @ExportConfig("未还金额")
    private String notReceiveAmount;
    @ExportConfig("已还金额")
    private String receiveAmount;
    @ExportConfig("应还款时间")
    private String promiseTime;
    @ExportConfig("身份证号")
    private String idCard;
}
