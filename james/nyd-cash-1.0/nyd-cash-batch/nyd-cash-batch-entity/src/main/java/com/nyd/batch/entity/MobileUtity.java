package com.nyd.batch.entity;

import lombok.Data;

import java.util.Map;

/**
 * 中间数据储存对象
 * Cong Yuxiang
 * 2018/1/30
 **/
@Data
public class MobileUtity {
    private FriendCircle friendCircle;
    private Map<String,String> addressBookMap;
}
