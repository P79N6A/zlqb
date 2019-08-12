package com.nyd.user.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nyd.user.model.RefundApplyInfo;

@Repository
public interface RefundApplyMapper {
	
    void insert(RefundApplyInfo apply);

    List<RefundApplyInfo> getRefundApplyList(RefundApplyInfo apply);
    List<RefundApplyInfo> jugeList(RefundApplyInfo apply);
    
    RefundApplyInfo getRefundApplyByRequestNo(@Param("requestNo") String requestNo);
    
    void update (RefundApplyInfo apply);

}
