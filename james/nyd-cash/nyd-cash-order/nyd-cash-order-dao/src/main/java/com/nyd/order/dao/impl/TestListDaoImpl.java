package com.nyd.order.dao.impl;

import com.nyd.order.dao.TestListDao;
import com.nyd.order.entity.InnerTest;
import com.nyd.order.model.InnerTestInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dengw on 2018/1/15
 */
@Repository
public class TestListDaoImpl implements TestListDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public List<InnerTest> getObjectsByMobile(String mobile) throws Exception {
        Criteria criteria = Criteria.from(InnerTest.class)
                .where().and("is_in_use",Operator.EQ,0)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        InnerTest tnnerTest = new InnerTest();
        tnnerTest.setMobile(mobile);
        return crudTemplate.find(tnnerTest,criteria);
    }

    @Override
    public void update(InnerTestInfo innerTestInfo) throws Exception {
        Criteria criteria = Criteria.from(InnerTest.class)
                .whioutId()
                .where().and("mobile",Operator.EQ,innerTestInfo.getMobile())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        innerTestInfo.setMobile(null);
        crudTemplate.update(innerTestInfo,criteria);
    }

    @Override
    public void save(InnerTestInfo innerTestInfo) throws Exception {
        crudTemplate.save(innerTestInfo);
    }

}
