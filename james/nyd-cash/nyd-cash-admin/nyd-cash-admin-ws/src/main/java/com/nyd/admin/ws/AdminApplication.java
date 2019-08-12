package com.nyd.admin.ws;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author liat.zhang
 */
@NettyBootstrap(springApplicationContext = "classpath:/com/nyd/admin/configs/ws/nyd-admin-application.xml", springServletContext = "classpath:/com/nyd/admin/configs/ws/nyd-admin-servlet.xml")
@Env(profile = "")
public class AdminApplication extends DispatcherServlet implements ApplicationListener<ContextStartedEvent> {

    /**
     * @param args
     * 入口程序
     */
    public static void main(String[] args) {
        TasfeApplication.run(AdminApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        // event.getApplicationContext();
        // 配置中心读取log配置并存入System
        //System.setProperty("log_home",logHome);
    }
}
