package com.nyd.zeus.dao.impl;

import com.nyd.zeus.dao.ChargebackWsmDao;
import com.nyd.zeus.dao.mapper.ChargebackWsmMapper;
import com.nyd.zeus.entity.ChargeBackWsm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
@Repository
public class ChargebackWsmDaoImpl implements ChargebackWsmDao{

//    @Resource(name="mysql")
//    private CrudTemplate crudTemplate;
    @Autowired
    private ChargebackWsmMapper chargebackWsmMapper;
//    @Override
//    public void save(ChargeBackWsm chargeBackWsm) {
//        try {
//            crudTemplate.save(chargeBackWsm);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void saveList(List<ChargeBackWsm> list,String tableName) {
        Map<String,Object> map = new HashMap<>();
        map.put("tablename",tableName);
        map.put("values",list);
        chargebackWsmMapper.insertBatchList(map);
//        try {
//            crudTemplate.save(list);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void dropTable(String tableName) {
        chargebackWsmMapper.dropTable(tableName);
    }

    @Override
    public void createTable(String tableName) {
        chargebackWsmMapper.createTable(tableName);
    }
}
