package com.nyd.user.ws.controller;

import com.nyd.user.model.BaseInfo;
import com.nyd.user.service.JobInfoService;
import com.tasfe.framework.support.model.ResponseData;
import com.nyd.user.model.JobInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Dengw on 17/11/2.
 * 工作信息接口
 */
@RestController
@RequestMapping("/user")
public class JobInfoController {
    private static Logger LOGGER = LoggerFactory.getLogger(JobInfoController.class);

    @Autowired
    private JobInfoService jobInfoService;

    @RequestMapping(value = "/job/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveJob(@RequestBody JobInfo jobInfo) throws Throwable{
        ResponseData responseData = jobInfoService.saveJobInfo(jobInfo);
        return responseData;
    }

    @RequestMapping(value = "/job/fetch/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData fetchJob(@RequestBody BaseInfo baseInfo) throws Throwable{
        if(baseInfo.getUserId() == null){
            return ResponseData.success();
        }else{
            return jobInfoService.getJobInfo(baseInfo.getUserId());
        }
    }
}
