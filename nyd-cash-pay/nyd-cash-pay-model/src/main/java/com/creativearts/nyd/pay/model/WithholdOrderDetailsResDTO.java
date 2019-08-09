package com.creativearts.nyd.pay.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author shaoqing.liu
 * @date 2018/11/26 11:29
 */
@Data
@ToString
public class WithholdOrderDetailsResDTO {

    /**流水号**/
    private String orderNo;
    /**支付订单号**/
    private String payOrderNo;

    /**实付金额**/
    private double amount;
    // 1 待处理 2 处理中 3 代扣成功 4 代扣失败 5 代扣超限额 6 系统error
    private Short state;

    /**商户码**/
    private String merchantCode;

    /**支付渠道**/
    private String payChannelCode;

    /**银行名称**/
    private String bankName;

    private String bankcardNo;

    /**推荐费用类型**/
    private String businessOrderType;

    /**业务流水号**/
    private String businessOrderNo;
}
