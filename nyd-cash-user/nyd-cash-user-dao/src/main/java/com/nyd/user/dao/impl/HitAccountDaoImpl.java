package com.nyd.user.dao.impl;

import com.nyd.user.dao.HitAccountDao;
import com.nyd.user.entity.HitAccount;
import com.nyd.user.model.HitAccountInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 11:21 2018/9/6
 */
@Repository
public class HitAccountDaoImpl implements HitAccountDao {

    @Autowired
    private CrudTemplate crudTemplate;

    @Override
    public void insert(HitAccount hitAccount) throws Exception {
        crudTemplate.save(hitAccount);
    }

    @Override
    public List<HitAccountInfo> getByMobile(String mobile) throws Exception {
        Criteria criteria = Criteria.from(HitAccount.class)
                .where()
                .and("account_number",Operator.EQ,mobile)
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        HitAccountInfo hitAccountInfo = new HitAccountInfo();
        return crudTemplate.find(hitAccountInfo,criteria);
    }

    @Override
    public List<HitAccountInfo> getBySha256Str(String sha256Str) throws Exception {
        Criteria criteria = Criteria.from(HitAccount.class)
                .where()
                .and("secret_sha_str",Operator.EQ,sha256Str)
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        HitAccountInfo hitAccountInfo = new HitAccountInfo();
        return crudTemplate.find(hitAccountInfo,criteria);
    }

    @Override
    public List<HitAccountInfo> getByMd5Str(String md5Str) throws Exception {
        Criteria criteria = Criteria.from(HitAccount.class)
                .where()
                .and("secret_md_str",Operator.EQ,md5Str)
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        HitAccountInfo hitAccountInfo = new HitAccountInfo();
        return crudTemplate.find(hitAccountInfo,criteria);
    }

    @Override
    public void updateByMdStr(HitAccountInfo hitAccountInfo)throws Exception{
        Criteria criteria = Criteria.from(HitAccount.class)
                .whioutId()
                .where()
                .and("secret_md_str",Operator.EQ,hitAccountInfo.getSecretMdStr())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        hitAccountInfo.setSecretMdStr(null);
        crudTemplate.update(hitAccountInfo,criteria);
    }
}
