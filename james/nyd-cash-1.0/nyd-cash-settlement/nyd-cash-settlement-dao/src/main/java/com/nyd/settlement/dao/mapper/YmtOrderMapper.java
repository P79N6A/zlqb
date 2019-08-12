package com.nyd.settlement.dao.mapper;

import com.nyd.settlement.entity.YmtOrder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YmtOrderMapper {
    List<YmtOrder> selectByRepayNo(String repayNo);
}
