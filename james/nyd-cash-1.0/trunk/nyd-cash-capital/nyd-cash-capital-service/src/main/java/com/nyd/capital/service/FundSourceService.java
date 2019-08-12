package com.nyd.capital.service;

import com.nyd.capital.entity.Fund;

import java.util.List;

/**
 * 资金源获取
 * Cong Yuxiang
 * 2017/11/30  extends CrudService<FundInfo,Fund,Long> 
 **/
public interface FundSourceService {

    List<Fund> queryUseSource();

}
