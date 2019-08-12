package com.tasfe.zh.web.server;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;

/**
 * Created by Lait on 2017/8/10.
 */
@NettyBootstrap
@Env(profile = "classpath:/com/tasfe/zh/configs/base/ws/properties/base-local-properties.xml")
public class ZhApplication {
    public static void main(String[] args) {
        TasfeApplication.run(ZhApplication.class,args);
    }
}
