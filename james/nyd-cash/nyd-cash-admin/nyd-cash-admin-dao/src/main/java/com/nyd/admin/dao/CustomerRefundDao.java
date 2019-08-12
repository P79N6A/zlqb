package com.nyd.admin.dao;

import com.nyd.admin.entity.RefundAuditLog;

/**
 * @Author: wucx
 * @Date: 2018/11/2 19:13
 */
public interface CustomerRefundDao {

    void save(RefundAuditLog refundAuditLog) throws Exception;
}
