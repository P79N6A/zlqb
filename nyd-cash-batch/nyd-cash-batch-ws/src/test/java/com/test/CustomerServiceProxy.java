package com.test;

/**
 * Cong Yuxiang
 * 2018/1/31
 **/
public class CustomerServiceProxy implements ICustomerService{
    private ICustomerService customerService;

    public void setCustomerService(ICustomerService customerService) {
        this.customerService =customerService;
    }

    public void doSomething1(){
        doBefore();
        customerService.doSomething2();
        doAfter();
    }

    public void doSomething2(){
        doBefore();
        customerService.doSomething2();
        doAfter();
    }

    private void doBefore() {
        // 例如，可以在此处开启事务
        System.out.println("dosome important things before...");
    }

    private void doAfter() {
        // 例如，可以在此处提交或回滚事务、释放资源等等
        System.out.println("dosome important things after...");
    }

}
