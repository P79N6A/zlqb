package com.nyd.batch.ws;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created by zhujx on 2017/11/17
 */
@NettyBootstrap(springApplicationContext = "classpath:/com/nyd/batch/configs/ws/nyd-batch-application.xml", springServletContext = "classpath:/com/nyd/batch/configs/ws/nyd-batch-servlet.xml")
@Env(profile = "")
public class BatchApplication extends DispatcherServlet implements ApplicationListener<ContextStartedEvent> {
    /**
     * @param args
     * @description <code>入口程序</code>
     */
    public static void main(String[] args) {
        TasfeApplication.run(BatchApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {

    }
}
