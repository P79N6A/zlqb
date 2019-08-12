package com.nyd.user.service.mq;

import com.alibaba.fastjson.JSON;
import com.nyd.user.dao.mapper.HitLibraryUserMapper;
import com.nyd.user.dao.mapper.UserSourceMapper;
import com.nyd.user.entity.UserSource;
import com.nyd.user.model.mq.UserSourceLogMessage;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 渠道引流撞库判断
 *
 * @author shaoqing.liu
 * @date 2018/7/3 17:25
 */
public class UserSourceLogProcesser implements RabbitmqMessageProcesser<UserSourceLogMessage> {

    private static Logger LOGGER = LoggerFactory.getLogger(UserSourceLogProcesser.class);


    @Autowired
    private UserSourceMapper userSourceMapper;



    @Autowired
    private HitLibraryUserMapper hitLibraryUserMapper;

    @Override
    public void processMessage(UserSourceLogMessage message) {
        LOGGER .info("UserSourceLogProcesser 进入渠道引流撞库判断处理方法 start param is：" + JSON.toJSONString(message));
        if (null == message) {
            return;
        }
        try {
            //判断是否撞库
            int hitLibraryCount = hitLibraryUserMapper.existHitLibraryUser(message.getAccountNumber());
            if(hitLibraryCount < 1 ){
                //如果没撞库。则记录数据
                //因为撞库是一天跑一次。如果是新用户 避免一天刷多笔判断 一个sourcelog表中两天内是否有重复记录。如果有重复记录。则不再记录
                UserSource selectSource = userSourceMapper.selectSource(message.getAccountNumber());
                if(null==selectSource){
                	UserSource userSource = new UserSource();
                    BeanUtils.copyProperties(message, userSource);
                    userSourceMapper.save(userSource);
                    LOGGER.info("UserSourceLogMapper insert"+ JSON.toJSON(userSource));
                }
            }else{
                LOGGER.info("手机号:{}已撞库",message.getAccountNumber());
            }
        }catch (Exception ex){
            LOGGER.error(String.format("UserSourceLogProcesser Error"), ex);
        }

    }

}
