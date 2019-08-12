package com.tasfe.zh.base.ws;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;

/**
 * Created by Lait on 2017/8/1.
 */
@NettyBootstrap
@Env(profile = "classpath:/com/tasfe/sis/configs/base/ws/properties/base-local-properties.xml")
public class BaseApplication {

    public static void main(String[] args) {
        TasfeApplication.run(BaseApplication.class, args);
    }

}
