package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;

/**
 * 五合一接口响应实体类
 * @author cm
 */
@Data
public class JxFiveComprehensiveResponse implements Serializable {

    //状态码
    private String statusCode;

    //返回信息
    private String message;

    //用户ID
    private String memberId;

    //
    private String url;

    //
    private String data;
}
