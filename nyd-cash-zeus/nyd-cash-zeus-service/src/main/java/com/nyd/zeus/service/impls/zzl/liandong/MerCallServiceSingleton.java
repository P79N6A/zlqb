package com.nyd.zeus.service.impls.zzl.liandong;

import com.umf.api.service.UmfService;
import com.umf.api.service.UmfServiceImpl;

public class MerCallServiceSingleton {  

    // 定义一个私有的构造方法
    private MerCallServiceSingleton() {  
    }  
    //分别传入商户号和商户私钥的证书地址
    private static final UmfService instance =  new UmfServiceImpl("60000100","G:/tecent/test/60000100商户签名证书/60000100_.key.p8");
    //private static final UmfService instance =  new UmfServiceImpl("60038402","G:/tecent/test/60000100商户签名证书/60038402_.key.p8");
    //private static final UmfService instance =  new UmfServiceImpl("6245","G:/tecent/test/60000100商户签名证书/6245_.key.p8");
    //private static final UmfService instance =  new UmfServiceImpl("41609526","G:/tecent/test/60000100商户签名证书/41609526_.key.p8");
    //private static final UmfService instance =  new UmfServiceImpl("60016109","G:/tecent/test/60000100商户签名证书/60016109_.key.p8");
    
    // 静态方法返回该类的实例
    public static UmfService getInstancei() {  
        return instance;  
    }  
  
}
