package com.nyd.zeus.model.helibao.util;



import java.security.PublicKey;

/**
 * Created by mask on 2019/2/16
 */
public class Test {

    private static final String CERT_PATH = "d:/helipay.cer";    //合利宝cert

    public static void main(String[] args) throws Exception {
        PublicKey publicKey = RSA.getPublicKeyByCert(CERT_PATH);

        System.out.println(publicKey);
    }
}
