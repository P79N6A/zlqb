package com.nyd.user.service.geetest;

import com.nyd.user.dao.mapper.GeetestConfigMapper;
import com.nyd.user.entity.GeetestConfig;
import com.nyd.user.model.GeeTestDto;
import com.nyd.user.model.GeeTestRespone;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author liuqiu
 */
@Component
public class GeeTestApi {

    private static Logger logger = LoggerFactory.getLogger(GeeTestApi.class);
    @Autowired
    private GeeTestConfig geeTestConfig;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GeetestConfigMapper mapper;

    public GeeTestRespone firstVerify(GeeTestDto dto) {
        GeetestLib gtSdk = getTrue(dto.getAppName(),dto.getIfNative());
        HashMap<String, String> param = setParam(dto);
        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(param);
        redisTemplate.opsForValue().set(gtSdk.gtServerStatusSessionKey + dto.getDeviceId(), gtServerStatus, 5, TimeUnit.MINUTES);
        GeeTestRespone resStr = com.alibaba.fastjson.JSONObject.parseObject(gtSdk.getResponseStr(),GeeTestRespone.class);
        logger.info("gettest respone is:" + resStr.toString());
        return resStr;
    }

    private HashMap<String, String> setParam(GeeTestDto dto) {
        String os = null;
        if ("1".equals(dto.getIfNative())){
            os = "native";
        }else{
            os = "h5";
        }
        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<>(100);
        //网站用户id
        param.put("user_id", dto.getDeviceId());
        //web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
        param.put("client_type", os);
        //传输用户请求验证时所携带的IP
        param.put("ip_address", dto.getIp());
        return param;
    }

    public void secondVerify(GeeTestDto dto) {
        GeetestLib gtSdk = getTrue(dto.getAppName(),dto.getIfNative());
        String challenge = dto.getChallenge();
        String validate = dto.getValidate();
        String secCode = dto.getSecCode();

        //从session中获取gt-server状态
        Integer status = (Integer)redisTemplate.opsForValue().get(gtSdk.gtServerStatusSessionKey + dto.getDeviceId());
        int gt_server_status_code = status;
        //自定义参数,可选择添加
        HashMap<String, String> param = setParam(dto);
        int gtResult;
        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, secCode, param);
            logger.info("gettest second respone is:" + gtResult);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            logger.error("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, secCode);
            logger.info(String.valueOf(gtResult));
        }

        if (gtResult == 1) {
            // 验证成功
            JSONObject data = new JSONObject();
            try {
                data.put("status", "success");
                data.put("version", gtSdk.getVersionInfo());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            logger.info(data.toString());
        } else {
            // 验证失败
            JSONObject data = new JSONObject();
            try {
                data.put("status", "fail");
                data.put("version", gtSdk.getVersionInfo());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            logger.info(data.toString());
        }
    }


    private GeetestLib getTrue(String appName,String ifNative) {
        boolean newBack;
        if ("true".equals(geeTestConfig.getNewfailback())) {
            newBack = true;
        } else {
            newBack = false;
        }
        GeetestConfig geetestConfig = mapper.selectByCodeAndType(appName, ifNative);
        return new GeetestLib(geetestConfig.getGeetestId(), geetestConfig.getGeetestKey(), newBack);
    }

    public int onePass(GeeTestDto dto){
        try {
            if (StringUtils.isBlank(dto.getAppName())){
                dto.setAppName("xxd");
            }
            GeetestConfig geetestConfig = mapper.selectByCodeAndType(dto.getAppName(), "1");
            if (StringUtils.isBlank(geetestConfig.getOnePassId())){
                geetestConfig = mapper.selectByCodeAndType("xxd","1");
            }
            GmessageLib gm = new GmessageLib(geetestConfig.getOnePassId(), geetestConfig.getOnePassKey());
            int result = gm.checkGateway(dto.getAccountNumber(), dto.getProcess_id(), dto.getAccesscode());
            if (result == 0) {
                return 1;
            }
        }catch (Exception e){
            return 0;
        }
        return 0;
    }
}
