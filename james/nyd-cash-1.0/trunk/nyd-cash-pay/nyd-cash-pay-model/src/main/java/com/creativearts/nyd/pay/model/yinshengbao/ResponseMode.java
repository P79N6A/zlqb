package com.creativearts.nyd.pay.model.yinshengbao;

import lombok.Data;

@Data
public class ResponseMode {
    private String result_code;
    private String result_msg;
    private String amount;
    private String orderId;
    private String mac;
    private String desc;
    private String status;
    private String subContractId;
}
