package com.nyd.msg.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/11/14
 **/
public @Data
class SmsResponse implements Serializable{
    private String status;
    private String msg;
}
