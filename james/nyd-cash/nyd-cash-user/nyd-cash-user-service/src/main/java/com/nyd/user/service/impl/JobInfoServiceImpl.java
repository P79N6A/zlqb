package com.nyd.user.service.impl;

import com.nyd.user.api.UserJobContract;
import com.nyd.user.dao.JobDao;
import com.nyd.user.dao.StepDao;
import com.nyd.user.entity.Job;
import com.nyd.user.entity.Step;
import com.nyd.user.model.JobInfo;
import com.nyd.user.model.enums.StepScore;
import com.nyd.user.service.JobInfoService;
import com.nyd.user.service.consts.UserConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dengw on 17/11/3.
 */
@Service(value = "userJobContract")
public class JobInfoServiceImpl implements JobInfoService,UserJobContract {
    private static Logger LOGGER = LoggerFactory.getLogger(JobInfoServiceImpl.class);

    @Autowired
    private JobDao jobDao;
    @Autowired
    private StepDao stepDao;

    /**
     * 保存工作信息
     * @param jobInfo
     * @return
     */
    @Override
    public ResponseData saveJobInfo(JobInfo jobInfo){
        LOGGER.info("begin to save jobInfo, userId is " + jobInfo.getUserId());
        ResponseData responseData = ResponseData.success();
        try {
            jobDao.save(jobInfo);
        } catch (Exception e) {
            LOGGER.error("save jobInfo error! userId = "+jobInfo.getUserId(),e);
            return ResponseData.error(UserConsts.DB_ERROR_MSG);
        }
        try {
            //更新信息完整度
            LOGGER.info("begin to save stepInfo");
            Step step = new Step();
            step.setUserId(jobInfo.getUserId());
            step.setJobFlag(UserConsts.FILL_FLAG);
            stepDao.updateStep(step);
            LOGGER.info("update stepInfo success");
        } catch (Exception e) {
            LOGGER.error("save jobInfo success，but update stepInfo failed! userId = "+jobInfo.getUserId(),e);
        }
        LOGGER.info("save jobInfo success");
        return responseData;
    }

    /**
     * 查询工作信息
     * @param userId
     * @return
     */
    @Override
    public ResponseData<JobInfo> getJobInfo(String userId){
        LOGGER.info("begin to get jobInfo, userId is " + userId);
        ResponseData responseData = ResponseData.success();
        try {
            JobInfo jobInfo = new JobInfo();
            List<JobInfo> jobList = jobDao.getJobsByUserId(userId);
            if(jobList != null && jobList.size()>0){
                jobInfo = jobList.get(0);
            }
            responseData.setData(jobInfo);
            LOGGER.info("get jobInfo success");
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            LOGGER.error("get jobInfo error! userId = "+userId,e);
        }
        return responseData;
    }

    /**
     * 根据公司名称获取工作信息
     * @param company
     * @return
     */
    @Override
    public ResponseData<List<JobInfo>> getJobInfos(String company) {
        LOGGER.info("begin to get jobInfo by company, company is " + company);
        ResponseData responseData = ResponseData.success();
        try {
            List<JobInfo> jobList= jobDao.getJobsByCompany(company);
            responseData.setData(jobList);
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            LOGGER.error("get jobInfo error! company = "+company,e);
        }
        return responseData;
    }

    /**
     *根据公司地址获取工作信息
     * @param companyAddress
     * @return
     */
    @Override
    public ResponseData<List<JobInfo>> getJobByCompanyAdress(String companyAddress) {
        LOGGER.info("begin to get jobInfo by companyAddress, companyAddress is " + companyAddress);
        ResponseData responseData = ResponseData.success();
        try {
            List<JobInfo> jobList= jobDao.getJobByCompanyAdress(companyAddress);
            responseData.setData(jobList);
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            LOGGER.error("get jobInfo error! companyAddress = "+companyAddress,e);
        }
        return responseData;
    }



}
