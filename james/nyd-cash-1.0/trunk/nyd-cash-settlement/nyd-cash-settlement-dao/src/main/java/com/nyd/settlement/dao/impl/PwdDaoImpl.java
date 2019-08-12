package com.nyd.settlement.dao.impl;

import com.nyd.settlement.dao.PwdDao;
import com.nyd.settlement.entity.Pwd;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Cong Yuxiang
 * 2018/2/1
 **/
@Repository
public class PwdDaoImpl implements PwdDao{
    Logger logger = LoggerFactory.getLogger(PwdDaoImpl.class);
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;


    @Override
    public Pwd selectPwdByType(Integer type) throws Exception {

            Pwd pwd = new Pwd();
            pwd.setType(type);
            Criteria criteria = Criteria.from(Pwd.class);
            List<Pwd> result = crudTemplate.find(pwd,criteria);
            if(result==null||result.size()==0){
                return null;
            }
            return result.get(0);

    }
}
