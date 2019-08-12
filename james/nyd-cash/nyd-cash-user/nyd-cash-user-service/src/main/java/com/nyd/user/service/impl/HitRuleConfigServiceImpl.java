package com.nyd.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.user.dao.mapper.HitRuleConfigMapper;
import com.nyd.user.entity.HitRuleConfig;
import com.nyd.user.model.request.HitRuleConfigRequest;
import com.nyd.user.service.HitRuleConfigService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HitRuleConfigServiceImpl implements HitRuleConfigService {
    private static final Logger logger = LoggerFactory.getLogger(HitRuleConfigServiceImpl.class);


    @Autowired
    private HitRuleConfigMapper hitRuleConfigMapper;

    @Override
    public ResponseData<List<HitRuleConfig>> findByAppNameAndSource(HitRuleConfigRequest request) {
        logger.info("通过AppName和source查找撞库规则参数:"+ JSON.toJSONString(request));
        ResponseData responseData = ResponseData.success();
        String appName = request.getAppName();                  //APP名称
        String source = "";                                     //source
        try {
            if (StringUtils.isNotBlank(request.getSource())){
                source = request.getSource();
                String resultSource = "";
                Map<String,String> map = new HashMap<>();
                map.put("appName",appName);
                //找到真正的渠道
                List<HitRuleConfig> hitRuleConfigList = hitRuleConfigMapper.selectByAppName(map);
                if (hitRuleConfigList != null && hitRuleConfigList.size() > 0){
                    for (HitRuleConfig hitRuleConfig : hitRuleConfigList){
                    	if(source.contains("_")) {                      	
                        	if(source.contains(hitRuleConfig.getSource()) && hitRuleConfig.getSource().contains("_")) {
                        		 resultSource = hitRuleConfig.getSource();
                        		 break;
                        	}
                    	}else {
                    		if(source.contains(hitRuleConfig.getSource())) {
                    			resultSource = hitRuleConfig.getSource();
                                break;
                    		}
                    		
                    	}                   	                    
                    }
                }

                if (!StringUtils.isNotBlank(resultSource)){
                    resultSource = "defaultSource";//表示没有找到对应的渠道,说明这家还没有配置撞库规则,就走默认的渠道和默认规则
                }

                logger.info("真正的渠道source:"+resultSource);

                Map<String,String> params = new HashMap<>();
                params.put("appName",appName);
                params.put("source",resultSource);
                //找到对应规则
                List<HitRuleConfig> list = hitRuleConfigMapper.selectByAppNameAndSource(params);
                logger.info("根据appName:"+appName+",source:"+source+"获取到的规则对象:"+JSON.toJSON(list));
                if (list != null && list.size() > 0 ){
                    responseData.setData(list);
                }
            }
        }catch (Exception e){
            logger.error("通过AppName和source查找撞库规则出错",e);
            ResponseData.error("服务器开小差");
        }

        return responseData;
    }
}
