package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;

/**
 * 即信提现接口线响应实体类
 *
 */
@Data
public class JxWithDrawResponse implements Serializable {


    //状态码
    private String statusCode;

    //返回信息
    private String message;

    //Url
    private String Url;

    //data
    private String Data;
}
