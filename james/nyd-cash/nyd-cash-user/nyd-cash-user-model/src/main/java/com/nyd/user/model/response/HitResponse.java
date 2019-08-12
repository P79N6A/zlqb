package com.nyd.user.model.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class HitResponse implements Serializable{

    //撞库结果状态  0：没命中      1：命中
    private int status;

    //撞库结果信息
    private String msg;

    //
    private String mobile;
}
