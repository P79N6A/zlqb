package com.nyd.zeus.service.impls;

import com.nyd.zeus.dao.ChargebackWsmDao;
import com.nyd.zeus.entity.ChargeBackWsm;
import com.nyd.zeus.model.ChargebackWsmVo;
import com.nyd.zeus.service.ChargebackWsmService;
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
public class ChargebackWsmServiceImpl implements ChargebackWsmService{
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

    @Override
    public void saveList(List<ChargebackWsmVo> chargebackWsmVos,String tableName){
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

    @Override
    public void dropTable(String tableName) {
        chargebackWsmDao.dropTable(tableName);
    }

    @Override
    public void createTable(String tableName) {
        chargebackWsmDao.createTable(tableName);
    }
}
