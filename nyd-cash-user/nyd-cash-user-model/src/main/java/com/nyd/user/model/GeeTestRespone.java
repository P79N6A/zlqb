package com.nyd.user.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author liuqiu
 */
@Data
@ToString
public class GeeTestRespone {

    private String success;
    private String challenge;
    private String gt;
    private String new_captcha;
}
