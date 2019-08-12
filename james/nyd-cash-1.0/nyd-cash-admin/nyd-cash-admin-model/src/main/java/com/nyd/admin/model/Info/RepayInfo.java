package com.nyd.admin.model.Info;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wucx
 * @Date: 2018/10/10 11:35
 */
@Data
public class RepayInfo implements Serializable {
    //交易渠道
    private String repayChannel;

    //交易订单号
    private String repayNo;
}
