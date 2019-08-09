package com.nyd.user.dao.impl;

import com.nyd.user.entity.Job;
import com.nyd.user.dao.JobDao;
import com.nyd.user.model.JobInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dengw on 17/11/2.
 */
@Repository
public class JobDaoImpl implements JobDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(JobInfo jobInfo) throws Exception {
        Job job= new Job();
        BeanUtils.copyProperties(jobInfo,job);
        crudTemplate.save(job);
    }

    @Override
    public List<JobInfo> getJobsByUserId(String userId) throws Exception {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setUserId(userId);
        return getJobs(jobInfo);
    }

    @Override
    public List<JobInfo> getJobsByCompany(String company) throws Exception {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setCompany(company);
        return getJobs(jobInfo);
    }

    @Override
    public List<JobInfo> getJobByCompanyAdress(String companyAddress) throws Exception {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setCompanyAddress(companyAddress);
        return getJobs(jobInfo);
    }

    @Override
    public void saveJob(Job job) throws Exception {
        crudTemplate.save(job);
    }

    private List<JobInfo> getJobs(JobInfo jobInfo) throws Exception {
        Criteria criteria = Criteria.from(Job.class)
                .where()
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc")
                .limit(0,1000);
        return crudTemplate.find(jobInfo,criteria);
    }
}
