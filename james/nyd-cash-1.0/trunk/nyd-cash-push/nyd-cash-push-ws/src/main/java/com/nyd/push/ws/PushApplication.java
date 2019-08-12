package com.nyd.push.ws;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created by zhujx on 2017/12/6
 */
@NettyBootstrap(springApplicationContext = "classpath:/com/nyd/push/configs/ws/nyd-push-application.xml", springServletContext = "classpath:/com/nyd/push/configs/ws/nyd-push-servlet.xml")
@Env(profile = "")
public class PushApplication extends DispatcherServlet implements ApplicationListener<ContextStartedEvent> {
    /**
     * @param args
     * @description <code>入口程序</code>
     */
    public static void main(String[] args) {
        TasfeApplication.run(PushApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent contextStartedEvent) {

    }
}
