package com.nyd.zeus.dao;

import com.nyd.zeus.model.SettleAccount;

/**
 * @ClassName: SettleAccountDao
 * @Description: TODO
 * @author dengqingfeng
 * @date 2019年8月13日
 */
public interface SettleAccountDao {
    void save(SettleAccount settleAccount)throws Exception;
}
