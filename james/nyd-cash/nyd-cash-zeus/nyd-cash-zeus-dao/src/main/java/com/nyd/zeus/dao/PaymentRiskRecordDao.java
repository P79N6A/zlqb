package com.nyd.zeus.dao;


import java.util.List;

import com.nyd.zeus.model.PaymentRiskRecordVo;

/**
 * Created by zhujx on 2017/11/18.
 */
public interface PaymentRiskRecordDao {

    void save(PaymentRiskRecordVo vo) throws Exception;

    void update(PaymentRiskRecordVo vo) throws Exception;

    PaymentRiskRecordVo getObjectById(String id) throws Exception;
    List<PaymentRiskRecordVo> getReadyData() throws Exception;
    List<PaymentRiskRecordVo> getProcessData() throws Exception;
}
