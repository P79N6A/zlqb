package com.nyd.user.service;

import com.nyd.user.model.RefundApplyInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
public interface RefundApplyService {
	
    ResponseData apply(RefundApplyInfo info);
    
    ResponseData save(RefundApplyInfo info);
    
    ResponseData update(RefundApplyInfo info);
    
    ResponseData getRefundApplyList(RefundApplyInfo apply);

}
