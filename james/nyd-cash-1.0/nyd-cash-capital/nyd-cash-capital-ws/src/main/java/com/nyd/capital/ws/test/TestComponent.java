package com.nyd.capital.ws.test;

import com.nyd.capital.ws.DaemonThread;
import com.nyd.capital.ws.UserThread;

/**
 * Cong Yuxiang
 * 2017/11/21
 **/
/*@Component*/
public class TestComponent {
//    @PostConstruct
    public void init(){
        Thread t1 = new Thread(new DaemonThread());
        Thread t2 = new Thread(new UserThread());
        t1.setDaemon(true);
        t1.start();
        t2.start();
    }
}
