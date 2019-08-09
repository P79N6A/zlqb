package com.nyd.user.service;

import com.nyd.user.entity.Job;
import com.nyd.user.model.JobInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 17/11/3.
 */
public interface JobInfoService {
    ResponseData saveJobInfo(JobInfo jobInfo);

    ResponseData<JobInfo> getJobInfo(String userId);

}
