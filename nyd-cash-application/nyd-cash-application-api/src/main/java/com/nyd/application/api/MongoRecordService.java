package com.nyd.application.api;

import com.tasfe.framework.support.model.ResponseData;

import java.util.Map;

/**
 * mongo服务
 */
public interface MongoRecordService {
    /**
     * 保存或更新账单相关数据
     * @param map
     * @param collectionName
     * @return
     */
    public ResponseData upsertByBillNo(Map<String, Object> map, String collectionName);
}
