package com.nyd.wsm.ws;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created by zhujx on 2017/12/6
 */
@NettyBootstrap(springApplicationContext = "classpath:/com/nyd/wsm/configs/ws/nyd-wsm-application.xml", springServletContext = "classpath:/com/nyd/wsm/configs/ws/nyd-wsm-servlet.xml")
@Env(profile = "")
public class WSMApplication extends DispatcherServlet implements ApplicationListener<ContextStartedEvent> {
    /**
     * @param args
     * @description <code>入口程序</code>
     */
    public static void main(String[] args) {
        TasfeApplication.run(WSMApplication.class, args);
       
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent contextStartedEvent) {

    }
}
