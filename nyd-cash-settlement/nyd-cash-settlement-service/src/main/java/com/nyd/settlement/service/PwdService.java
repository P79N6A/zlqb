package com.nyd.settlement.service;

import com.nyd.settlement.model.dto.PwdDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Cong Yuxiang
 * 2018/2/1
 **/
public interface PwdService {
    ResponseData validatePwd(PwdDto pwdDto);
}
