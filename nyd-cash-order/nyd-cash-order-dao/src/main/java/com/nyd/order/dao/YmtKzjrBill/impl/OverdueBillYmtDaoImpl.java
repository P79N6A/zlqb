package com.nyd.order.dao.YmtKzjrBill.impl;

import com.nyd.order.dao.YmtKzjrBill.OverdueBillYmtDao;
import com.nyd.order.entity.YmtKzjrBill.OverdueBillYmt;
import com.nyd.order.model.YmtKzjrBill.OverdueBillYmtInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class OverdueBillYmtDaoImpl implements OverdueBillYmtDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;


    @Override
    public List<OverdueBillYmtInfo> selectByOrderSno(String orderSno) throws Exception {
        Criteria criteria = Criteria.from(OverdueBillYmt.class)
                .whioutId()
                .where().and("order_sno", Operator.EQ,orderSno)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        OverdueBillYmtInfo overdueBillInfo = new OverdueBillYmtInfo();
        return crudTemplate.find(overdueBillInfo,criteria);
    }

    @Override
    public OverdueBillYmtInfo selectByBillNo(String billNo) throws Exception {
        Criteria criteria = Criteria.from(OverdueBillYmt.class)
                .whioutId()
                .where().and("bill_no", Operator.EQ,billNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        OverdueBillYmtInfo overdueBillInfo = new OverdueBillYmtInfo();
        List<OverdueBillYmtInfo> overdueBillInfoList = crudTemplate.find(overdueBillInfo,criteria);
        if (overdueBillInfoList!=null && overdueBillInfoList.size()>0) {
            return overdueBillInfoList.get(0);
        }
        return null;
    }
    @Override
    public void updateByOrderSno(OverdueBillYmtInfo overdueBillYmtInfo) throws Exception{
        Criteria criteria = Criteria.from(OverdueBillYmt.class)
                .whioutId()
                .where().and("order_sno", Operator.EQ,overdueBillYmtInfo.getOrderSno())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        overdueBillYmtInfo.setOrderSno(null);
        crudTemplate.update(overdueBillYmtInfo,criteria);
    }

    @Override
    public void updateByBillNo(OverdueBillYmtInfo overdueBillInfo) throws Exception {
        Criteria criteria = Criteria.from(OverdueBillYmt.class)
                .whioutId()
                .where().and("bill_no", Operator.EQ,overdueBillInfo.getBillNo())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        overdueBillInfo.setBillNo(null);
        crudTemplate.update(overdueBillInfo,criteria);
    }

    @Override
    public List<OverdueBillYmtInfo> selectByBillStatus(String billStatus) throws Exception {
        Criteria criteria = Criteria.from(OverdueBillYmt.class)
                .whioutId()
                .where().and("bill_status", Operator.EQ,billStatus)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        OverdueBillYmtInfo overdueBillInfo = new OverdueBillYmtInfo();
        return crudTemplate.find(overdueBillInfo,criteria);
    }

    @Override
    public void save(OverdueBillYmtInfo overdueBillInfo) throws Exception {
        OverdueBillYmt overdueBill = new OverdueBillYmt();
        BeanUtils.copyProperties(overdueBillInfo,overdueBill);
        crudTemplate.save(overdueBill);
    }
}
