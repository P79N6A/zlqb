package com.nyd.capital.service.kzjr.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.model.kzjr.*;
import com.nyd.capital.service.kzjr.KzjrService;
import com.nyd.capital.service.kzjr.component.KzjrComponent;
import com.nyd.capital.service.kzjr.config.KzjrConfig;
import com.nyd.capital.service.validate.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

    @Autowired
    private ValidateUtil validateUtil;



    @Override
    public JSONObject sendSmsCode(SendSmsRequest request) {
        request.setChannelCode(kzjrConfig.getChannelCode());

        String sign = kzjrComponent.getSign(request.getMapWithoutSign());
        request.setSign(sign);
        logger.info(JSON.toJSONString(request));
        logger.info(request.toString());
        validateUtil.validate(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<JSONObject> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/sendSmsCode", HttpMethod.POST, entity, JSONObject.class);
       return s.getBody();
    }


    /**
     * 开户
     * @param request OpenAccountRequest
     * @return JSONObject
     */
    @Override
    public JSONObject accountOpen(OpenAccountRequest request)  {
        String sign = kzjrComponent.getSign(request.getMapWithoutSign());
        request.setSign(sign);

        validateUtil.validate(request);
        String name = null;
        try {
            name = URLEncoder.encode(request.getName(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("kzjr开户时转码出错"+e.getMessage());
            return null;
        }


        request.setName(name);
//        System.out.println(request.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        String code = URLEncoder.encode(request.toString(),"UTF-8");
//        System.out.println(code);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<JSONObject> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/accountOpen", HttpMethod.POST, entity, JSONObject.class);
        return s.getBody();

    }

    @Override
    public JSONObject queryAccount(QueryAccountRequest request) {
        request.setChannelCode(kzjrConfig.getChannelCode());
        String sign = kzjrComponent.getSign(request.getMapWithoutSign());
        request.setSign(sign);

        validateUtil.validate(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<JSONObject> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/queryAccount", HttpMethod.POST, entity, JSONObject.class);
        return s.getBody();
    }

    /**
     * 提交资产
     * @param request AssetSubmitRequest
     * @return JSONObject
     */
    @Override
    public JSONObject assetSubmit(AssetSubmitRequest request) {
        request.setChannelCode(kzjrConfig.getChannelCode());
        String sign = kzjrComponent.getSign(request.getMapWithoutSign());
        request.setSign(sign);

        validateUtil.validate(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<JSONObject> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/assetSubmit", HttpMethod.POST, entity, JSONObject.class);
        return s.getBody();
    }

    @Override
    public JSONObject productList(GetProductRequest request) {
        String sign = kzjrComponent.getSign(request.getMapWithoutSign());
        request.setSign(sign);

        validateUtil.validate(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<JSONObject> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/productList", HttpMethod.POST, entity, JSONObject.class);
        return s.getBody();
    }

    @Override
    public JSONObject repayNotify(RepayNotifyRequest request) {
        String sign = kzjrComponent.getSign(request.getMapWithoutSign());
        request.setSign(sign);

        validateUtil.validate(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<JSONObject> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/repayNotify", HttpMethod.POST, entity, JSONObject.class);
        return s.getBody();
    }
    //放款查询
    @Override
    public JSONObject queryMatchResult(QueryRemitResult remitResult) {
        String sign = kzjrComponent.getSign(remitResult.getMapWithoutSign());
        remitResult.setSign(sign);

        validateUtil.validate(remitResult);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(remitResult.toString(), headers);
        ResponseEntity<JSONObject> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/queryMatchResult", HttpMethod.POST, entity, JSONObject.class);
        return s.getBody();
    }
    //放款批量查询
    @Override
    public JSONObject queryMatchResultList(QueryRemitResultBatch remitResultBatch) {
        String sign = kzjrComponent.getSign(remitResultBatch.getMapWithoutSign());
        remitResultBatch.setSign(sign);

        validateUtil.validate(remitResultBatch);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(remitResultBatch.toString(), headers);
        ResponseEntity<JSONObject> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/queryMatchResultList", HttpMethod.POST, entity, JSONObject.class);
        return s.getBody();
    }

    @Override
    public JSONObject queryAssetList(QueryAssetListRequest request) {
        String sign = kzjrComponent.getSign(request.getMapWithoutSign());
        request.setSign(sign);

        validateUtil.validate(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<JSONObject> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/queryAssetList", HttpMethod.POST, entity, JSONObject.class);
        return s.getBody();
    }

    @Override
    public JSONObject queryAsset(QueryAssetRequest request) {
        String sign = kzjrComponent.getSign(request.getMapWithoutSign());
        request.setSign(sign);

        validateUtil.validate(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<JSONObject> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/queryAsset", HttpMethod.POST, entity, JSONObject.class);
        return s.getBody();
    }

    /**
     * 绑定银行卡
     * @param request BindCardRequest
     * @return JSONObject
     */
    @Override
    public JSONObject bindCard(BindCardRequest request) {
        request.setChannelCode(kzjrConfig.getChannelCode());
        String sign = kzjrComponent.getSign(request.getMapWithoutSign());
        request.setSign(sign);

        validateUtil.validate(request);
        logger.info("bind"+ JSON.toJSONString(request));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<JSONObject> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/bindCard", HttpMethod.POST, entity, JSONObject.class);
        return s.getBody();
    }

    @Override
    public JSONObject unBindCard(UnBindCardRequest request) {
        request.setChannelCode(kzjrConfig.getChannelCode());
        String sign = kzjrComponent.getSign(request.getMapWithoutSign());
        request.setSign(sign);

        validateUtil.validate(request);
        logger.info("unbind"+ JSON.toJSONString(request));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<JSONObject> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/unBindCard", HttpMethod.POST, entity, JSONObject.class);
        return s.getBody();
    }

    @Override
    public JSONObject batchAssetSubmit(BatchAssetSubmitRequest request) {
        String sign = kzjrComponent.getSign(request.getMapWithoutSign());
        request.setSign(sign);

//        validateUtil.validate(request);
        logger.info("batchAssetSubmit"+ JSON.toJSONString(request));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<JSONObject> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/batchAssetSubmit", HttpMethod.POST, entity, JSONObject.class);
        return s.getBody();
    }

    @Override
    public String openAccountPage(KzjrOpenAcountRequest request) {
        request.setChannelCode(kzjrConfig.getChannelCode());
//        logger.info(request.toStringNew());
        String sign = kzjrComponent.getSign(request.getMapWithoutSign());
        request.setSign(sign);

        String name = null;
        String url = null;
        try {
            name = URLEncoder.encode(request.getName(),"UTF-8");
            url = URLEncoder.encode(request.getReturnUrl(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("kzjr开户页面时转码出错"+e.getMessage());
            return null;
        }
        request.setName(name);
        request.setReturnUrl(url);
        logger.info("openAccountPage"+ request.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<String> s = restTemplate.exchange(kzjrConfig.getBaseUrl()+"/accountOpenPage", HttpMethod.POST, entity, String.class);
        return s.getBody();
    }
}
