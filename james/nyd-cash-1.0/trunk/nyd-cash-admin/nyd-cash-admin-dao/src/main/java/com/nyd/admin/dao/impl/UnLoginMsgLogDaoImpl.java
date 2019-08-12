package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.UnLoginMsgLogDao;
import com.nyd.admin.entity.UnLoginMsgLog;
import com.nyd.admin.model.UnLoginMsgLogInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 14:38 2018/10/1
 */
@Repository
public class UnLoginMsgLogDaoImpl implements UnLoginMsgLogDao {

    @Autowired
    private CrudTemplate crudTemplate;

    @Override
    public void save(UnLoginMsgLog unLoginMsgLog) throws Exception {
        crudTemplate.save(unLoginMsgLog);
    }

    @Override
    public void update(UnLoginMsgLogInfo unLoginMsgLogInfo) throws Exception {
        Criteria criteria = Criteria.from(UnLoginMsgLog.class)
                .whioutId()
                .where()
                .and("mobile",Operator.EQ,unLoginMsgLogInfo.getMobile())
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        unLoginMsgLogInfo.setMobile(null);
        crudTemplate.update(unLoginMsgLogInfo,criteria);
    }

    @Override
    public List<UnLoginMsgLogInfo> getByMobile(String mobile) throws Exception {
        Criteria criteria = Criteria.from(UnLoginMsgLog.class)
                .whioutId()
                .where()
                .and("mobile",Operator.EQ,mobile)
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        UnLoginMsgLogInfo unLoginMsgLogInfo = new UnLoginMsgLogInfo();
        return crudTemplate.find(unLoginMsgLogInfo,criteria);
    }
}
