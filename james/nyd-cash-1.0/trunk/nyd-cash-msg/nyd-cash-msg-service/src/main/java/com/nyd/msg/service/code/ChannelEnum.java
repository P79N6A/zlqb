package com.nyd.msg.service.code;

import java.util.HashMap;
import java.util.Map;

public enum ChannelEnum {
    TC("1"),
    MW("2"),
    TCBATCH("3"),
    /**
     * 大汉三通
     */
    DHST("5"),
    /**
     * 公共服务的短信发送
     */
    CST("6"),
    /**
     * UOLEEM
     */
    UOLEEM("8");

    private String code;
//    private String desc;
    private ChannelEnum(String code){
        this.code = code;
//        this.desc = desc;
    }
    public String getCode(){
        return code;
    }

    public static Map<String,ChannelEnum> getMap(){
        Map<String, ChannelEnum> enumDataMap = new HashMap<String, ChannelEnum>();
        for (ChannelEnum type : ChannelEnum.values()) {
            enumDataMap.put(type.getCode(),type);
        }
        return enumDataMap;
    }
}
