package com.creativearts.nyd.pay.service.yinshengbao.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class YsbProperties {

    @Value("${ysb.accountId}")
    private String accountId;

    @Value("${ysb.contractId}")
    private String contractId;

    @Value("${ysb.key}")
    private String key;

    @Value("${ysb.sign.url}")
    private String AgreementURL;

    @Value("${ysb.daikou.url}")
    private String DaiKouURL;

    @Value("${ysb.orderquery.url}")
    private String OrderQueryURL;

    @Value("${ysb.subContractId.url}")
    private String SubContractIdURL;

    @Value("${ysb.subContractIdDelay.url}")
    private String SubContractIdDelayURL;

    @Value("${ysb.daikouResultNotify.url}")
    private String DaiKouResultNotifyURL;

}
