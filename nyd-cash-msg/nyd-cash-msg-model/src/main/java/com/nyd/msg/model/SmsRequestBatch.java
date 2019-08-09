package com.nyd.msg.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 相同内容群发
 * Yuxiang Cong
 **/
public @Data
class SmsRequestBatch implements Serializable{
    private Integer smsType;
    private String appName;
    /**
     * 90个以内
     */
    private List<String> cellPhones;
    private Map<String,Object> replaceMap;
}
