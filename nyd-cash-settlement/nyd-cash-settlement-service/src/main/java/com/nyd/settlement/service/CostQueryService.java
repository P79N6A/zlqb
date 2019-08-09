package com.nyd.settlement.service;

import com.github.pagehelper.PageInfo;
import com.nyd.settlement.model.dto.RecommendFeeQueryDto;
import com.nyd.settlement.model.dto.ValuationFeeQueryDto;
import com.nyd.settlement.model.vo.ValuationFeeVo;

public interface CostQueryService {
    PageInfo<ValuationFeeVo> queryValuationFee(ValuationFeeQueryDto dto);

    PageInfo queryRecommendFee(RecommendFeeQueryDto dto);
}
