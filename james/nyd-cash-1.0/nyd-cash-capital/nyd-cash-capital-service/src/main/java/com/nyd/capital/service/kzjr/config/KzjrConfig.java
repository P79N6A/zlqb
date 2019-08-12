package com.nyd.capital.service.kzjr.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Cong Yuxiang
 * 2017/12/11
 **/
@Configuration
public @Data
class KzjrConfig {
    @Value("${kzjr.channel.code}")
    private String channelCode;
    @Value("${kzjr.privatekey}")
    private String privateKey;

    @Value("${kzjr.url}")
    private String baseUrl;
    @Value("${kzjr.returnUrl}")
    private String returnUrl;

    @Value("${kzjr.openPageUrl}")
    private String openPageUrl;


    //银码头
    @Value("${kzjr.returnUrl.ymt}")
    private String returnUrlYmt;


    @Value("${kzjr.openpage.error.nyd.url}")
    private String pageErrorUrlNyd;

    @Value("${kzjr.openpage.error.ymt.url}")
    private String pageErrorUrlYmt;

    @Value("${kzjr.bank.url}")
    private String bankUrl;

    @Value("${driver.location}")
    private String driverLoc;

    /*@Value("${kzjr.openpage.towt.nyd.url}")
    private String toWtNyd;

    @Value("${kzjr.openpage.towt.ymt.url}")
    private String toWtYmt;*/


}
