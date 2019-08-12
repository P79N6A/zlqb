package com.nyd.admin.dao.mapper;

import com.nyd.admin.dao.CrudDao;
import com.nyd.admin.entity.FundDetail;
import com.nyd.admin.entity.FundInfo;
import com.nyd.admin.model.fundManageModel.FundDetailModel;
import com.nyd.admin.model.fundManageModel.FundInfoModel;

import java.util.List;


/**
 * @author Peng
 * @create 2018-01-02 14:39
 **/
public interface FundDetailMapper extends CrudDao<FundDetail> {
    List<FundDetailModel> queryFundDetail(FundDetail fundDetail);
}
