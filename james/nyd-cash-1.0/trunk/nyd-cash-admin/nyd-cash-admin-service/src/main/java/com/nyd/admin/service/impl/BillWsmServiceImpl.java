package com.nyd.admin.service.impl;


import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.BillWsmDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.BillWsmMapper;
import com.nyd.admin.entity.BillWsm;
import com.nyd.admin.model.BillWsmVo;
import com.nyd.admin.service.BillWsmService;
import com.nyd.admin.service.CrudService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.BillWsmMapStruct;
import com.nyd.admin.service.utils.DateConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
@Service
public class BillWsmServiceImpl extends CrudService<BillWsmMapper,BillWsm> implements BillWsmService {
    @Autowired
    private BillWsmDao billWsmDao;

//    @Override
//    public void save(BillWsmVo billWsmVo){
//        ConvertUtils.register(new DateConverter(), Date.class);
//        BillWsm billWsm = new BillWsm();
//        try {
//            BeanUtils.copyProperties(billWsm,billWsmVo);
//            billWsmDao.save(billWsm);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//
//    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void saveList(List<BillWsmVo> billWsmVos, String tableName){
        ConvertUtils.register(new DateConverter(), Date.class);
        List<BillWsm> list = new ArrayList<>();
        for(BillWsmVo vo:billWsmVos){
            BillWsm billWsm = new BillWsm();
            try {
                BeanUtils.copyProperties(billWsm,vo);
                list.add(billWsm);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        billWsmDao.saveList(list,tableName);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void dropTable(String tableName) {
        billWsmDao.dropTable(tableName);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void createTable(String tableName) {
        billWsmDao.createTable(tableName);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public PageInfo<BillWsmVo> findPage(BillWsmVo vo,String tableName) throws ParseException {
        Map<String,Object> map = new HashMap<>();
        map.put("tableName",tableName);
        map.put("merchantOrderNo",vo.getMerchantOrderNo());
        map.put("reconciliationDay",new SimpleDateFormat("yyyy-MM-dd").parse(vo.getReconciliationDay()));
        PageInfo<BillWsm> result = this.queryPage(vo,map);
        return BillWsmMapStruct.INSTANCE.poPage2VoPage(result);
    }
}
