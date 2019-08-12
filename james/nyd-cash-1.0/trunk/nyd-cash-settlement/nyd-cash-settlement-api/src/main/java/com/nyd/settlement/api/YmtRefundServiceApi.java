package com.nyd.settlement.api;

import com.nyd.settlement.model.dto.RecommendRefundDto;
import com.tasfe.framework.support.model.ResponseData;

public interface YmtRefundServiceApi {

    //推荐费退款记录保存到表中
    ResponseData addRecommendRefund(RecommendRefundDto dto);
}
