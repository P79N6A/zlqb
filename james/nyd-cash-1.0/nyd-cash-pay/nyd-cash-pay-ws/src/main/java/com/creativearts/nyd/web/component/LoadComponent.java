package com.creativearts.nyd.web.component;

import com.creativearts.nyd.pay.service.unionpay.sdk.CertUtil;
import com.creativearts.nyd.pay.service.unionpay.sdk.SDKConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Yuxiang Cong
 **/
@Component
public class LoadComponent {
    @PostConstruct
    public void initUnionPayConfig(){
        System.out.println("union pay starting");
       SDKConfig.getConfig().loadPropertiesFromSrc();
        CertUtil.init();
    }
}
