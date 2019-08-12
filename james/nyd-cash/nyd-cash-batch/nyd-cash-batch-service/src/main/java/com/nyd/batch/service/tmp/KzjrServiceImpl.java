package com.nyd.batch.service.tmp;

import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.model.kzjr.QueryAssetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Cong Yuxiang
 * 2017/12/11
 **/
@Service
public class KzjrServiceImpl implements KzjrService{

    Logger logger = LoggerFactory.getLogger(KzjrServiceImpl.class);

    @Autowired
    private KzjrComponent kzjrComponent;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KzjrConfig kzjrConfig;




    @Override
    public JSONObject queryAsset(QueryAssetRequest request) {
        String sign = kzjrComponent.getSign(request.getMapWithoutSign());
        request.setSign(sign);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<JSONObject> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/queryAsset", HttpMethod.POST, entity, JSONObject.class);
        return s.getBody();
    }


}
