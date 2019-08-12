package com.nyd.msg.service;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author liat.zhang@gmail.com
 * @date 2016年9月15日-下午9:19:23
 * @description <code>NettyServerBootstrap</code>服务器引导程序类，用于调度服务器启动接口
 * (springApplicationContext = "classpath*:mobanker-*-application.xml",springServletContext = "classpath*:mobanker-*-servlet.xml")
 */
@NettyBootstrap(springApplicationContext = "classpath:/nyd-msg-application.xml", springServletContext = "classpath:/nyd-msg-servlet.xml")
//@NettyBootstrap
@Env(profile = "")
public class NydDemoApplication extends DispatcherServlet implements ApplicationListener<ContextStartedEvent> {
    /**
     * 621778 8305100 363014
     *
     * @param args
     * @description <code>入口程序</code>
     */
    public static void main(String[] args) {
        TasfeApplication.run(NydDemoApplication.class, args);
    }

    //@Value("log.home")
    private String logHome;


    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        // event.getApplicationContext();
        // 配置中心读取log配置并存入System
        //System.setProperty("log_home",logHome);


    }
}
