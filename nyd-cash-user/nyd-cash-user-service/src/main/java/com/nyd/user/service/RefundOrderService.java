package com.nyd.user.service;

import com.nyd.user.model.RefundInfo;
import com.nyd.user.model.RefundOrderInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */

public interface RefundOrderService{
    ResponseData save(RefundOrderInfo refund) throws Exception;
    
    void sumbitRefundList(RefundInfo refund) throws Exception;
}
