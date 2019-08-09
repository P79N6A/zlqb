package com.nyd.msg.service.utils;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DaHanMessage {
    private String account;
    private String password;
    private String phones;
    private String content;
    private String sign;
    private String sendtime;
}
