package com.nyd.capital.model.pocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author liuqiu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PocketPushAssetRepaymentPeriodDto {

    /**
     * 订单号
     */
    private String outTradeNo;
    /**
     * 总还款计划信息
     */
    private PocketRepaymentBase repaymentBase;
    /**
     * 还款计划详细信息
     */
    private List<PocketPeriodBase> periodBase;
}
