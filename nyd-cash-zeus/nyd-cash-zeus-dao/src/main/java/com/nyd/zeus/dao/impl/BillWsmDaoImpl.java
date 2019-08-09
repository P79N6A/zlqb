package com.nyd.zeus.dao.impl;

import com.nyd.zeus.dao.BillWsmDao;
import com.nyd.zeus.dao.mapper.BillWsmMapper;
import com.nyd.zeus.entity.BillWsm;
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
public class BillWsmDaoImpl implements BillWsmDao{
//    @Resource(name="mysql")
//    private CrudTemplate crudTemplate;
    @Autowired
    private BillWsmMapper billWsmMapper;
//    @Override
//    public void save(BillWsm billWsm) {
//        try {
//            crudTemplate.save(billWsm);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void dropTable(String tableName) {
        billWsmMapper.dropTable(tableName);
    }

    @Override
    public void createTable(String tableName) {
        billWsmMapper.createTable(tableName);
    }

    @Override
    public void saveList(List<BillWsm> list,String tableName) {
        Map<String,Object> map = new HashMap<>();
        map.put("tablename",tableName);
        map.put("values",list);
        billWsmMapper.insertBatchList(map);
//        try {
//            crudTemplate.save(list);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

//    @Override
//    public int selectBy() {
//        return billWsmMapper.selectBy();
//    }

}
