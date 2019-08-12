package com.nyd.user.service;

import com.nyd.user.model.BaseInfo;
import com.nyd.user.model.IdentityInfo;
import com.nyd.user.model.PersonInfo;
import com.nyd.user.model.StepInfo;
import com.tasfe.framework.support.model.ResponseData;

public interface NewIdentityInfoService {
	
	ResponseData saveFaceIdentity(IdentityInfo identityInfo)  throws Exception;
	
	ResponseData saveIdentityInfo(IdentityInfo identityInfo) throws Exception;
	
	ResponseData savePersonInfo(PersonInfo personInfo) throws Exception;
	
	ResponseData saveMobileFlag(StepInfo stepInfo);
	
	ResponseData getStepInfo(String userId,String appName);
	
	ResponseData getCashList(BaseInfo baseInfo);
}
