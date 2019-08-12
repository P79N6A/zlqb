package com.nyd.capital.ws.controller;

import com.nyd.capital.api.service.CapitalApi;
import com.nyd.capital.model.jx.SubmitJxMsgCode;
import com.nyd.capital.model.pocket.Pocket2CallbackDto;
import com.nyd.capital.model.pocket.Pocket2JobDto;
import com.nyd.capital.model.pocket.PocketAccountDetailDto;
import com.nyd.capital.model.pocket.PocketSendCodeDto;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.pocket.business.Pocket2Business;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


/**
 * @author liuqiu
 */
@RestController
@RequestMapping("/capital/pocket")
public class Pocket2Controller {

    private static Logger logger = LoggerFactory.getLogger(Pocket2Controller.class);

    @Autowired
    private Pocket2Business pocket2Business;
    @Autowired
    private CapitalApi capitalApi;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取江西银行开户页面跳转接口
     */
    @RequestMapping(value = "/openPageRetUrl", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    public void openPageRetUrl(String userId) {
        logger.info("开户页面提交成功,userId is:"+userId);
    }
    /**
     * 合规页面跳转接口
     */
    @RequestMapping(value = "/termsAuthPageRetUrl", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    public void termsAuthPageRetUrl(String userId) {
        logger.info("合规页面提交成功,userId is:"+userId);
    }
    /**
     * 授权页面跳转接口
     */
    @RequestMapping(value = "/complianceBorrowPageRetUrl", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    public void complianceBorrowPageRetUrl(String orderNo) {
        logger.info("授权页面提交成功,userId is:"+orderNo);
        if (redisTemplate.hasKey("pocket:complianceBorrow" + orderNo)){
            return;
        }else {
            redisTemplate.opsForValue().set("pocket:complianceBorrow" + orderNo,"1",14, TimeUnit.DAYS);
        }
        capitalApi.pushPocketOrder(orderNo);
    }
    /**
     * 提现页面跳转接口
     */
    @RequestMapping(value = "/withdrawRetUrl", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    public void withdrawRetUrl(String orderNo) {
        logger.info("提现页面提交成功,orderNo is:"+orderNo);
    }

    @RequestMapping(value = "/getJxBankCode")
    public ResponseData openJxHtml(@RequestBody PocketSendCodeDto request) {
        if (request == null || StringUtils.isBlank(request.getUserId()) || StringUtils.isBlank(request.getOrderNo())) {
            return ResponseData.error(OpenPageConstant.PRARM_ERROR);
        }
        return pocket2Business.getJxBankCode(request);
    }

    @RequestMapping(value = "/submitJxMsgCode")
    public ResponseData submitJxMsgCode(@RequestBody SubmitJxMsgCode submitJxMsgCode) {
        if (submitJxMsgCode == null || StringUtils.isBlank(submitJxMsgCode.getUserId()) || StringUtils.isBlank(submitJxMsgCode.getSmsCode()) || StringUtils.isBlank(submitJxMsgCode.getDriverUuid())) {
            return ResponseData.error(OpenPageConstant.PRARM_ERROR);
        }
        return pocket2Business.submitJxMsgCode(submitJxMsgCode);
    }

    @RequestMapping(value = "/selectUserOpenDetail")
    public ResponseData selectUserOpenDetail(@RequestBody PocketAccountDetailDto dto) {
        if (dto == null || StringUtils.isBlank(dto.getUserId())) {
            return ResponseData.error(OpenPageConstant.PRARM_ERROR);
        }
        return pocket2Business.selectUserOpenDetail(dto,true);
    }

    @RequestMapping(value = "/pocketCallback")
    public String pocketCallback(@RequestBody Pocket2CallbackDto dto) {
        return pocket2Business.pocketCallback(dto);
    }


    @RequestMapping(value = "/pocketJob")
    public String pocketJob(@RequestBody Pocket2JobDto dto) {
        return pocket2Business.pocketJob(dto);
    }
}
