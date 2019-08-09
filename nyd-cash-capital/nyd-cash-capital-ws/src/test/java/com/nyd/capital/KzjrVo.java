package com.nyd.capital;

import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/12/11
 **/
public @Data class KzjrVo implements Serializable{
//    private String status
    private String channelCode;
    private String name;
    private String privateKey;
}
