package com.nyd.capital.model.pocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author liuqiu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PocketCreateOrderLendPayDto {
    /**
     * 用户信息
     */
    private PocketUserBase2 userBase;
    /**
     * 订单信息
     */
    private PocketOrderBase2 orderBase;
//    /**
//     * 总还款计划信息(可不传)
//     */
//    private String repaymentBase;
//    /**
//     * 还款计划详细信息(可不传)
//     */
//    private String periodBase;
//    /**
//     * 受托支付商户信息(可不传)
//     */
//    private String receiptBase;
//    /**
//     * 关联订单信息(可不传)
//     */
//    private String orderGroupBase;
//    /**
//     * 合同展示利率等(可不传)
//     */
//    private String other;
}
