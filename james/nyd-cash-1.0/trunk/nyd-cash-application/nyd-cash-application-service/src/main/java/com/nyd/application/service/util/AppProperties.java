package com.nyd.application.service.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Dengw on 2017/11/16
 */
@Component
@Getter
public class AppProperties {
    //七牛
    @Value("${qiniu.accessKey}")
    private  String qnAccessKey;

    @Value("${qiniu.secretKey}")
    private  String qnSecretKey;

    @Value("${qiniu.Zone}")
    private  String qnZone;

    @Value("${qiniu.bucket}")
    private  String qnBbucket;

    @Value("${qiniu.domainOfBucket}")
    private  String qnDomainOfBucket;

    @Value("${qiniu.contractBucket}")
    private  String qnContractBucket;

    @Value("${qiniu.expireInSeconds}")
    private  String qnExpireInSeconds;

    //法大大
    @Value("${fadada.api.url}")
    private  String fadadaUrl;

    @Value("${fadada.api.appId}")
    private  String fadadaAppId;

    @Value("${fadada.api.appSecret}")
    private  String fadadaAppSecret;

    @Value("${fadada.api.kehuNumber}")
    private  String fadadaKehuNumber;

    @Value("${fadada.api.version}")
    private  String fadadaVersion;

    @Value("${fadada.api.notifyUrl}")
    private  String fadadaNotifyUrl;

    @Value("${app.showCopyRight}")
    private  String appShowCopyRight;

    @Value("${pdf.fontInfo}")
    private  String pdfFontInfo;

    @Value("${pdf.templatePath}")
    private  String pdfTemplatePath;

    @Value("${jiguang.push.url}")
    private String jiguangPushUrl;
}
