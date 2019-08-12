package com.nyd.admin.dao;

import com.nyd.admin.entity.CustomerServiceLog;
import com.nyd.admin.model.Info.CustomerServiceLogInfo;

import java.util.List;

public interface CustomerServiceLogDao {
    void save(CustomerServiceLog customerServiceLog) throws Exception;

    List<CustomerServiceLogInfo> findCustomerServiceListByUserId(String userId) throws Exception;
}
