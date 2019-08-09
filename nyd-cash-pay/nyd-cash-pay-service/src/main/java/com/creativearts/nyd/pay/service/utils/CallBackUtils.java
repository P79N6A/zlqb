package com.creativearts.nyd.pay.service.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/12/18
 **/
public class CallBackUtils {
    public static <T> T  parse(String str,Class<T> c) throws UnsupportedEncodingException {
        str = URLDecoder.decode(str,"UTF-8");
        String[] ss = str.split("&");
        Map<String,String> map = new HashMap<>();
        for (String s:ss){
           String[] entry = s.split("=");
           if(entry.length==2){
               map.put(entry[0],entry[1]);
           }
        }
        return JSONObject.parseObject(JSON.toJSONString(map),c);
    }

    public static void main(String[] args) throws UnsupportedEncodingException, ParseException {
//        NotifyResponseVo vo = parse("rt7_orderAmount=1.51&rt13_orderStatus=SUCCESS&rt12_cardAfterFour=7504&sign=2e3e16cdda7de41a8e1da69821274464&rt1_bizType=Withhold&rt6_serialNumber=WITHHOLD171218214933AYAZ&rt8_bankName=%E4%B8%AD%E5%9B%BD%E5%BB%BA%E8%AE%BE%E9%93%B6%E8%A1%8C&rt2_retCode=0000&rt9_bankCode=CCB&rt5_orderId=bill1513604972367&rt4_customerNumber=C1800026414&rt11_completeDate=2017-12-18+21%3A49%3A35&rt3_retMsg=%E6%88%90%E5%8A%9F&rt10_onlineCardType=DEBIT",NotifyResponseVo.class);
//        System.out.println(JSON.toJSONString(vo));
//        System.out.println(vo.getRt3_retMsg());
//         long a = DateUtils.parseDate("2050-01-01","yyyy-MM-dd").getTime();
//        DateFormatUtils.format(new Date(),"");
//         long b = DateUtils.parseDate("2010-01-01","yyyy-MM-dd").getTime();
        System.out.println((System.currentTimeMillis()+"").substring(2,11));
    }
}
