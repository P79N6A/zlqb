package com.test;

import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2018/1/31
 **/
public class TestProxy {
    public static void main(String[] args) {
        // 创建代理目标对象。对于Spring来说，这一工作
        // 是由Spring DI容器完成的。
//        ICustomerService serviceProxyTarget = new CustomerServiceImpl();
//
//        // 创建代理对象。对于Spring来说，这一工作
//        // 也是由Spring DI容器完成的。
//        CustomerServiceProxy serviceProxy= new CustomerServiceProxy();
//        serviceProxy.setCustomerService(serviceProxyTarget);
//        ICustomerService serviceBean= (ICustomerService) serviceProxy;
//
//        // 调用业务逻辑操作
//        serviceBean.doSomething1();
        BigDecimal bigDecimal = new BigDecimal(-1);
        System.out.println(bigDecimal.add(new BigDecimal(1)));
    }
}
