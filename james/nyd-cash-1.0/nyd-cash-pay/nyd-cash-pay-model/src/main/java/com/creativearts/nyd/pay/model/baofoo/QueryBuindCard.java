package com.creativearts.nyd.pay.model.baofoo;

import lombok.Data;
import lombok.ToString;

/**
 * @author shaoqing.liu
 * @date 2018/10/13 18:25
 */

@Data
@ToString
public class QueryBuindCard {

    /**请求号 **/
    private String requestNo;
    /**银行卡号 **/
    private String cardNo;

    /**银行名称**/
    private String bankName;

    /**渠道code **/
    private String payChannelCode;

    /**渠道名称 **/
    private String payChannelName;

    /**身份证号 **/
    private String idcardNo;

    /**手机号码**/
    private String phone;

    /**状态 1 待绑定 2 绑定中 3 已绑定 4 绑定失败 5 解绑 **/
    private int state;
}
