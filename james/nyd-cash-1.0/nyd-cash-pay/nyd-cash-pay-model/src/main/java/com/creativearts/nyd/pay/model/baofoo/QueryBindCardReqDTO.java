package com.creativearts.nyd.pay.model.baofoo;

import lombok.Data;
import lombok.ToString;

/**
 *
 * @author shaoqing.liu
 * @date 2018/10/13 16:04
 */
@Data
@ToString
public class QueryBindCardReqDTO {


    private String idcardNo;

    /**来源 默认为nyd**/
    private String source;

    private int state;


    /**支付渠道**/
    private String[] payChannelCodes;


}
