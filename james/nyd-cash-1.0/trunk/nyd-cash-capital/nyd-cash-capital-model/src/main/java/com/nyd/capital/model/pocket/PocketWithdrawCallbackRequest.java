package com.nyd.capital.model.pocket;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author liuqiu
 */
@Data
@ToString
public class PocketWithdrawCallbackRequest implements Serializable {

    /**
     * 放款订单号
     */
    private String order_id;

    /**
     * 打款日期如20161228
     */
    private String opr_dat;

    /**
     * 打款状态，0：打款成功；1003：打款失败；
     */
    private int code;

    /**
     * 打款结果描述
     */
    private String result;

    /**
     * 签名key固定为
     */
    private String sign;
}
