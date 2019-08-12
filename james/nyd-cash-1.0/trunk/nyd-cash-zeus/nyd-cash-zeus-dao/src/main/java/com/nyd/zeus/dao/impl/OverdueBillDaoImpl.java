package com.nyd.zeus.dao.impl;

import com.nyd.zeus.dao.OverdueBillDao;
import com.nyd.zeus.entity.OverdueBill;
import com.nyd.zeus.model.OverdueBillInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujx on 2017/11/18.
 */
@Repository
public class OverdueBillDaoImpl implements OverdueBillDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(OverdueBillInfo overdueBillInfo) throws Exception {
        OverdueBill overdueBill = new OverdueBill();
        BeanUtils.copyProperties(overdueBillInfo,overdueBill);
        crudTemplate.save(overdueBill);
    }

    @Override
    public void update(OverdueBillInfo overdueBillInfo) throws Exception {
        Criteria criteria = Criteria.from(OverdueBill.class)
                .whioutId()
                .where().and("bill_no", Operator.EQ,overdueBillInfo.getBillNo())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        overdueBillInfo.setBillNo(null);
        overdueBillInfo.setUpdateTime(null);
        crudTemplate.update(overdueBillInfo,criteria);
    }

    @Override
    public List<OverdueBillInfo> getObjectsByOrderNo(String orderNo) throws Exception {
        Criteria criteria = Criteria.from(OverdueBill.class)
                .where().and("order_no", Operator.EQ,orderNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
        return crudTemplate.find(overdueBillInfo,criteria);
    }

    @Override
    public List<OverdueBillInfo> getObjectsByUserId(String userId) throws Exception {
        Criteria criteria = Criteria.from(OverdueBill.class)
                .where().and("user_id", Operator.EQ,userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
        return crudTemplate.find(overdueBillInfo,criteria);
    }

    @Override
    public OverdueBillInfo getObjectByBillNo(String billNo) throws Exception {
        Criteria criteria = Criteria.from(OverdueBill.class)
                .where().and("bill_no", Operator.EQ,billNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
        List<OverdueBillInfo> overdueBillInfoList = new ArrayList<OverdueBillInfo>();
        overdueBillInfoList = crudTemplate.find(overdueBillInfo,criteria);
        if(overdueBillInfoList != null && overdueBillInfoList.size() > 0 ){
            overdueBillInfo = overdueBillInfoList.get(0);
            return overdueBillInfo;
        }
        return  null;
    }

    @Override
    public OverdueBillInfo getObjectByUserId(String userId) throws Exception {
        Criteria criteria = Criteria.from(OverdueBill.class)
                .where().and("user_id", Operator.EQ,userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere().orderBy("create_time desc");
        OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
        List<OverdueBillInfo> overdueBillInfoList = new ArrayList<OverdueBillInfo>();
        overdueBillInfoList = crudTemplate.find(overdueBillInfo,criteria);
        if(overdueBillInfoList != null && overdueBillInfoList.size() > 0 ){
            overdueBillInfo = overdueBillInfoList.get(0);
        }
        return  overdueBillInfo;
    }

}
