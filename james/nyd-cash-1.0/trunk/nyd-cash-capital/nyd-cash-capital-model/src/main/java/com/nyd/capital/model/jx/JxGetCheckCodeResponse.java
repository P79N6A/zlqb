package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;

@Data
public class JxGetCheckCodeResponse implements Serializable {

    //状态码
    private String statusCode;

    //返回信息
    private String message;

}
