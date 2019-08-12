package com.nyd.msg.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class SmsUoleemResponse implements Serializable {
    /** 响应状态 0表示成功，其他表示失败 **/
    private String code;
    /** 格式正确条数 **/
    private String msg;

}
