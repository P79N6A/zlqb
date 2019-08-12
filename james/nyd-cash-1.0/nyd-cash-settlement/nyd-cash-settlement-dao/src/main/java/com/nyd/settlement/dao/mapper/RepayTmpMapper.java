package com.nyd.settlement.dao.mapper;

import com.nyd.settlement.dao.CrudDao;
import com.nyd.settlement.entity.repay.RepaySettleTmp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * yuxiang.cong
 */
@Mapper
public interface RepayTmpMapper extends CrudDao<RepaySettleTmp> {
    List<RepaySettleTmp> queryListByIds(List<Long> ids);

    void updateTmp(List<Long> ids);
}