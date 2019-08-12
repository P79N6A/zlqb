package com.nyd.product.dao;

import com.nyd.product.model.FundInfo;

import java.util.List;

/**
 * Created by zhujx on 2017/11/22.
 */
public interface FundDao {

    void save(FundInfo fundInfo) throws Exception;

    void update(FundInfo fundInfo) throws Exception;

    List<FundInfo> getFundInfoLs() throws Exception;
}
