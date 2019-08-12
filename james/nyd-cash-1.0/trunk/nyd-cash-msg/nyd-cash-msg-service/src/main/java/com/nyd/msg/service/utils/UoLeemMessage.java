package com.nyd.msg.service.utils;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UoLeemMessage {
    private String ts;
    private String account;
    private String username;
    private String content;
    private String pswd;
    private String pwd;
    private String mobile;
    private String msg;
    private Boolean needstatus;
    private String product;
    private String extno;
    private String resptype;
    private String encrypt;
}
