package com.nyd.application.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.nyd.application.dao.ActivityDao;
import com.nyd.application.entity.Activity;
import com.nyd.application.model.request.ActivityModel;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
@Repository
public class ActivityDaoImpl implements ActivityDao{
	
	@Resource(name="mysql")
    private CrudTemplate crudTemplate;
	@Override
	public List<ActivityModel> getActivitiesByAppName(String appName) throws Exception {
		ActivityModel activity = new ActivityModel();
        Criteria criteria = Criteria.from(Activity.class)
                .where()
                .and("app_name",Operator.EQ,appName)
                .and("delete_flag", Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        return crudTemplate.find(activity,criteria);
	}
	 @Override
    public List<ActivityModel> getNydActivities() throws Exception {
        ActivityModel activity = new ActivityModel();
        Criteria criteria = Criteria.from(Activity.class)
                .where()
                .and("app_name",Operator.EQ,"nyd")
                .and("delete_flag", Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        return crudTemplate.find(activity,criteria);
	 }
}
