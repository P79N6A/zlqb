//package com.nyd.admin.service.impl;
//
//import com.nyd.admin.dao.ds.DataSourceContextHolder;
//import com.nyd.admin.entity.Fund;
//import com.nyd.admin.model.FundInfo;
//import com.nyd.admin.model.FundInfoQueryVo;
//import com.nyd.admin.model.enums.FundSourceEnum;
//import com.nyd.admin.service.IFundService;
//import com.nyd.admin.service.aspect.RoutingDataSource;
//import com.nyd.admin.service.utils.DateConverter;
//import com.tasfe.framework.crud.api.criteria.Criteria;
//import com.tasfe.framework.crud.api.enums.Operator;
//import com.tasfe.framework.support.service.impls.CrudServiceImpl;
//import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.beanutils.ConvertUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * Cong Yuxiang
// * 2017/11/30
// **/
//@Service
//public class FundServiceUnUse extends CrudServiceImpl<FundInfo,Fund,Long> implements IFundService{
//    Logger logger = LoggerFactory.getLogger(FundServiceUnUse.class);
//
//    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
//    @Override
//    public List<FundInfo> queryByCondition(FundInfoQueryVo vo) {
//        if(vo==null||(vo.getFundCode()==null && vo.getStatusCode()==null)){
//            return new ArrayList<>();
//        }
//        Criteria criteria = Criteria.from(Fund.class);
//        if(vo.getStatusCode()!=null){
//            criteria.where().and("is_in_use",Operator.EQ,vo.getStatusCode());
//        }
//        if(vo.getFundCode()!=null){
//            criteria.where().and("fund_code", Operator.EQ, vo.getFundCode());
//        }
//        criteria.where().endWhere();
//
//        return this.find(criteria);
//
//    }
//
//    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
//    @Override
//    public boolean saveFundIno(FundInfo info) {
//        if(info==null){
//
//            logger.error("资金信息为空");
//        }
//        info.setFundCode(FundSourceEnum.getFundSourceCode(info.getFundName()));
//        Fund fund = new Fund();
//        ConvertUtils.register(new DateConverter(), Date.class);
//        try {
//            BeanUtils.copyProperties(fund,info);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            return false;
////            logger.error(e.getMessage());
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//            return false;
//        }
//        this.save(fund);
//        return true;
//
//    }
//}
