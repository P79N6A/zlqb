package com.nyd.application.api;

import com.nyd.application.model.mongo.FileImagesInfo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;
import java.util.Map;

/**
 * mongo服务
 */
public interface MongoRecordService {
    /**
     * 保存数据
     * @param map
     * @param collectionName
     * @return
     */
    public ResponseData save(Map<String, Object> map, String collectionName);
    /**
     * 根据账单编号获取相关数据
     * @param billNo
     * @param collectionName
     * @return
     */
    public ResponseData<List<Map<String,String>>> getSettleAccountImg(String billNo, String collectionName);
}
