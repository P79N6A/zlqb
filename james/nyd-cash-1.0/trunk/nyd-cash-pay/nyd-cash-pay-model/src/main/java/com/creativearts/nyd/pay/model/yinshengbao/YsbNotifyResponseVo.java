package com.creativearts.nyd.pay.model.yinshengbao;

import lombok.Data;

import java.io.Serializable;

/**
 * 银生宝代扣通知返回对象
 */
@Data
public class YsbNotifyResponseVo implements Serializable {
    private String result_code;
    private String result_msg;
    private String amount;
    private String orderId;
    private String mac;
}
