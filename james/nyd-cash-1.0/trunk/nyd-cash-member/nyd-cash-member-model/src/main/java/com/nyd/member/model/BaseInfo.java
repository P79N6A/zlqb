package com.nyd.member.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hwei on 2017/12/7.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseInfo implements Serializable{
    //用户ID
    private String userId;
    //账号
    private String accountNumber;
    //设备Id
    private String deviceId;



}
