package com.nyd.user.api;

import java.util.Map;

import com.nyd.user.model.StepInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by hwei on 2018/3/12.
 */
public interface UserStepContract {
    ResponseData<StepInfo> getUserStep(String userId);

    ResponseData<StepInfo> doAssessTask(String userId);

    void updateUserStep(StepInfo step) throws Exception;

    void sendMsgToUserToApplication(String userId);
    
    Map<String,Object> updateFaceFlagInfo(int pageSize,long startId);
}
