package com.nyd.capital.service.impl;

import com.nyd.capital.dao.ds.DataSourceContextHolder;
import com.nyd.capital.dao.mappers.FundMapper;
import com.nyd.capital.entity.Fund;
import com.nyd.capital.service.CrudService;
import com.nyd.capital.service.FundSourceService;
import com.nyd.capital.service.aspect.RoutingDataSource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/8
 **/
@Service
public class FundSourceServiceImpl extends CrudService<FundMapper,Fund>  implements FundSourceService{

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public List<Fund> queryUseSource() {
        Fund fund = new Fund();
        fund.setDeleteFlag(0);
        fund.setIsInUse(0);
        return this.findList(fund);
    }
}
