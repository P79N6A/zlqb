package com.nyd.capital.service.qcgz.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 七彩格子配置文件对应的实体类
 * @author cm
 */
@Configuration
@Data
public class QcgzConfig {

    @Value("${qcgz.channelCode}")
    private String channelCode;

    @Value("${qcgz.privatekey}")
    private String privateKey;

    @Value("${qcgz.submitAssetUrl}")
    private String submitAssetUrl;

    @Value("${qcgz.loanApplyUrl}")
    private String loanApplyUrl;

    @Value("${qcgz.queryLoanApplyResultUrl}")
    private String queryLoanApplyResultUrl;

    @Value("${qcgz.loanSucceesNotifyUrl}")
    private String loanSucceesNotifyUrl;

}
