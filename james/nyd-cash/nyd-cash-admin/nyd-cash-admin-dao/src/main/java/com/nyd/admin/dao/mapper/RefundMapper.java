package com.nyd.admin.dao.mapper;

import com.nyd.admin.model.dto.RefundApplyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RefundMapper {
	

    List<RefundApplyDto> getRefundApplyList(RefundApplyDto apply);
    
    RefundApplyDto getRefundApplyByRequestNo(@Param("requestNo") String requestNo);
    
    //void update (RefundApplyDto apply);
    /**
     *查询个数
     * @param refundApplyDto
     * @return
     */
    Integer findCount(RefundApplyDto refundApplyDto);

    List<RefundApplyDto> findRefundDetails(RefundApplyDto refundApplyDto);

    /**
     * 财务查询
     * @param refundApplyDto
     * @return
     */
    List<RefundApplyDto> financeQueryRefundDetails(RefundApplyDto refundApplyDto);

    Integer financeQueryRefundCount(RefundApplyDto refundApplyDto);


}
