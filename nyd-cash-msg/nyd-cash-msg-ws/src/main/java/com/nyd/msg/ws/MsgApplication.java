package com.nyd.msg.ws;

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
@NettyBootstrap(springApplicationContext = "classpath:/com/nyd/msg/configs/ws/nyd-msg-application.xml", springServletContext = "classpath:/com/nyd/msg/configs/ws/nyd-msg-servlet.xml")
@Env(profile = "")
public class MsgApplication extends DispatcherServlet implements ApplicationListener<ContextStartedEvent> {
    /**
     * @param args
     * @description <code>入口程序</code>
     */
    public static void main(String[] args) {
        TasfeApplication.run(MsgApplication.class, args);
    }



    @Override
    public void onApplicationEvent(ContextStartedEvent event) {

    }
}
