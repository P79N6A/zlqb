package com.nyd.member.dao.impl;

import com.nyd.member.dao.MemberConfigDao;
import com.nyd.member.entity.MemberConfig;
import com.nyd.member.model.MemberConfigModel;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dengw on 2017/12/6
 */
@Repository
public class MemberConfigDaoImpl implements MemberConfigDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(MemberConfig memberConfig) throws Exception {
        crudTemplate.save(memberConfig);
    }

    @Override
    public List<MemberConfigModel> getMermberConfigByBusiness(String business) throws Exception {
        MemberConfigModel memberConfigModel = new MemberConfigModel();
        memberConfigModel.setBusiness(business);
        Criteria criteria = Criteria.from(MemberConfig.class)
                .where()
                .and("delete_flag", Operator.EQ,0)
                .endWhere();

        return crudTemplate.find(memberConfigModel,criteria);
    }

    @Override
    public MemberConfigModel getMermberConfigByType(String type) throws Exception {
        MemberConfigModel memberConfigModel = new MemberConfigModel();
        memberConfigModel.setType(type);
        Criteria criteria = Criteria.from(MemberConfig.class)
                .where()
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        List<MemberConfigModel> list = crudTemplate.find(memberConfigModel,criteria);
        if (list!=null&&list.size()>0) {
            return list.get(0);
        }
        return null;
    }

	@Override
	public MemberConfigModel getMermberConfigByTypeBusiness(String type, String business) throws Exception {
		 MemberConfigModel memberConfigModel = new MemberConfigModel();
		 memberConfigModel.setType(type);
		 memberConfigModel.setBusiness(business);
		 Criteria criteria = Criteria.from(MemberConfig.class)
	                .where()
	                .and("delete_flag", Operator.EQ,0)
	                .endWhere();
		 List<MemberConfigModel> list = crudTemplate.find(memberConfigModel,criteria);
	        if (list!=null&&list.size()>0) {
	            return list.get(0);
	        }
	        return null;
	}

}
