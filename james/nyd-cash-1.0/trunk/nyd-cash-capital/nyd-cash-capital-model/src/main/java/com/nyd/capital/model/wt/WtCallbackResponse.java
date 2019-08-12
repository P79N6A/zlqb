package com.nyd.capital.model.wt;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2018/4/26
 **/
@Data
public class WtCallbackResponse implements Serializable{
    private String orderNo;
    private String withDrawNo;//提现流水
    private String recordNo;//记账流水
    private String remitTime;
    private BigDecimal amount;

    /**
     * 0是成功 1为失败
     */
    private String status;
}
