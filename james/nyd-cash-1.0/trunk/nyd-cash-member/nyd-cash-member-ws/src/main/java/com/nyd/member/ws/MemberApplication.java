package com.nyd.member.ws;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created by Dengw on 2017/11/9
 */
@NettyBootstrap(springApplicationContext = "classpath:/com/nyd/member/configs/ws/nyd-member-application.xml", springServletContext = "classpath:/com/nyd/member/configs/ws/nyd-member-servlet.xml")
@Env(profile = "")
public class MemberApplication extends DispatcherServlet implements ApplicationListener<ContextStartedEvent> {
    /**
     * @param args
     * @description <code>入口程序</code>
     */
    public static void main(String[] args) {
        TasfeApplication.run(MemberApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        // event.getApplicationContext();
        // 配置中心读取log配置并存入System
        //System.setProperty("log_home",logHome);
    }
}
