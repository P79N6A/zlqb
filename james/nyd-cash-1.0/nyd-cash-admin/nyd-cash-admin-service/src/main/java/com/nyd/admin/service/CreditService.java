package com.nyd.admin.service;

import com.nyd.admin.model.dto.CreditDto;
import com.nyd.admin.model.dto.CreditRemarkDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @Author: wucx
 * @Date: 2018/10/16 21:10
 */
public interface CreditService {

    /**
     * 授信查询
     * @param creditDto
     * @return
     */
    ResponseData findCreditDetails(CreditDto creditDto);

    /**
     * 授信操作
     * @param creditRemarkDto
     * @return
     */
    ResponseData updateCreditRemark(CreditRemarkDto creditRemarkDto);
}
