package com.nyd.user.dao;

import com.nyd.user.model.ClickInfo;

/**
 * Created by zhujx on 2017/12/12.
 */
public interface ClickDao {

    void save(ClickInfo clickInfo) throws Exception;
}
