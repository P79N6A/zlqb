package com.nyd.capital.model.kzjr.response;

import lombok.Data;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/13
 **/
@Data
public class KzjrCallbackResponse {
    private String channelCode;
    private String orderId;
    private String accountId;
    private String nonceStr;
    private String sign;
    private List<KzjrInvestorResponse> investorList;
    private String key;
}
