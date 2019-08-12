package com.nyd.settlement.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peng
 * @create 2018-01-19 16:35
 **/
public enum RepayTypeEnum {
    //'0强扣,1使用第二种方法还款,2用绑定的卡还款,3会员费,
    // 10非以上任何一种,11会员费失败,12强扣失败,13使用第二种方式还款失败,
    // 14用绑定的卡还款失败,15未知失败',

    zero(0,"强扣"),
    one(1,"使用第二种方法还款"),
    two(2,"用绑定的卡还款"),
    three(3,"会员费"),
    ten(10,"非以上任何一种"),
    eleven(11,"会员费失败"),
    twelve(12,"强扣失败"),
    thirteen(13,"使用第二种方式还款失败"),
    fourteen(14,"用绑定的卡还款失败"),
    fifteen(15,"未知失败");


    private Integer code;
    private String name;

    RepayTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Map<Integer,String> enumMap() {
        Map<Integer,String> map = new HashMap<>();

        for (RepayTypeEnum type : RepayTypeEnum.values()) {

            map.put(type.getCode(),type.getName());

        }
        return map;
    }
}
