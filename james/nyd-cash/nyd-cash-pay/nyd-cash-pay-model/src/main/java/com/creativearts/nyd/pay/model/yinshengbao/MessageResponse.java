package com.creativearts.nyd.pay.model.yinshengbao;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageResponse implements Serializable{

    //响应码
    private String result_code;

    //结果描述
    private String result_msg;

    //用户授权码
    private String token;


}
