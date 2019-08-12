package com.nyd.settlement.service;

import com.nyd.settlement.model.dto.QueryDto;
import com.nyd.settlement.model.dto.refund.AddRefundDto;
import com.nyd.settlement.model.dto.refund.DoRefundDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by hwei on 2018/1/15.
 */
public interface ReFundService {
    ResponseData queryNoReFund(QueryDto queryDto);

    ResponseData queryNoReFundDetail(QueryDto queryDto);

    ResponseData addReFund(AddRefundDto addRefundDto);

    ResponseData doReFund(DoRefundDto doRefundDto);

    ResponseData queryAlreadyReFund(QueryDto queryDto);
}
