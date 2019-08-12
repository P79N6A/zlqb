package com.nyd.settlement.ws;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created by Dengw on 2017/1/9
 */
@NettyBootstrap(springApplicationContext = "classpath:/com/nyd/settlement/configs/ws/nyd-settlement-application.xml", springServletContext = "classpath:/com/nyd/settlement/configs/ws/nyd-settlement-servlet.xml")
@Env(profile = "")
public class SettlementApplication extends DispatcherServlet implements ApplicationListener<ContextStartedEvent> {
    /**
     * @param args
     * @description <code>入口程序</code>
     */
    public static void main(String[] args) {
        TasfeApplication.run(SettlementApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        // event.getApplicationContext();
        // 配置中心读取log配置并存入System
        //System.setProperty("log_home",logHome);
    }
}
