package com.nyd.capital.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/12/13
 **/
@Data
public class CheckSendSmsDto implements Serializable{
    private String p2pId;
    private Integer flag; //0 需要发短信验证码)  1 不需要发 2 解绑失败
}
