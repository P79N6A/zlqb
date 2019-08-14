package com.nyd.user.service;

import com.nyd.application.api.MongoRecordService;
import com.nyd.user.api.MongoDBService;
import com.nyd.user.service.impl.AccountInfoServiceImpl;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * mongodb服务
 */
@Service("mongoDBService")
public class MongoDBServiceImpl implements MongoDBService {
    private static Logger logger = LoggerFactory.getLogger(MongoDBServiceImpl.class);
    @Autowired
    private MongoRecordService mongoRecordService;

    /**
     * 保存或更新账单相关数据
     * @param map
     * @param collectionName
     * @return
     */
    public ResponseData upsertByBillNo(Map<String,Object> map, String collectionName){
        try {
            return mongoRecordService.upsertByBillNo(map, collectionName);
        }catch (Exception e){
            ResponseData response=ResponseData.error();
            response.setMsg("mongo服务异常");
            logger.error("调用mongoRecordService异常 e="+e.getMessage());
            return response;
        }
    }
}
