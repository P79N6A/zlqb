package com.nyd.order.dao.impl;

import com.nyd.order.dao.WithholdTaskConfigDao;
import com.nyd.order.model.WithholdTaskConfig;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author liuqiu
 */
@Repository
public class WithholdTaskConfigDaoImpl implements WithholdTaskConfigDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public List<WithholdTaskConfig> select() throws Exception {
        Criteria criteria = Criteria.from(WithholdTaskConfig.class)
                .where()
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        WithholdTaskConfig config = new WithholdTaskConfig();
        return crudTemplate.find(config,criteria);
    }
    @Override
    public List<WithholdTaskConfig> selectByCode(String code) throws Exception {
    	Criteria criteria = Criteria.from(WithholdTaskConfig.class)
    			.where()
    			.and("config_code", Operator.EQ,code)
    			.and("delete_flag",Operator.EQ,0)
    			.endWhere();
    	WithholdTaskConfig config = new WithholdTaskConfig();
    	return crudTemplate.find(config,criteria);
    }

    @Override
    public void update(Date startTime) throws Exception {
        Criteria criteria = Criteria.from(WithholdTaskConfig.class)
                .whioutId()
                .where()
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        WithholdTaskConfig config = new WithholdTaskConfig();
        config.setStartTime(startTime);
        crudTemplate.update(config,criteria);
    }
    @Override
    public void updateByCode(Date startTime,String code) throws Exception {
    	Criteria criteria = Criteria.from(WithholdTaskConfig.class)
    			.whioutId()
    			.where()
    			.and("config_code",Operator.EQ,code)
    			.and("delete_flag",Operator.EQ,0)
    			.endWhere();
    	WithholdTaskConfig config = new WithholdTaskConfig();
    	config.setStartTime(startTime);
    	crudTemplate.update(config,criteria);
    }
}
