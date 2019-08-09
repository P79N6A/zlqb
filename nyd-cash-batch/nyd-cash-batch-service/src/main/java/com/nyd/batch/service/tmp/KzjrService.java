package com.nyd.batch.service.tmp;

import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.model.kzjr.QueryAssetRequest;

/**
 * Cong Yuxiang
 * 2017/12/11
 **/
public interface KzjrService {


    //查询单个资产
    JSONObject queryAsset(QueryAssetRequest request);

}
