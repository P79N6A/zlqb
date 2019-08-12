package com.creativearts.nyd.web;

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
@NettyBootstrap(springApplicationContext = "classpath:/com/nyd/pay/configs/ws/nyd-pay-application.xml", springServletContext = "classpath:/com/nyd/pay/configs/ws/nyd-pay-servlet.xml")
@Env(profile = "")
public class PayApplication extends DispatcherServlet implements ApplicationListener<ContextStartedEvent> {
    /**
     * 621778 8305100 363014
     *
     * @param args
     * @description <code>入口程序</code>
     */
    public static void main(String[] args) {
        System.out.println("Java进程可以向操作系统申请到的最大内存:"+(Runtime.getRuntime().maxMemory())/(1024*1024)+"M");
        System.out.println("Java进程空闲内存:"+(Runtime.getRuntime().freeMemory())/(1024*1024)+"M");
        System.out.println("Java进程现在从操作系统那里已经申请了内存:"+(Runtime.getRuntime().totalMemory())/(1024*1024)+"M");

        byte[] bys = new byte[1024*1024];//申请1M内存
        System.out.println("Java进程可以向操作系统申请到的最大内存:"+(Runtime.getRuntime().maxMemory())/(1024*1024)+"M");
        System.out.println("Java进程空闲内存:"+(Runtime.getRuntime().freeMemory())/(1024*1024)+"M");
        System.out.println("Java进程现在从操作系统那里已经申请了内存:"+(Runtime.getRuntime().totalMemory())/(1024*1024)+"M");

        TasfeApplication.run(PayApplication.class, args);
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
