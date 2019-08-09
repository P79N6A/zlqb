package com.nyd.batch.model;

import com.nyd.batch.model.annotation.ExportConfig;
import lombok.Data;

/**
 * 昨天催收的状态
 * Cong Yuxiang
 * 2018/1/9
 **/
@Data
public class OverdueStatus {
    @ExportConfig("姓名")
    private String name;
    @ExportConfig("身份证号")
    private String idCard;
    @ExportConfig("逾期阶段")
    private String period;
    @ExportConfig("手机号")
    private String mobile;
    @ExportConfig("已收金额")
    private String receiveAmount;
    @ExportConfig("未收金额")
    private String notRecvAmount;
    @ExportConfig("还款时间")
    private String repayTime;
    @ExportConfig("是否结清")
    private String done;
    @ExportConfig("结清时间")
    private String settleTime;
    @ExportConfig("账单号")
    private String billNo;
    @ExportConfig("订单号")
    private String orderNo;

}
