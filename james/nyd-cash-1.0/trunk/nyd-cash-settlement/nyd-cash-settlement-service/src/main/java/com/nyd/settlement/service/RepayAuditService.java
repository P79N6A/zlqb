package com.nyd.settlement.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.nyd.settlement.model.dto.repay.RepayAddDto;
import com.nyd.settlement.model.dto.repay.RepayAuditQueryDto;
import com.nyd.settlement.model.vo.repay.RepayAuditVo;

import java.util.List;

/**
 * Cong Yuxiang
 * 2018/1/16
 **/
public interface RepayAuditService {
    void save(RepayAddDto dto);

    PageInfo<RepayAuditVo> findAuditList(RepayAuditQueryDto repayAuditQueryDto);

    JSONObject audit(List<Long> ids,String updateBy);
}
