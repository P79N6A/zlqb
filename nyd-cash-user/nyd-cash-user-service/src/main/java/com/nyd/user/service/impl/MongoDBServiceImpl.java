package com.nyd.user.service.impl;

import com.nyd.application.api.MongoRecordService;
import com.nyd.user.api.MongoDBService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
     * 保存数据
     * @param map
     * @param collectionName
     * @return
     */
    public ResponseData save(Map<String,Object> map, String collectionName){
        try {
            return mongoRecordService.save(map, collectionName);
        }catch (Exception e){
            ResponseData response=ResponseData.error();
            response.setMsg("mongo服务异常");
            logger.error("调用mongoRecordService异常 e="+e.getMessage());
            return response;
        }
    }
    /**
     * 根据账单编号获取相关数据
     * @param billNo
     * @param collectionName
     * @return
     */
    public ResponseData<List<Map<String,String>>> getSettleAccountImg(String billNo, String collectionName){
        try {
            return mongoRecordService.getSettleAccountImg(billNo, collectionName);
        }catch (Exception e){
            ResponseData response=ResponseData.error();
            response.setMsg("mongo服务异常");
            logger.error("调用mongoRecordService异常 e="+e.getMessage());
            return response;
        }
    }
}
