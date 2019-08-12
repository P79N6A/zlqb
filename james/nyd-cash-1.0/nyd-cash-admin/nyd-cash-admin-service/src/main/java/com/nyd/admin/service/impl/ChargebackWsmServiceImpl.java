package com.nyd.admin.service.impl;


import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.ChargebackWsmDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.ChargebackWsmMapper;
import com.nyd.admin.entity.ChargeBackWsm;
import com.nyd.admin.model.ChargebackWsmVo;
import com.nyd.admin.service.ChargebackWsmService;
import com.nyd.admin.service.CrudService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.ChargeBackWsmMapStruct;
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
public class ChargebackWsmServiceImpl extends CrudService<ChargebackWsmMapper,ChargeBackWsm> implements ChargebackWsmService {

    @Autowired
    private ChargebackWsmDao chargebackWsmDao;
//    @Override
//    public void save(ChargebackWsmVo chargebackWsmVo){
//        ConvertUtils.register(new DateConverter(), Date.class);
//        ChargeBackWsm chargeBackWsm = new ChargeBackWsm();
//        try {
//            BeanUtils.copyProperties(chargeBackWsm,chargebackWsmVo);
//            chargebackWsmDao.save(chargeBackWsm);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//
//    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void saveList(List<ChargebackWsmVo> chargebackWsmVos, String tableName){
        ConvertUtils.register(new DateConverter(), Date.class);
        List<ChargeBackWsm> list = new ArrayList<>();
        for(ChargebackWsmVo vo:chargebackWsmVos) {
            ChargeBackWsm chargeBackWsm = new ChargeBackWsm();
            try {
                BeanUtils.copyProperties(chargeBackWsm, vo);
                list.add(chargeBackWsm);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        chargebackWsmDao.saveList(list,tableName);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void dropTable(String tableName) {
        chargebackWsmDao.dropTable(tableName);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void createTable(String tableName) {
        chargebackWsmDao.createTable(tableName);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public PageInfo<ChargebackWsmVo> findPage(ChargebackWsmVo vo,String tableName) throws ParseException {
        Map<String,Object> map = new HashMap<>();
        map.put("tableName",tableName);
        map.put("merchantOrderNo",vo.getMerchantOrderNo());
        map.put("reconciliationDay",new SimpleDateFormat("yyyy-MM-dd").parse(vo.getReconciliationDay()));
        PageInfo<ChargeBackWsm> result = this.queryPage(vo,map);
        return ChargeBackWsmMapStruct.INSTANCE.poPage2VoPage(result);
    }
}
