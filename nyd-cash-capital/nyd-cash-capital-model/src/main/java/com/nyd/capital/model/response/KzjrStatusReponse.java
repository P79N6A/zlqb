package com.nyd.capital.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2018/3/2
 **/
@Data
public class KzjrStatusReponse implements Serializable{
    private String p2pId;
    private Integer flag; //0需要发短信验证码  1开户  2 正常 3正在开户中
    private String url;//地址。当flag为1时有效
}
