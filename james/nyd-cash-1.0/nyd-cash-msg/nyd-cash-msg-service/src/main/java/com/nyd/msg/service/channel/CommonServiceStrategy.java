package com.nyd.msg.service.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.msg.service.utils.HttpsUtils;
import com.nyd.msg.service.utils.Message;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hwei on 2018/12/12.
 */
@Component
public class CommonServiceStrategy implements ChannelStrategy {

    private static Logger logger = LoggerFactory.getLogger(CommonServiceStrategy.class);

    @Override
    public boolean sendSms(Message message, boolean batch) {
        String commonLog = "【技术中心-大汉三通通道】";
        logger.info("{}发送短信，参数【{},{}】",commonLog, JSON.toJSONString(message),batch);
        if (message==null|| StringUtils.isBlank(message.getCellPhones())) {
            return false;
        }
        try {
            Map<String,Object> param = new HashMap();
            if (StringUtils.isNotBlank(message.getAppName())){
                param.put("appID",message.getAppName());
            } else {
                param.put("appID","nyd");
            }
            param.put("groupCode",1);
            param.put("mobilePhone",message.getCellPhones());
            param.put("sign","【"+message.getSign()+"】");
            String smsTem = message.getSmsTemplate().substring(message.getSmsTemplate().indexOf("】")+1);
            param.put("templateContent",smsTem);
            String result = doSendMsg(message.getSmsPlatUrl(),JSON.toJSONString(param));
            logger.info("CommonServiceStrategy 短信发送成功！结果是 "+result);
            if (result!=null) {
                JSONObject json = JSON.parseObject(result);
                if (json!=null&&"0".equals(json.getString("status"))) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("CommonServiceStrategy sendSms has exception!",e);
        }
        return false;
    }

    private String doSendMsg(String url, String jsonStr) {
        int retryCount=3;
        String result = null;
        do {
            try {
                result = HttpsUtils.sendPost(url,jsonStr);
                if (StringUtils.isNotBlank(result)) {
                    JSONObject jsonObject = JSON.parseObject(result);
                    return result;
                } else {
                    retryCount--;
                }
            } catch (Exception e) {
                logger.error("doSendMsg has exception",e);
                retryCount--;
            }
        } while (retryCount>0);
        return result;
    }
}
