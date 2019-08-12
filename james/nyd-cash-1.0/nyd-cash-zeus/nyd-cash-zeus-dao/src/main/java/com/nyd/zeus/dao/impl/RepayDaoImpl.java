package com.nyd.zeus.dao.impl;

import com.nyd.zeus.dao.RepayDao;
import com.nyd.zeus.entity.Remit;
import com.nyd.zeus.entity.Repay;
import com.nyd.zeus.model.RepayInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhujx on 2017/11/21.
 */
@Repository
public class RepayDaoImpl implements RepayDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(RepayInfo repayInfo) throws Exception {
        Repay repay = new Repay();
        BeanUtils.copyProperties(repayInfo,repay);
        crudTemplate.save(repay);
    }

    @Override
    public List<RepayInfo> getRepayInfoByBillNo(String billNo) throws Exception {
        Criteria criteria = Criteria.from(Remit.class)
                .where().and("bill_no", Operator.EQ,billNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        RepayInfo repayInfo = new RepayInfo();
        return crudTemplate.find(repayInfo,criteria);
    }
}
