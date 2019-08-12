package com.nyd.user.dao.impl;

import com.nyd.user.dao.StepDao;
import com.nyd.user.entity.Step;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dengw on 17/11/4.
 */
@Repository
public class StepDaoImpl implements StepDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void saveStep(Step step) throws Exception {
        crudTemplate.save(step);
    }

    @Override
    public void updateStep(Step step) throws Exception {
        Criteria criteria = Criteria.from(Step.class)
                .whioutId()
                .where()
                .and("user_id", Operator.EQ, step.getUserId())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        step.setUserId(null);
        step.setUpdateTime(null);
        crudTemplate.update(step,criteria);
    }

    @Override
    public List<Step> getStepByUserId(String userId) throws Exception {
        Step step = new Step();
        Criteria criteria = Criteria.from(Step.class)
                .where()
                .and("user_id", Operator.EQ, userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        List<Step> stepList = crudTemplate.find(step,criteria);
        return stepList;
    }

    @Override
    public List<Step> selectByUserId(String userId) throws Exception {
        Step step = new Step();
        Criteria criteria = Criteria.from(Step.class)
                .where()
                .and("user_id", Operator.EQ, userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        List<Step> stepList = crudTemplate.find(step,criteria);
        return stepList;
    }
}
