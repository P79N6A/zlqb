package com.nyd.user.ws;

import com.creativearts.fx.agent.logback.EnableLogbackConfigure;
import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created by Dengw on 2017/11/9
 */
@EnableLogbackConfigure
@NettyBootstrap(springApplicationContext = "classpath:/com/nyd/user/configs/ws/nyd-user-application.xml", springServletContext = "classpath:/com/nyd/user/configs/ws/nyd-user-servlet.xml")
@Env(profile = "")
public class UserApplication extends DispatcherServlet implements ApplicationListener<ContextStartedEvent> {
    /**
     * @param args
     * @description <code>入口程序</code>
     */
    public static void main(String[] args) {
        TasfeApplication.run(UserApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        // event.getApplicationContext();
        // 配置中心读取log配置并存入System
        //System.setProperty("log_home",logHome);
    }
}
