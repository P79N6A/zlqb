package com.nyd.capital.ws;

/**
 * Cong Yuxiang
 * 2017/11/21
 **/
public class UserThread implements Runnable{
    @Override
    public void run() {
        int i=0;
        while (i<5) {
            i++;
            System.out.println("#############user");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
