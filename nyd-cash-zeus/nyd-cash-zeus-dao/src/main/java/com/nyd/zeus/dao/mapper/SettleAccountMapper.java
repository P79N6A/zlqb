package com.nyd.zeus.dao.mapper;

import com.nyd.zeus.model.SettleAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * dengqingfeng
 * 2019/8/12
 **/
@Mapper
public interface SettleAccountMapper {
    void save(SettleAccount settleAccount);
}
