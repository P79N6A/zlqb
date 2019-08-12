package com.nyd.mq.ws;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created by zhujx on 2017/11/17
 */
@NettyBootstrap(springApplicationContext = "classpath:/com/nyd/mq/configs/ws/nyd-mq-application.xml", springServletContext = "classpath:/com/nyd/mq/configs/ws/nyd-mq-servlet.xml")
@Env(profile = "")
public class MqApplication extends DispatcherServlet implements ApplicationListener<ContextStartedEvent> {
    /**
     * @param args
     * @description <code>入口程序</code>
     */
    public static void main(String[] args) {
        TasfeApplication.run(MqApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {

    }
}
