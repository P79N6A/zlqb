package com.nyd.user.dao;

import com.nyd.user.entity.Job;
import com.nyd.user.model.JobInfo;

import java.util.List;

/**
 * Created by Dengw on 17/11/2.
 */
public interface JobDao {
    void save(JobInfo jobInfo) throws Exception;

    List<JobInfo> getJobsByUserId(String userId) throws Exception;

    List<JobInfo> getJobsByCompany(String company) throws Exception;

    List<JobInfo> getJobByCompanyAdress(String companyAddress) throws Exception;

    void saveJob(Job job) throws Exception;
}
