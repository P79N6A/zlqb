package com.nyd.capital.ws;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author liat.zhang@gmail.com
 * @date 2016年9月15日-下午9:19:23
 * @description <code>NettyServerBootstrap</code>服务器引导程序类，用于调度服务器启动接口
 * (springApplicationContext = "classpath*:mobanker-*-application.xml",springServletContext = "classpath*:mobanker-*-servlet.xml")
 */
@NettyBootstrap(springApplicationContext = "classpath:/com/nyd/capital/configs/ws/nyd-capital-application.xml", springServletContext = "classpath:/com/nyd/capital/configs/ws/nyd-capital-servlet.xml")
//@NettyBootstrap
@Component
@Env(profile = "")
public class CapitalApplication extends DispatcherServlet implements ApplicationListener {
    /**
     * 621778 8305100 363014
     *
     * @param args
     * @description <code>入口程序</code>
     */
    public static void main(String[] args) {
        TasfeApplication.run(CapitalApplication.class, args);

//        Runtime.getRuntime().addShutdownHook(new Thread(){
//            @Override
//            public void run() {
//                int i =0;
//                while (i<5) {
//                    i++;
//                    System.out.println("1234");
//                }
//            }
//        });
    }

    //@Value("log.home")
    private String logHome;


//    @Override
//    public void onApplicationEvent(ContextClosedEvent event) {
////        System.out.println("stopped");
//        // event.getApplicationContext();
//        // 配置中心读取log配置并存入System
//        //System.setProperty("log_home",logHome);
//
//
//    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
//        if(event instanceof ContextClosedEvent){
//                        System.out.println(event.getClass().getSimpleName()+" 事件已发生！");
//                    }else if(event instanceof ContextRefreshedEvent){//如果是容器关闭事件
//                        System.out.println(event.getClass().getSimpleName()+" 事件已发生！");
//                     }else if(event instanceof ContextStartedEvent){
//                         System.out.println(event.getClass().getSimpleName()+" 事件已发生！");
//                     }else if(event instanceof ContextStoppedEvent){
//                         System.out.println(event.getClass().getSimpleName()+" 事件已发生！");
//                     }else{
//                         System.out.println("有其它事件发生:"+event.getClass().getName());
//                    }
    }
}
