package com.nyd.admin.dao;

import com.nyd.admin.model.fundManageModel.FundConfigModel;

import java.util.List;

/**
 * Created by hwei on 2017/12/29.
 */
public interface FundConfigDao {
    List<FundConfigModel>  getFundConfig() throws Exception;
}
