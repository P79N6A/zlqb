package com.nyd.mq.ws.controller;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.nyd.mq.service.util.MongoApi;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujx on 2018/1/17.
 */
@RestController
@RequestMapping(value = "/mongo")
public class MongoController {

    private static Logger logger = LoggerFactory.getLogger(MongoController.class);

    @Autowired
    MongoApi mongoApi;

    /**
     * 更新mongoAppId数据
     * @param map
     * @return
     */
    @RequestMapping(value = "/updateDspMongoInfo", method = RequestMethod.POST, produces = "application/json")
    public ResponseData updateDspMongoInfo(@RequestBody Map map){
        logger.info("入参" +  new Gson().toJson(map));
        ResponseData responseData = ResponseData.success();
        if(map != null && map.size() > 0){
            String collectionName = map.get("collectionName").toString();
            //先查询所有不包含appId的数据,然后组装appId更新数据
            List<Map> list =  mongoApi.getMongoInfoTemplate(collectionName);
            logger.info("查询出需要更新的数据" + list.size() + new Gson().toJson(list));
            if (list != null && list.size() > 0){
                for ( Map h : list){
                    String input = h.get("input").toString();
                    Map mapType = JSON.parseObject(input,Map.class);
                    h.put("appId", mapType.get("appId"));
                    mongoApi.upsert(h, collectionName);
                }
            }
            //logger.info("组装后的数据" + new Gson().toJson(list));
        }else{
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }

    /**
     * 更新mongoAppId数据
     * @param map
     * @return
     */
    @RequestMapping(value = "/updateUserIdMongoInfo", method = RequestMethod.POST, produces = "application/json")
    public ResponseData updateUserIdMongoInfo(@RequestBody Map map){
        logger.info("入参" +  new Gson().toJson(map));
        ResponseData responseData = ResponseData.success();
        if(map != null && map.size() > 0){
            String collectionName = map.get("collectionName").toString();
            //先查询所有不包含appId的数据,然后组装appId更新数据
            List<Map> list =  mongoApi.getMongoInfoTemplate(collectionName);
            logger.info("查询出需要更新的数据" + list.size() + new Gson().toJson(list));
            if (list != null && list.size() > 0){
                for ( Map h : list){
                    String id = String.valueOf(h.get("_id"));
                    h.put("userId",id);
                    mongoApi.upsert(h, collectionName);
                }
            }
            //logger.info("组装后的数据" + new Gson().toJson(list));
        }else{
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }


    @RequestMapping(value = "/updateMongoInfo", method = RequestMethod.POST, produces = "application/json")
    public ResponseData updateMongoInfo(@RequestBody Map map) throws ParseException {
        logger.info("入参" +  new Gson().toJson(map));
        ResponseData responseData = ResponseData.success();
        if(map != null && map.size() > 0){
            String collectionName = map.get("collectionName").toString();
            //先查询所有不包含appId的数据,然后组装appId更新数据
            List<Map> list =  mongoApi.getMongoInfoByIdTemplate(collectionName);
            logger.info("查询出需要更新的数据" + list.size() + new Gson().toJson(list));
            if (list != null && list.size() > 0){
                for (Map h : list){
                    String create_time = String.valueOf(h.get("create_time"));
                    String update_time = String.valueOf(h.get("update_time"));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    h.put("create_time",simpleDateFormat.parse(create_time));
                    h.put("update_time",simpleDateFormat.parse(update_time));

                    mongoApi.upsert(h, collectionName);
                }
            }
            //logger.info("组装后的数据" + new Gson().toJson(list));
        }else{
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }

}
