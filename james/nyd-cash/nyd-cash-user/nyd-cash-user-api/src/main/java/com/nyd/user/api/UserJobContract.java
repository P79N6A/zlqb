package com.nyd.user.api;

import com.nyd.user.model.JobInfo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by Dengw on 2017/11/13
 */
public interface UserJobContract {
    ResponseData<JobInfo> getJobInfo(String userId);

    ResponseData<List<JobInfo>> getJobInfos(String company);

    ResponseData<List<JobInfo>> getJobByCompanyAdress(String companyAddress);
}
