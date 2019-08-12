package com.creativearts.nyd.pay.service;

import com.creativearts.nyd.pay.model.enums.PayStatus;

/**
 * Cong Yuxiang
 * 2018/4/19
 **/
public interface RedisProcessService {
    /**
     * 判断是否可以支付  用于还款时
     * @param billNo
     * @return
     */
    PayStatus checkIfPay(String billNo);

    void putPayStatusMember(String userId,String value);

    String getPayStatusMember(String userId);

    void delPayStatusMember(String userId);
}
