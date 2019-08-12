package com.nyd.user.dao;

import com.nyd.user.entity.Step;

import java.util.List;

/**
 * Created by Dengw on 17/11/4.
 */
public interface StepDao {
    void saveStep(Step step) throws Exception;
    void updateStep(Step step) throws Exception;
    List<Step> getStepByUserId(String userId) throws Exception;

    List<Step> selectByUserId(String userId) throws Exception;
}
