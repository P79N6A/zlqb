package com.nyd.capital.ws;

/**
 * Cong Yuxiang
 * 2017/11/21
 **/
public class DaemonThread implements Runnable{
    @Override
    public void run() {
        while (true) {
            System.out.println("!!!!!!!!!!daemon");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
