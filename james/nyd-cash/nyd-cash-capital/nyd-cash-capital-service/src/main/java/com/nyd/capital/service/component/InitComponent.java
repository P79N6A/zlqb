package com.nyd.capital.service.component;

import com.nyd.capital.service.ICacheService;
import com.nyd.capital.service.SchedulerService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Cong Yuxiang
 * 2017/11/21
 **/
@Component
public class InitComponent implements ApplicationContextAware{
    @Autowired
    private ICacheService cacheService;
    @Autowired
    private SchedulerService schedulerService;

    public static ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
//        System.out.println("init");
//        cacheService.deleteKey("wsm-tmp");
        System.out.println("**********scheduler**********");

       /* try {
            schedulerService.scheduleKzjr();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        InitComponent.applicationContext = applicationContext;
    }
}
