package com.creativearts.nyd.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creativearts.nyd.pay.model.MemberCarryResp;
import com.creativearts.nyd.pay.model.QueryRequest;
import com.creativearts.nyd.pay.service.RedisProcessService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Cong Yuxiang
 **/
@RestController
@RequestMapping(value="/pay/status")
public class StatusController {

    final Logger logger = LoggerFactory.getLogger(StatusController.class);
    @Autowired
    private RedisProcessService redisProcessService;

    @RequestMapping(value="/memquery")
    public ResponseData memquery(@RequestBody QueryRequest queryRequest) throws Exception {
        logger.info("memquery传入的参数为"+ JSON.toJSONString(queryRequest));
        String result = redisProcessService.getPayStatusMember(queryRequest.getUserId());
        logger.info("memquery redis结果为"+result);

        JSONObject object = new JSONObject();
        if(result==null){
            ResponseData responseData = ResponseData.success();
            responseData.setCode("2");
            responseData.setMsg("正在支付中");
            object.put("code","2");
            responseData.setData(object);
            logger.info(queryRequest.getUserId()+"返回的结果"+JSON.toJSONString(responseData));
            return responseData;
        }else {
            MemberCarryResp resp = JSON.parseObject(result,MemberCarryResp.class);
            logger.info("memquery查询的结果"+JSON.toJSONString(resp));
            if("0".equals(resp.getStatus())){
                ResponseData responseData = ResponseData.success();
                responseData.setCode("0");
                responseData.setMsg("支付成功");
                object.put("code","0");
                object.put("type",resp.getMemberType());
                responseData.setData(object);
                logger.info(queryRequest.getUserId()+"返回的结果"+JSON.toJSONString(responseData));
                return responseData;
            }else {
                ResponseData responseData = ResponseData.success();
                responseData.setCode("1");
                responseData.setMsg("支付失败");
                object.put("code","1");
                object.put("type",resp.getMemberType());
                responseData.setData(object);
                logger.info(queryRequest.getUserId()+"返回的结果"+JSON.toJSONString(responseData));
                return  responseData;
            }
        }
    }
}
