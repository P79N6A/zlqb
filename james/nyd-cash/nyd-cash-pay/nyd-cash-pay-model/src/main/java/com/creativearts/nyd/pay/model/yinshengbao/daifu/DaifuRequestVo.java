package com.creativearts.nyd.pay.model.yinshengbao.daifu;

import com.creativearts.nyd.pay.model.annotation.RequireField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2018/2/5
 **/
@Data
public class DaifuRequestVo {

    @RequireField
    private String accountId;//商户编号
    @RequireField
    private String name;
    @RequireField
    private String cardNo;//银行卡号
    @RequireField
    private String orderId;//订单号
    @RequireField
    private String purpose;//意图

    @RequireField
    private BigDecimal amount;

    private String responseUrl;
}
