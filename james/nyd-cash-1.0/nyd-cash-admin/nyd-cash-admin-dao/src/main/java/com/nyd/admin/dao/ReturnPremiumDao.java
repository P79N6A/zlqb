package com.nyd.admin.dao;

import com.nyd.admin.entity.ReturnPremium;

public interface ReturnPremiumDao {

    /**
     * 保存退费标记
     * @param returnPremium
     */
    void save(ReturnPremium returnPremium) throws Exception;
}
