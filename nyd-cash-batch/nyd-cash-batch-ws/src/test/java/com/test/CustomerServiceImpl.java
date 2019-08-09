package com.test;

/**
 * Cong Yuxiang
 * 2018/1/31
 **/
public class CustomerServiceImpl implements ICustomerService{
    @Override
    public void doSomething1() {
        System.out.println("InsideCustomerServiceImpl.doSomething1()");

        doSomething2();
    }

    @Override
    public void doSomething2() {
        System.out.println("InsideCustomerServiceImpl.doSomething2()");
    }
}
