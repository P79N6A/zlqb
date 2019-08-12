package com.nyd.user.api;

import com.nyd.user.model.RefundApplyInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.UserDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
public interface RefundApplyApi {
	
    
    ResponseData save(RefundApplyInfo info);
    
    ResponseData update(RefundApplyInfo info);
    
    ResponseData getRefundApplyList(RefundApplyInfo apply);

}
