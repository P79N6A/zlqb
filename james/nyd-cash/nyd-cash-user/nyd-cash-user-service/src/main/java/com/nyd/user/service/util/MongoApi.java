package com.nyd.user.service.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.client.result.UpdateResult;
import com.nyd.application.model.request.AttachmentModel;
import com.nyd.user.model.request.NewAttachmentModel;
import com.nyd.user.model.vo.RefundAppVo;

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
    
    /**
     * 保存
     */
    public Integer save(Map<String, Object> map, String collectionName) {
        if (map != null && map.size()>0){
            if(collectionName != null && !"".equals(collectionName)){
                addCreateTimeAndupdateTime(map,collectionName);
                mongoTemplate.save(map,collectionName);
                return 1;
            }
        }
        return 0;
    }
    
    /**
        * 根据accountNumber 查询数据
     * @param accountNumber
     * @return
     */
    public List<NewAttachmentModel> getNewAttachmentModel(String accountNumber){
        Criteria criteria = Criteria.where("accountNumber").is(accountNumber);
        return mongoTemplate.find(new Query(criteria), NewAttachmentModel.class, "attachment");
    }
    /**
     * 根据退款订单号查询图片信息数据
     * @param
     * @return
     */
    public List<RefundAppVo> getRefundImge(String refundNo){
    	Criteria criteria = Criteria.where("refundNo").is(refundNo);
    	return mongoTemplate.find(new Query(criteria).with(new Sort(Sort.Direction.DESC,"create_time")), RefundAppVo.class, "attachment");
    }
    /**
     * 根据退款订单号查询图片信息数据
     * @param
     * @return
     */
    public void updateRefundImge(RefundAppVo refundNo){
    	Criteria criteria = Criteria.where("refundNo").is(refundNo.getRefundNo());
    	Update update = new Update();
    	update.set("reason", refundNo);
    	mongoTemplate.updateFirst(new Query(criteria).with(new Sort(Sort.Direction.DESC,"create_time")), update, "attachment");
    }
    /**
     * 根据退款订单号查询图片信息数据
     * @param
     * @return
     */
    public UpdateResult updateRefundImge(Map<String,Object> refundNo){
    	Criteria criteria = Criteria.where("refundNo").is(refundNo.get("refundNo"));
        Map<String,Object> map = mongoTemplate.findOne(new Query(criteria).with(new Sort(Sort.Direction.DESC,"create_time")), Map.class, "attachment");
        String id = String.valueOf(map.get("_id"));
    	Update update = new Update();
    	fillUpdate(update,refundNo);
    	return mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(id)), update, "attachment");
    }

}
