package com.nyd.user.ws.controller;

import com.nyd.user.model.BaseInfo;
import com.nyd.user.model.StepInfo;
import com.nyd.user.service.StepInfoService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Dengw on 17/11/4.
 */
@RestController
@RequestMapping("/user")
public class StepInfoController {
    private static Logger LOGGER = LoggerFactory.getLogger(StepInfoController.class);
    @Autowired
    private StepInfoService stepInfoService;

    @RequestMapping(value = "/score/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData updateScore(@RequestBody StepInfo stepInfo) throws Throwable{
        ResponseData responseData = stepInfoService.updateStepInfo(stepInfo);
        return responseData;
    }

    @RequestMapping(value = "/score/fetch/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData fetchScore(@RequestBody BaseInfo baseInfo) throws Throwable{
        ResponseData responseData = stepInfoService.getStepScore(baseInfo.getUserId(),baseInfo.getAppName());
        return responseData;
    }

    @RequestMapping(value = "/score/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData fetchScoreNew(@RequestBody BaseInfo baseInfo) throws Throwable{
        ResponseData responseData = stepInfoService.getStepScore(baseInfo.getUserId(),baseInfo.getAppName());
        return responseData;
    }

    @RequestMapping(value = "/getScore/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getScore(@RequestBody BaseInfo baseInfo) throws Throwable{
        ResponseData responseData = stepInfoService.getStepScoreInfo(baseInfo.getUserId());
        return responseData;
    }

    @RequestMapping(value = "/stepInfo/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getStepInfo(@RequestBody BaseInfo baseInfo) throws Throwable{
        ResponseData responseData = stepInfoService.getUserStep(baseInfo.getUserId());
        return responseData;
    }

    @RequestMapping(value = "/assess/report/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getAssessReport(@RequestBody BaseInfo baseInfo) throws Throwable{
        ResponseData responseData = stepInfoService.getAssessReport(baseInfo.getUserId());
        return responseData;
    }
}
