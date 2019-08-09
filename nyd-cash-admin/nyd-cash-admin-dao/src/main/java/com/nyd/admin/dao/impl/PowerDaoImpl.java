package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.PowerDao;
import com.nyd.admin.entity.power.Power;
import com.nyd.admin.model.power.dto.PowerDto;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Peng
 * @create 2018-01-03 14:38
 **/
@Repository
public class PowerDaoImpl implements PowerDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    @Override
    public void savePowerMessage(Power power) throws Exception {
        crudTemplate.save(power);
    }

    @Override
    public void updatePowerMessage(PowerDto dto) throws Exception {
        Criteria criteria = Criteria.from(Power.class)
                .whioutId()
                .where().and("id", Operator.EQ,dto.getId())
                .endWhere();
        crudTemplate.update(dto,criteria);
    }

    @Override
    public void queryPowerMessage() throws Exception {
        Power power = new Power();
        Criteria criteria = Criteria.from(Power.class)
                .where()
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        crudTemplate.find(power,criteria);

    }
}
