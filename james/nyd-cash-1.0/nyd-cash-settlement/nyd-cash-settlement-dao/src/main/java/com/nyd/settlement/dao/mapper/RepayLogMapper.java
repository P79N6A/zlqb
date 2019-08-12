package com.nyd.settlement.dao.mapper;

import com.nyd.settlement.entity.repay.TRepayLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2018/1/17
 **/
@Mapper
public interface RepayLogMapper {

    List<TRepayLog> selectRepayDetail(Map map);

}
