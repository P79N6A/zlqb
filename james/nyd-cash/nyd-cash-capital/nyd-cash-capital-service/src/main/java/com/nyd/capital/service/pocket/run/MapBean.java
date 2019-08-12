package com.nyd.capital.service.pocket.run;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.service.UserPocketService;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.pocket.service.Pocket2Service;
import com.nyd.capital.service.pocket.util.PocketConfig;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.UserInfo;
import com.tasfe.framework.support.model.ResponseData;
import lombok.Data;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuqiu
 */
@Component
@Data
public class MapBean {
    private static final Logger logger = LoggerFactory.getLogger(MapBean.class);
    private ConcurrentHashMap<String, WebDriver> concurrentHashMap;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private Pocket2Service pocket2Service;
    @Autowired
    private UserPocketService userPocketService;
    @Autowired
    private UserIdentityContract userIdentityContract;
    @Autowired
    private PocketConfig pocketConfig;

    @PostConstruct
    private void initMapBean() {
        concurrentHashMap = new ConcurrentHashMap();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Enumeration<String> keys = concurrentHashMap.keys();
                        List<String> list = new ArrayList<>();
                        while (keys.hasMoreElements()) {
                            String key = keys.nextElement();
                            WebDriver webDriver = concurrentHashMap.get(key);
                            Object result = redisTemplate.opsForValue().get(OpenPageConstant.OPEM_PAGE_REDIS_CODE + key);
                            if (result != null) {
                                JSONObject jsonObject = JSON.parseObject(result.toString());
                                String code = jsonObject.getString("code");
                                String userId = jsonObject.getString("userId");
                                ResponseData<UserInfo> userInfo = userIdentityContract.getUserInfo(userId);
                                if (!OpenPageConstant.STATUS_ZERO.equals(userInfo.getStatus())){
                                    return;
                                }
                                UserInfo data = userInfo.getData();
                                String idNumber = data.getIdNumber();
                                list.add(key);
                                logger.info("the sms code is:" + code);
                                MsgCodeRunnableVo vo = new MsgCodeRunnableVo();
                                vo.setDriver(webDriver);
                                vo.setCode(code);
                                vo.setPocket2Service(pocket2Service);
                                vo.setUserPocketService(userPocketService);
                                vo.setUserId(userId);
                                vo.setIdNumber(idNumber);
                                vo.setPocketConfig(pocketConfig);
                                vo.setRedisTemplate(redisTemplate);
                                //进入提交验证码的处理
                                MsgCodeRunnable runnable = new MsgCodeRunnable(vo);
                                logger.info("Enters the processing thread that submits the captcha:" + key);
                                threadPoolTaskExecutor.execute(runnable);
                                redisTemplate.delete(OpenPageConstant.OPEM_PAGE_REDIS_CODE + key);
                            }
                        }
                        for (String key : list) {
                            concurrentHashMap.remove(key);
                        }
                        Thread.sleep(3000);
                    } catch (Throwable e) {
                        logger.error("Enters the processing thread that submits the captcha has exception" + e.getMessage());
                    }
                }
            }
        });
        thread.start();
    }
}
