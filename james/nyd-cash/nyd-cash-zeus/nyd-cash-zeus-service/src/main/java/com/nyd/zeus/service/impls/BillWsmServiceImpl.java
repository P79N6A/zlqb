package com.nyd.zeus.service.impls;

import com.nyd.zeus.dao.BillWsmDao;
import com.nyd.zeus.entity.BillWsm;
import com.nyd.zeus.model.BillWsmVo;
import com.nyd.zeus.service.BillWsmService;
import com.nyd.zeus.service.util.DateConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
@Service
public class BillWsmServiceImpl implements BillWsmService{
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

    @Override
    public void saveList(List<BillWsmVo> billWsmVos,String tableName){
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

    @Override
    public void dropTable(String tableName) {
        billWsmDao.dropTable(tableName);
    }

    @Override
    public void createTable(String tableName) {
        billWsmDao.createTable(tableName);
    }
}
