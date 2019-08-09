package com.nyd.admin.service.impl;

import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.FundMapper;
import com.nyd.admin.entity.Fund;
import com.nyd.admin.model.FundInfo;
import com.nyd.admin.model.FundInfoQueryVo;
import com.nyd.admin.service.CrudService;
import com.nyd.admin.service.IFundService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.FundMapStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/11/30
 **/
@Service
public class FundService extends CrudService<FundMapper,Fund> implements IFundService{
    Logger logger = LoggerFactory.getLogger(FundService.class);

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public List<FundInfo> queryByCondition(FundInfoQueryVo vo) {
        Fund fund = new Fund();
        fund.setFundCode(vo.getFundCode());
        fund.setIsInUse(vo.getIsInUse());
       List<Fund> result = this.getDao().findList(fund);
        return FundMapStruct.INSTANCE.poList2VoList(result);

    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public boolean saveFundIno(FundInfo info) {
        if(info==null){

            logger.error("资金信息为空");
        }
        Fund fund = FundMapStruct.INSTANCE.vo2Po(info);
       this.getDao().insert(fund);
        return true;

    }
}
