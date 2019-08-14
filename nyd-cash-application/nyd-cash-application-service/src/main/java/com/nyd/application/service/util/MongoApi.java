package com.nyd.application.service.util;

import com.nyd.application.model.mongo.AddressBook;
import com.nyd.application.model.mongo.CallInfo;
import com.nyd.application.model.mongo.FilePDFInfo;
import com.nyd.application.model.mongo.SmsInfo;
import com.nyd.application.model.request.AttachmentModel;
import com.nyd.application.service.commonEnum.MongoCollection;
import com.nyd.application.service.impl.AgreeMentServiceImpl;
import com.nyd.user.entity.Attachment;
import org.apache.commons.lang3.StringUtils;
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
     * 保存
     */
    public Integer save(Map<String, Object> map,String collectionName) {
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
     * 保存或更新
     */
    public Integer upsert(Map<String, Object> map, String collectionName) {
        if (map != null && map.size()>0){
            String userId = String.valueOf(map.get("userId")).toUpperCase();
            addCreateTimeAndupdateTime(map, collectionName);
            Update update = new Update();
            map.remove("userId");
            fillUpdate(update, map);
            mongoTemplate.upsert(new Query(new Criteria("_id").is(userId)), update, collectionName);
            return 1;
        }
        return 0;
    }

    /**
     * 保存或更新
     */
    public Integer upsertContractId(Map<String, Object> map, String collectionName) {
        if (map != null && map.size()>0){
            String contractId = String.valueOf(map.get("contractId"));
            addCreateTimeAndupdateTimeContractId(map, collectionName);
            Update update = new Update();
            map.remove("contractId");
            fillUpdate(update, map);
            mongoTemplate.upsert(new Query(new Criteria("_id").is(contractId)), update, collectionName);
            return 1;
        }
        return 0;
    }

    /**
     * 增加创建时间和更新时间
     */
    private void addCreateTimeAndupdateTimeContractId(Map<String, Object> map, String collectionName) {
        boolean exists = false;
        if (map != null && map.containsKey("contractId")) {
            String id = (String) map.get("contractId");
            exists = mongoTemplate.exists(new Query(new Criteria("_id").is(id)), collectionName);
        }
        if (!exists) {
            map.put("create_time", new Date());
        }
        map.put("update_time", new Date());
    }

    /**
     * 增加创建时间和更新时间
     */
    private void addCreateTimeAndupdateTime(Map<String, Object> map, String collectionName) {
        boolean exists = false;
        if (map != null && map.containsKey("id")) {
            String id = (String) map.get("id");
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
     * 保存或更新通讯录
     */
    public Integer saveAddressBook (List<AddressBook> list) {
        if (list != null && list.size()>0){
            mongoTemplate.insert(list, MongoCollection.ADDRESSBOOK.getCode());
            return 1;
        }
        return 0;
    }

    /**
     * 保存或更新通话记录
     */
    public Integer saveCallInfo (List<CallInfo> list) {
        if (list != null && list.size()>0){
            mongoTemplate.insert(list,MongoCollection.CALLINFO.getCode());
            return 1;
        }
        return 0;
    }

    /**
     * 保存或更新短信
     */
    public Integer saveSmsInfo (List<SmsInfo> list) {
        if (list != null && list.size()>0){
            mongoTemplate.insert(list,MongoCollection.SMSINFO.getCode());
            return 1;
        }
        return 0;
    }

    /**
     * 保存或更新埋点信息
     */
    public Integer saveBuriedInfo (List<Map<String, Object>> list) {
        if (list != null && list.size()>0){
            mongoTemplate.insert(list,MongoCollection.BURIEDINFO.getCode());
            return 1;
        }
        return 0;
    }

    /**
     * 查询通讯录
     * @return
     */
    public List<AddressBook> getAddressBooks(String phoneNo,String deviceId) {
        Criteria criteria = Criteria.where("phoneNo").is(phoneNo)
                .and("deviceId").is(deviceId);
        return mongoTemplate.find(new Query(criteria),AddressBook.class);
    }

    /**
     * 查询通话记录
     * @return
     */
    public List<CallInfo> getCallInfos(String phoneNo,String deviceId) {
        Criteria criteria = Criteria.where("phoneNo").is(phoneNo)
                .and("deviceId").is(deviceId);
        return mongoTemplate.find(new Query(criteria),CallInfo.class);
    }

    /**
     * 查询短信
     * @return
     */
    public List<SmsInfo> getSmsInfo(String phoneNo,String deviceId) {
        Criteria criteria = Criteria.where("phoneNo").is(phoneNo)
                .and("deviceId").is(deviceId);
        return mongoTemplate.find(new Query(criteria),SmsInfo.class);
    }

    /**
     * 查询合同模板编号
     * @return
     */
    public Map<String,Object> getContractTemplate() {
        Map<String,Object> map = new HashMap<>();
        try {
            map = mongoTemplate.findOne(new Query(new Criteria("_id").is("contract")),HashMap.class,
                    "contract_template");
        } catch (Exception e) {
            LOGGER.error("getContractTemplate from mongo has except ",e);
        }
        return map;
    }

    /**
     * 查询合同模板编号
     * @return
     */
    public Map<String,Object> getContractTemplate(String appName) {
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isBlank(appName)||"nyd".equals(appName)) {
            return getContractTemplate();
        }
        try {
            map = mongoTemplate.findOne(new Query(new Criteria("_id").is("contract_"+appName)),HashMap.class,
                    "contract_template");
        } catch (Exception e) {
            LOGGER.error("getContractTemplate from mongo has except ",e);
        }
        return map;
    }

    /**
     * 查询PDF文件qiniu路径
     *
     * @param userId
     * @return
     */
    public FilePDFInfo getFilePDFInfo(String userId) {
        FilePDFInfo filePDFInfo = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), FilePDFInfo.class, "pdfInfo");
        return filePDFInfo;
    }

    public void saveFilePDFInfo(FilePDFInfo filePDFInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", filePDFInfo.getUserId());
        map.put("reportCode", filePDFInfo.getReportCode());
        map.put("filePDFPath", filePDFInfo.getFilePDFPath());
        addCreateTimeAndupdateTime(map,"pdfInfo");
        mongoTemplate.save(map, "pdfInfo");
    }

    public List<Attachment> getAttachment(String userId) {
        Criteria criteria = Criteria.where("userId").is(userId);
        return mongoTemplate.find(new Query(criteria),Attachment.class);
    }

    public AttachmentModel getAttachmentModel(String userId, String type){
        Criteria criteria = Criteria.where("userId").is(userId).and("type").is(type);
        return mongoTemplate.findOne(new Query(criteria), AttachmentModel.class, "attachment");
    }

    /**
     * 保存或更新账单相关数据
     */
    public Integer upsertByBillNo(Map<String, Object> map, String collectionName) {
        if (map != null && map.size()>0){
            String billNo = String.valueOf(map.get("billNo"));
            addCreateTimeAndupdateTimeContractId(map, collectionName);
            Update update = new Update();
            map.remove("billNo");
            fillUpdate(update, map);
            mongoTemplate.upsert(new Query(new Criteria("_id").is(billNo)), update, collectionName);
            return 1;
        }
        return 0;
    }
}
