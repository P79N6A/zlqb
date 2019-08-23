package com.nyd.msg.service.channel.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author WangXinHua
 * @describe 功能描述 创蓝短信返回数据
 * @Date 9:44 on 2019/8/23
 */
@Data
public class ChuangLanResponseDto {

    private String code;

    private String msgId;

    private String time;

    private String errorMsg;

    public static void main(String[] args) {
        String s = "{\"code\":\"0\",\"msgId\":\"19082221074623444\",\"time\":\"20190822210746\",\"errorMsg\":\"\"}";

        ChuangLanResponseDto chuangLanResponseDto = JSON.parseObject(s,ChuangLanResponseDto.class);
        System.out.printf(chuangLanResponseDto.toString());
    }
}
