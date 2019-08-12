package com.creativearts.nyd.pay.service.baofoo;

import com.creativearts.nyd.pay.model.baofoo.PreOrderPayVo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 非dubbo服务
 * Cong Yuxiang
 * 2017/12/12
 **/
public interface BaofooPayService {
    /**
     * 宝付快捷支付预支付
     * @param vo
     * @return
     */
    ResponseData quickReadyPayBf(PreOrderPayVo vo);

    /**
     * 宝付快捷支付
     */
    ResponseData quickPayBf();
}
