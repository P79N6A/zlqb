package com.nyd.msg.dao.impl;

import com.nyd.msg.dao.ISysSmsParamDao;
import com.nyd.msg.entity.SysSmsParam;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.api.params.CrudParam;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.xml.registry.infomodel.User;
import java.util.List;

@Repository
public class SysSmsParamDao implements ISysSmsParamDao{

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public SysSmsParam selectBySourceType(Integer type) {
        SysSmsParam param = new SysSmsParam();
        param.setSmsType(type);
        Criteria criteria = Criteria.from(SysSmsParam.class);
        try {
            List<SysSmsParam> params = crudTemplate.find(param,criteria);
            if(params == null){
                return null;
            }
            return params.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
