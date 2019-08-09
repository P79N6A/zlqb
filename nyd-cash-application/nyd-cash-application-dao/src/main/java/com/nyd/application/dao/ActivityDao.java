package com.nyd.application.dao;

import java.util.List;

import com.nyd.application.model.request.ActivityModel;

public interface ActivityDao {
	
	 List<ActivityModel> getActivitiesByAppName(String appName) throws Exception;
	 
	 List<ActivityModel> getNydActivities() throws Exception;

}
