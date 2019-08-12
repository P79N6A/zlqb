package com.nyd.pay.api.service;

import com.creativearts.nyd.pay.model.helibao.CreateOrderVo;
import com.nyd.pay.api.enums.WithHoldType;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Cong Yuxiang
 * 2017/12/12
 **/
public interface PayService {
    //代扣
    ResponseData withHold(CreateOrderVo vo, WithHoldType type);

    //代付
    ResponseData daiFu();

    /**
     * 查询代扣结果
     * @return
     */
    ResponseData selectWithholdResult();

}
