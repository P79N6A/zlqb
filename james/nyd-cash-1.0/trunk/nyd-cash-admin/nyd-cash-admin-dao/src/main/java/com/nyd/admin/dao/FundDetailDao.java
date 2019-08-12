package com.nyd.admin.dao;

import com.nyd.admin.entity.FundDetail;

import java.util.List;

/**
 * Created by hwei on 2018/1/2.
 */
public interface FundDetailDao {
    void saveFundDetails(List<FundDetail> fundDetails) throws Exception;
}
