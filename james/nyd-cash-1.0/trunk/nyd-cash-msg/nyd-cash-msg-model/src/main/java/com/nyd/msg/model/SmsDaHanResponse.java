package com.nyd.msg.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class SmsDaHanResponse implements Serializable {
    /** 响应时间 **/
    private String ts;
    /** 响应状态 0表示成功，其他表示失败 **/
    private String result;
    /** 格式正确条数 **/
    private String suc;
    /** 格式错误条数 **/
    private String fail;
    /** 状态匹配使用（如果响应的状态不是0，或者提交时needstatus不等于true，则没有msgid字段）**/
    private String msgid;
}
