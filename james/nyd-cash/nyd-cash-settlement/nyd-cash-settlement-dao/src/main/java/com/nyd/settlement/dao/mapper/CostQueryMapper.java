package com.nyd.settlement.dao.mapper;

import com.nyd.settlement.model.dto.RecommendFeeQueryDto;
import com.nyd.settlement.model.dto.ValuationFeeQueryDto;
import com.nyd.settlement.model.po.RecommendFeePo;
import com.nyd.settlement.model.po.ValuationFeePo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CostQueryMapper {
    List<ValuationFeePo> findValuationFee(ValuationFeeQueryDto dto);


    List<RecommendFeePo> findRecommendFee(RecommendFeeQueryDto dto);
}
