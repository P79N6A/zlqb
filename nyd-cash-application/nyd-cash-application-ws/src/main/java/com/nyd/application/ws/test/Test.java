package com.nyd.application.ws.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fadada.sdk.client.FddClientBase;
import com.fadada.sdk.client.FddClientExtra;
import com.google.gson.Gson;
import com.nyd.application.service.commonEnum.ResultCode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
//        uploadTempelate();
//        generateContract();
        invokeViewPdfURL();


    }

    public static void uploadTempelate(){
        FddClientBase clientBase = null;
        if (clientBase==null){
            clientBase = new FddClientBase("401870","xzUdxf7SXGjmIxp5pC3cBlZM","2.0","http://test.api.fabigbig.com:8888/api/");
        }
        System.out.println(clientBase);

        File file = new File("D:\\fadada\\123\\122.pdf");
        String response = clientBase.invokeUploadTemplate("ceshi001",file,"");
        JSONObject jsonObject = JSON.parseObject(response);
        System.out.println(jsonObject);
        if (jsonObject!=null&&jsonObject.containsKey("code")) {
            if (!"1".equals(jsonObject.getString("code"))) {

            }
        }
    }

    public static void generateContract(){
        FddClientBase clientBase = null;
        if (clientBase==null){
            clientBase = new FddClientBase("401870","xzUdxf7SXGjmIxp5pC3cBlZM","2.0","http://test.api.fabigbig.com:8888/api/");
        }
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("contractId","121212");
            String response = clientBase.invokeGenerateContract("ceshi001","hetong001",
                    "hello world","12","2","{\"platformName\":\"hehe\"}",null);
            System.out.println("generateContract response is "+response);
            JSONObject jsonObject = JSON.parseObject(response);
            System.out.println("generateContract response is "+jsonObject);
            if (jsonObject!=null&&jsonObject.containsKey("code")) {
                if (!"1000".equals(jsonObject.getString("code"))) {
                } else {
                    map.put("downloadUrl",jsonObject.getString("download_url"));
                    map.put("viewPdfUrl",jsonObject.getString("viewpdf_url"));
                }
            } else {
            }
        } catch (Exception e) {
        }
    }

    public static void invokeViewPdfURL(){
        FddClientExtra clientExtra = null;
        if (clientExtra==null){
            clientExtra = new FddClientExtra("401870","xzUdxf7SXGjmIxp5pC3cBlZM","2.0","http://test.api.fabigbig.com:8888/api/");
        }
        String viewUrl = clientExtra.invokeViewPdfURL("hetong001");
        System.out.println("viewUrl is "+viewUrl);
    }
}
