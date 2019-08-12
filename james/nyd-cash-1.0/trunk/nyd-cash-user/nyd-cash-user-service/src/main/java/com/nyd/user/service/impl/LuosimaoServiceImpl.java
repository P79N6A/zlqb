package com.nyd.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.user.model.AccountInfo;
import com.nyd.user.service.LuosimaoService;
import com.nyd.user.service.util.UserProperties;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @Author: zhangp
 * @Description:  验证人机结果
 * @Date: 17:40 2018/8/9
 */
@Service
public class LuosimaoServiceImpl implements LuosimaoService {

    private static Logger logger = LoggerFactory.getLogger(LuosimaoServiceImpl.class);

    @Autowired
    private UserProperties userProperties;

    @Override
    public ResponseData verifyResult(AccountInfo accountInfo) {
        ResponseData responseData = ResponseData.success();
        String verifyResult = accountInfo.getVerifyResult();
        String accountNumber = accountInfo.getAccountNumber();
        try {
            if ("ON".equals(userProperties.getLuosimaoVerifySwitch())) {
                if (StringUtils.isBlank(verifyResult)) {
                    return ResponseData.error("人机校验失败，请重新验证!");
                }
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
                params.add("api_key",userProperties.getLuosimaoKey());
                params.add("response",verifyResult);
                HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, requestHeaders);
                ResponseEntity<String> result = restTemplate().postForEntity(userProperties.getLuosimaoVerifyUrl(),requestEntity,String.class);
                logger.info("请求图形验证结果是:"+ JSON.toJSONString(result));
                if (result!=null&&result.getBody()!=null) {
                    JSONObject resJson = JSON.parseObject(result.getBody());
                    if ("success".equals(resJson.getString("res"))) {
                        return responseData;
                    }
                }
            } else {
                return responseData;
            }
        } catch (Exception e) {
            logger.error("verifyResult has exception! accountNumber:"+accountNumber,e);
        }
        return ResponseData.error("人机校验失败，请重新验证!");
    }

    public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }
}
