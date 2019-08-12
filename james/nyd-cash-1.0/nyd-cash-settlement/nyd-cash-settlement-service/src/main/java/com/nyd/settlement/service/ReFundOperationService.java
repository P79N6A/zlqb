package com.nyd.settlement.service;

import com.nyd.settlement.entity.refund.Refund;
import com.nyd.settlement.model.dto.refund.DoRefundDto;

/**
 * Created by hwei on 2018/1/17.
 */
public interface ReFundOperationService {
    void saveRefund(Refund refund) throws Exception;

    void updateBillRefundAmount(DoRefundDto doRefundDto) throws Exception;
}
