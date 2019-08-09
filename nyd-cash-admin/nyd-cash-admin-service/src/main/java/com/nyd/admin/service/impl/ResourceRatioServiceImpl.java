package com.nyd.admin.service.impl;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.ResourceRatioMapper;
import com.nyd.admin.model.ResourceRatioVo;
import com.nyd.admin.service.ResourceRatioService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Peng
 * @create 2017-12-27 12:04
 **/
@Service
public class ResourceRatioServiceImpl implements ResourceRatioService {

    private static Logger LOGGER = LoggerFactory.getLogger(ResourceRatioServiceImpl.class);

    @Autowired
    ResourceRatioMapper resourceRatioMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData getResourceRatio() {
        ResponseData responseData = ResponseData.success();
        String source = null;
        int sumRegister = 0;
        int register = 0;
        try {
            List<ResourceRatioVo> resourceRatioVoList = resourceRatioMapper.findList();
            List<Map<String,Object>> list = new ArrayList<>();
            Map<String,Object> qltgmap = new HashMap<>();
            for(int i = 0 ; i < resourceRatioVoList.size() ; i++) {
                Map<String,Object> map = new HashMap<>();
                source = resourceRatioVoList.get(i).getSource().toString();
                register = resourceRatioVoList.get(i).getSumRegister();
                if(source.contains("qltg")){
                    sumRegister = sumRegister + register;
                }else {
                    map.put("source",source);
                    map.put("sumRegister",register);
                    list.add(map);
                }
            }
            qltgmap.put("source","qltg");
            qltgmap.put("sumRegister",sumRegister);
            list.add(qltgmap);
            responseData.setData(list);
        }catch (Exception e) {
            LOGGER.error("query resourceRatio error! ");
            return responseData.error();
        }
        return responseData;
    }
}
