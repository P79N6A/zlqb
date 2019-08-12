package com.nyd.settlement.dao;

import com.nyd.settlement.entity.refund.Refund;

/**
 * Created by hwei on 2018/1/17.
 */
public interface RefundDao {
    /**
     * 保存退款记录
     */
    void saveRefund(Refund refund) throws Exception;
}
