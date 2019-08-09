package com.nyd.capital.service.wsm;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Cong Yuxiang
 * 2017/11/13
 **/
public class MyHostnameVerifier implements HostnameVerifier{
    public MyHostnameVerifier() {
    }

    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}
