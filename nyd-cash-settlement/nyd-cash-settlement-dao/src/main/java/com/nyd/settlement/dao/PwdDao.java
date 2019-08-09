package com.nyd.settlement.dao;

import com.nyd.settlement.entity.Pwd;

/**
 * Cong Yuxiang
 * 2018/2/1
 **/
public interface PwdDao {

    Pwd selectPwdByType(Integer type) throws Exception;
}
