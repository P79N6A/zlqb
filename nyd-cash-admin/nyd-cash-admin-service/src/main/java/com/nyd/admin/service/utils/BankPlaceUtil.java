package com.nyd.admin.service.utils;

import com.alibaba.fastjson.JSONObject;
import java.util.Map;

public class BankPlaceUtil {

    public static String getBankPlace(String cardNo) {
        String bangkPlace = null;
        JSONObject jsonObject = HttpClientUtils.httpGet(ConfigUtil.URL+"?cardNum="+cardNo+"&appkey="+ConfigUtil.APPCODE);
        Map<String, Object> map = (Map<String, Object>) jsonObject;
        Map<String, Object> result = (Map<String, Object>)map.get("result");
        if (map.get("code").equals("10000")) {
                if (result.get("showapi_res_code").equals(0)){
                    Map<String, String> message = (Map<String, String>) result.get("showapi_res_body");
                    bangkPlace = message.get("area");
                }
        }
        return bangkPlace;
    }

    public static void main(String[] args) {
        System.out.println(getBankPlace("6217001180010619651"));
    }
}
