package com.nyd.mq.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Dengw on 2017/11/16
 */
@Component
public class MongoApi {
    private static Logger LOGGER = LoggerFactory.getLogger(MongoApi.class);
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存或更新
     */
    public Integer upsert(Map<String, Object> map, String collectionName) {
        if (map != null && map.size()>0){
            addCreateTimeAndupdateTime(map, collectionName);
            Update update = new Update();
            fillUpdate(update, map);
            mongoTemplate.upsert(new Query(new Criteria("_id").is(String.valueOf(map.get("_id")))), update, collectionName);
            return 1;
        }
        return 0;
    }

    /**
     * 增加创建时间和更新时间
     */
    private void addCreateTimeAndupdateTime(Map<String, Object> map, String collectionName) {
        boolean exists = false;
        if (map != null && map.containsKey("_id")) {
            String id = String.valueOf(map.get("_id"));
            exists = mongoTemplate.exists(new Query(new Criteria("_id").is(id)), collectionName);
        }
        if (!exists) {
            map.put("create_time", new Date());
        }
        map.put("update_time", new Date());
    }

    /**
     * 组装数据
     */
    private void fillUpdate(Update update, Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            for (Map.Entry<String, Object> e : entrySet) {
                update.set(e.getKey(), e.getValue());
            }
        }
    }


    /**
     * 查询mongo信息模板
     * @return
     */
    public List<Map> getMongoInfoTemplate(String collectionName) {
        List<Map> list = new ArrayList<>();
        try {
            list = mongoTemplate.find(new Query(new Criteria("appId").exists(false)),Map.class,
                    collectionName);
        } catch (Exception e) {
            LOGGER.error("getMongoInfoTemplate from mongo has except ",e);
        }
        return list;
    }


    /**
     * 查询mongo信息模板
     * 20180418
     * @return
     */
    public List<Map> getMongoInfoByIdTemplate(String collectionName) {
        List<Map> list = new ArrayList<>();
        try {
            list = mongoTemplate.find(new Query(new Criteria("create_time").regex("2018")),Map.class,
                    collectionName);
        } catch (Exception e) {
            LOGGER.error("getMongoInfoTemplate from mongo has except ",e);
        }
        return list;
    }

}
