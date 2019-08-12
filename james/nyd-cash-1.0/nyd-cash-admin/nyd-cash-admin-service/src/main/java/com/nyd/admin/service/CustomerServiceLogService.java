package com.nyd.admin.service;

import com.nyd.admin.model.dto.CustomerServiceLogDto;
import com.tasfe.framework.support.model.ResponseData;

public interface CustomerServiceLogService {

    /**
     * 保存客服记录
     * @param customerServiceLogDto
     * @return
     */
    ResponseData saveCustomerServiceLog(CustomerServiceLogDto customerServiceLogDto);

    /**
     * 客服记录查询
     * @param userId
     * @return
     */
    ResponseData findCustomerServiceList(String userId);
}
