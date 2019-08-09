package com.nyd.admin.service;

import com.nyd.admin.model.FundInfo;
import com.nyd.admin.model.FundInfoQueryVo;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/11/30  extends CrudService<FundInfo,Fund,Long> 
 **/
public interface IFundService{
    List<FundInfo> queryByCondition(FundInfoQueryVo vo);

    boolean saveFundIno(FundInfo info);
}
