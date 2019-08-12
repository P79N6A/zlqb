package com.creativearts.nyd.pay.model.yinshengbao;

import lombok.Data;

/**
 * 代扣请求对象
 */

@Data
public class DaiKouRequest {

    private String purpose;
    private String phoneNo;
    private String subContractId;
    private String accountId;//系统自动填充
    private String orderId;
    private String amount;
    private String responseUrl;//系统自动填充
    private String mac;//系统自动填充

}
