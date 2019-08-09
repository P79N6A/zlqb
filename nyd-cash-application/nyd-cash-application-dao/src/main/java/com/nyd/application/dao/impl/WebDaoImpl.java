package com.nyd.application.dao.impl;

import com.nyd.application.dao.WebDao;
import com.nyd.application.entity.WebInfo;
import com.nyd.application.model.request.WebModel;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dengw on 2017/12/1
 */
@Repository
public class WebDaoImpl implements WebDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public List<WebModel> getWebInfos() throws Exception {
        WebModel model = new WebModel();
            Criteria criteria = Criteria.from(WebInfo.class)
                    .where()
                    .and("delete_flag",Operator.EQ,0)
                    .endWhere();
            return crudTemplate.find(model,criteria);
    }
}
