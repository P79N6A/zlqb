package com.nyd.settlement.service;

import com.github.pagehelper.PageInfo;
import com.nyd.settlement.entity.refund.YmtRefund;
import com.nyd.settlement.model.dto.RecommendRefundDto;
import com.nyd.settlement.model.dto.RefundDetailDto;
import com.tasfe.framework.support.model.ResponseData;

public interface YmtRefundService {


    PageInfo findRefundDetail(RefundDetailDto dto);


    ResponseData addRecommendRefund(RecommendRefundDto dto);

    YmtRefund findByRefundFlowNo(String repayNo);
}
