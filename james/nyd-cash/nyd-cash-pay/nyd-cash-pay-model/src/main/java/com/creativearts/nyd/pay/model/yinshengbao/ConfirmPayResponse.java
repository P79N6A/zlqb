package com.creativearts.nyd.pay.model.yinshengbao;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConfirmPayResponse implements Serializable{
    //响应码
    private String result_code;

    //结果描述
    private String result_msg;

    //交易状态
    private String status;

    //结果描述
    private String desc;
}
