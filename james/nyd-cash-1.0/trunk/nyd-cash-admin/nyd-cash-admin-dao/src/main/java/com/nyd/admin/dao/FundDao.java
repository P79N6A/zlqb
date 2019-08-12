package com.nyd.admin.dao;


import com.nyd.admin.model.FundInfo;
import com.nyd.admin.model.FundInfoQueryVo;

import java.util.List;

/**
 * Created by zhujx on 2017/11/22.
 */
public interface FundDao {

    boolean save(FundInfo fundInfo) throws Exception;

    void update(FundInfo fundInfo) throws Exception;

    List<FundInfo> getFundInfoLs() throws Exception;

    List<FundInfo> queryFundInfoByCondition(FundInfoQueryVo vo) throws Exception;
}
