package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.FundConfigDao;
import com.nyd.admin.entity.FundConfig;
import com.nyd.admin.model.fundManageModel.FundConfigModel;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hwei on 2017/12/29.
 */
@Repository
public class FundConfigDaoImpl implements FundConfigDao{
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    @Override
    public List<FundConfigModel> getFundConfig() throws Exception{
        FundConfigModel model = new FundConfigModel();
        Criteria criteria = Criteria.from(FundConfig.class)
                .whioutId()
                .where()
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        return crudTemplate.find(model,criteria);
    }
}
