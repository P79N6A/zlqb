package com.nyd.user.service;

import com.nyd.user.entity.Step;
import com.nyd.user.model.StepInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 17/11/4.
 */
public interface StepInfoService {
    ResponseData updateStepInfo(StepInfo stepInfo);

    ResponseData<StepInfo> getStepScore(String userId,String appName);

    void saveStep(Step step) throws Exception;

    void updateStep(Step step1) throws Exception;

    ResponseData getStepScoreInfo(String userId);

    ResponseData getUserStep(String userId);

    ResponseData getAssessReport(String userId);

    ResponseData<Step> findByUserId(String userId);
}
