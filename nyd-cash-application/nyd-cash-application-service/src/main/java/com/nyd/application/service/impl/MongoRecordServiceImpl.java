package com.nyd.application.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.nyd.application.api.MongoRecordService;
import com.nyd.application.service.util.MongoApi;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
/**
 * mongo服务
 */
@Service("mongoRecordService")
public class MongoRecordServiceImpl implements MongoRecordService {
    private static Logger LOGGER = LoggerFactory.getLogger(MongoRecordServiceImpl.class);

    @Autowired
    private MongoApi mongoApi;
    /**
     * 保存或更新账单相关数据
     * @param map
     * @param collectionName
     * @return
     */
    public ResponseData upsertByBillNo(Map<String, Object> map,String collectionName) {
        LOGGER.info("begin to saveMapByCollectionName");
        ResponseData responseData = ResponseData.success();
        try {
            Object billNo=map.get("billNo");
            if(billNo==null|| StringUtils.isBlank(String.valueOf(billNo))){
                responseData.setStatus("1");
                responseData.setMsg("账单编号不能为空");
                return responseData;
            }
            mongoApi.upsertByBillNo(map,collectionName);
            LOGGER.info("saveMapByCollectionName success !");
        } catch (Exception e) {
            responseData.setStatus("1");
            responseData.setMsg("数据存储失败");
            LOGGER.error("saveMapByCollectionName error !",e);
        }
        return responseData;
    }
}
