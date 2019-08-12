package com.nyd.user.dao.impl;

import com.nyd.user.entity.ExtraInfo;
import com.nyd.user.dao.ExtraInfoDao;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dengw on 17/11/2.
 */
@Repository
public class ExtraInfoDaoImpl implements ExtraInfoDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void saveExtraInfo(ExtraInfo extraInfo) throws Exception {
        crudTemplate.save(extraInfo);
    }

    @Override
    public List<ExtraInfo> getExtraInfosByUserId(String userId) throws Exception {
        ExtraInfo extraInfo = new ExtraInfo();
        List<ExtraInfo> extraInfoList = crudTemplate.find(extraInfo, Criteria.from(com.nyd.user.entity.ExtraInfo.class)
                .where().and("user_id", Operator.EQ, userId).endWhere());
        return extraInfoList;
    }
}
