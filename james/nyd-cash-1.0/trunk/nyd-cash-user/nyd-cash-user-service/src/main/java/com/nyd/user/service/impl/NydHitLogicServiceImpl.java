package com.nyd.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.user.entity.HitLog;
import com.nyd.user.entity.HitRuleConfig;
import com.nyd.user.entity.UserTarget;
import com.nyd.user.model.enums.RuleEnum;
import com.nyd.user.model.request.HitRequest;
import com.nyd.user.model.request.HitRuleConfigRequest;
import com.nyd.user.model.response.HitResponse;
import com.nyd.user.service.HitLogService;
import com.nyd.user.service.HitRuleConfigService;
import com.nyd.user.service.NydHitLogicService;
import com.nyd.user.service.UserTargetService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "nydHitLogicService")
public class NydHitLogicServiceImpl implements NydHitLogicService {
    private static Logger logger = LoggerFactory.getLogger(NydHitLogicServiceImpl.class);

    @Autowired
    private HitRuleConfigService hitRuleConfigService;

    @Autowired
    private UserTargetService userTargetService;

    @Autowired
    private HitLogService hitLogService;

    @Override
    public ResponseData HitByMobileAndRule(HitRequest request) {
        ResponseData response = ResponseData.success();
        logger.info("侬要贷撞库请求参数:"+ JSON.toJSON(request));
        if (!StringUtils.isNotBlank(request.getAppName())){
            return ResponseData.error("app名称为空");
        }
        if (!StringUtils.isNotBlank(request.getSource())){
            return ResponseData.error("渠道来源为空");
        }
        if (!StringUtils.isNotBlank(request.getMobile())){
            return ResponseData.error("手机号为空");
        }
        if (!StringUtils.isNotBlank(request.getMobileType())){
            return ResponseData.error("手机号类型为空");
        }
        logger.info("nyd撞库规则传入参数"+ToStringBuilder.reflectionToString(request));
        String source = request.getSource();    //哪一家渠道：
        String appName = request.getAppName();  //app名称
        String mobile = request.getMobile();    //手机号
        String type = request.getMobileType(); //手机号加密类型
        String ruleCode = "";                   //撞库规则

        try {
            //1.找到相关渠道对应的规则
            HitRuleConfigRequest hitRuleConfigRequest = new HitRuleConfigRequest();
            hitRuleConfigRequest.setAppName(appName);
            hitRuleConfigRequest.setSource(source);
            ResponseData<List<HitRuleConfig>> responseData = hitRuleConfigService.findByAppNameAndSource(hitRuleConfigRequest);
            if ("0".equals(responseData.getStatus()) && responseData.getData() != null){
                List<HitRuleConfig> list = responseData.getData();
                HitRuleConfig hitRuleConfig = list.get(0);
                ruleCode = hitRuleConfig.getRuleCode();
            }

            //2.找到具体规则后,手机号根据这个规则去撞库,判断是否命中,并保存记录
            Map<String,String> params = new HashMap<>();
            params.put("ruleCode",ruleCode);
            params.put("mobile",mobile);
            params.put("type",type);

            //撞库规则1~4的撞库逻辑
            if (ruleCode.equals(RuleEnum.Rule1.getCode()) || ruleCode.equals(RuleEnum.Rule2.getCode()) || ruleCode.equals(RuleEnum.Rule3.getCode())
                    || ruleCode.equals(RuleEnum.Rule4.getCode())){
                List list = userTargetService.toHitByRuleCodeAndMobile(params);
                logger.info("撞库后的结果list:"+ JSON.toJSON(list));

                HitLog hitLog = new HitLog();
                HitResponse hitResponse = new HitResponse();  //响应给调用方的结果
                String phone = request.getMobile();
                if (list != null && list.size() > 0){
                    /*Object o = list.get(0);
                    if(o instanceof UserTarget){
                        phone = ((UserTarget) o).getMobile();
                    }*/
                    hitLog.setHitResult(1);
                    hitResponse.setStatus(1);
                    hitResponse.setMsg("命中");
                    hitResponse.setMobile(phone);
                }else {
                    hitLog.setHitResult(0);
                    hitResponse.setStatus(0);
                    hitResponse.setMsg("未命中");                  
                }
                logger.info(phone+ToStringBuilder.reflectionToString(hitResponse));               
                hitLog.setMobile(phone);
                hitLog.setAppName(appName);
                hitLog.setSource(source);
                hitLog.setRuleCode(ruleCode);
                logger.info("保存撞库记录对象:"+JSON.toJSON(hitLog));
                hitLogService.saveHitLog(hitLog);
                logger.info("手机号:"+mobile+"的撞库日志保存成功");
                logger.info("返给调用者的对象:"+JSON.toJSON(hitResponse));
                response.setData(hitResponse);
            }

            //针对零撞库的逻辑(零撞库：用户进来直接保存为未命中)
            if (ruleCode.equals(RuleEnum.Rule5.getCode()) ||StringUtils.isBlank(ruleCode) ){
                HitLog zerohitLog = new HitLog();
                zerohitLog.setHitResult(0);
                zerohitLog.setMobile(mobile);
                zerohitLog.setAppName(appName);
                zerohitLog.setSource(source);
                zerohitLog.setRuleCode(RuleEnum.Rule5.getCode());
                logger.info("保存零撞库记录对象:"+JSON.toJSON(zerohitLog));
                hitLogService.saveHitLog(zerohitLog);
                logger.info("零撞库记录保存成功");

                HitResponse hitResponse = new HitResponse();  //响应给调用方的结果
                hitResponse.setStatus(1);
                hitResponse.setMsg("命中");
                response.setData(hitResponse);
            }

        }catch (Exception e){
            logger.error("侬要贷撞库出错",e);
            return ResponseData.error("服务器开小差");
        }
        return response;
    }
}
