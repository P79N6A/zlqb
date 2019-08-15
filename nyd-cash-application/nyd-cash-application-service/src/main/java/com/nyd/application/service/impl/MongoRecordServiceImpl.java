package com.nyd.application.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.nyd.application.api.MongoRecordService;
import com.nyd.application.model.mongo.FileImagesInfo;
import com.nyd.application.service.util.MongoApi;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    /**
     * 根据账单编号获取相关数据
     * @param billNo
     * @param collectionName
     * @return
     */
    public ResponseData<List<Map<String,String>>> getSettleAccountImg(String billNo, String collectionName){
        LOGGER.info("begin to getFilesInfo");
        ResponseData responseData = ResponseData.success();
        try {
            if(billNo==null|| StringUtils.isBlank(billNo)){
                responseData.setStatus("1");
                responseData.setMsg("账单编号不能为空");
                return responseData;
            }
            List<FileImagesInfo> data=mongoApi.getSettleAccountImg(billNo,collectionName);
            if(data==null||data.size()==0){
                return responseData;
            }
            List<Map<String,String>> list=new ArrayList<Map<String,String>>(data.size());
            for(FileImagesInfo info:data){
                list.add(objectToMap(info));
            }
            responseData.setData(list);
            LOGGER.info("getFilesInfo success !");
            return responseData;
        } catch (Exception e) {
            responseData.setStatus("1");
            responseData.setMsg("数据获取失败");
            LOGGER.error("getFilesInfo error !",e);
            return responseData;
        }
    }
    private Map<String, String> objectToMap(Object obj) throws Exception {
        if(obj == null){
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            //使private成员可以被访问、修改
            field.setAccessible(true);
            if(null != field.get(obj)) {
                map.put(field.getName(), String.valueOf(field.get(obj)));
            }
        }
        return map;
    }
}
