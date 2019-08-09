package com.nyd.settlement.dao.mapper;

import com.nyd.settlement.entity.refund.YmtRefund;
import com.nyd.settlement.model.dto.RefundDetailDto;
import com.nyd.settlement.model.po.YmtRefundPo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YmtRefundMapper {
    List<YmtRefundPo> selectRefundDetail(RefundDetailDto dto);

    void saveRecommendRefund(YmtRefund ymtRefund);

    YmtRefund selectByRefundFlowNo(String repayNo);
}
