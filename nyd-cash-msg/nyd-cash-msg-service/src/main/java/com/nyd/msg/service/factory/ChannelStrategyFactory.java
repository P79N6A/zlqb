package com.nyd.msg.service.factory;

import com.nyd.msg.service.channel.*;
import com.nyd.msg.service.code.ChannelEnum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ChannelStrategyFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    public  ChannelStrategy buildChannel(ChannelEnum code){
        switch (code.getCode()){
            case "1":
                return applicationContext.getBean(TianChangChannelStrategy.class);
            case "2":
                return applicationContext.getBean(MengWangChannelStrategy.class);
            case "3":
                return applicationContext.getBean(TianChangChannelStrategy.class);
            case "5":
                return applicationContext.getBean(DaHanTricomChannelStrategy.class);
            case "6":
                return applicationContext.getBean(CommonServiceStrategy.class);
            case "8":
                return applicationContext.getBean(UoleemChannelStrategy.class);
            case "10":
                return applicationContext.getBean(ChuangLanChannelStrategy.class);
            default:
                return applicationContext.getBean(DaHanTricomChannelStrategy.class);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
