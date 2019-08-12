package com.nyd.application.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.nyd.application.dao.FlowConfigDao;
import com.nyd.application.entity.FlowConfig;
import com.nyd.application.model.FlowConfigModel;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;

@Repository
public class FlowConfigDaoImpl implements FlowConfigDao{
	
	 @Resource(name="mysql")
	 private CrudTemplate crudTemplate;
	
	@Override
	public List<FlowConfigModel> getFlowConfigByChannelId(String channelId) throws Exception {
		    FlowConfigModel flowConfigModel = new FlowConfigModel();
	        Criteria criteria = Criteria.from(FlowConfig.class)
	                .where()
	                .and("channel_id", Operator.EQ,channelId)
	                .and("delete_flag",Operator.EQ,0)
	                .endWhere();	       
	        return crudTemplate.find(flowConfigModel,criteria);
	        
		
		
	}

}
