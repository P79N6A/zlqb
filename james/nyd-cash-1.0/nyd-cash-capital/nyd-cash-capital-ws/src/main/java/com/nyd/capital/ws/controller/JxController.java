package com.nyd.capital.ws.controller;

import com.nyd.capital.model.jx.*;
import com.nyd.capital.service.jx.business.JxBusiness;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.order.model.msg.OrderMessage;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuqiu
 */
@RestController
@RequestMapping("/capital/jx")
public class JxController {
    Logger logger = LoggerFactory.getLogger(KzjrController.class);

    @Autowired
    private JxBusiness jxBusiness;
    @RequestMapping(value = "/openJxHtml")
    public ResponseData openJxHtml(@RequestBody OpenJxHtmlRequest openJxHtmlRequest){
        return jxBusiness.openJxHtml(openJxHtmlRequest);
    }

    @RequestMapping(value = "/submitJxMsgCode")
    public ResponseData submitJxMsgCode(@RequestBody SubmitJxMsgCode submitJxMsgCode){
        return jxBusiness.submitJxMsgCode(submitJxMsgCode);
    }

    @RequestMapping(value = "/callbackForHtml", method = RequestMethod.GET)
    public ResponseData jxReturnForHtml(String jxCode) {
        return jxBusiness.jxReturnForHtml(jxCode);
    }

    @RequestMapping(value = "/callbackForWithdraw")
    public ResponseData jxReturnForWithdraw(@RequestBody ReturnForWithdrawRequest returnForWithdrawRequest) {
        return jxBusiness.jxReturnForWithdraw(returnForWithdrawRequest);
    }

    @RequestMapping(value = "/onlyForWithdraw")
    public ResponseData onlyForWithdraw(@RequestBody ReturnForWithdrawRequest returnForWithdrawRequest) {
        return jxBusiness.onlyForWithdraw(returnForWithdrawRequest);
    }

    /**
     * 查询用户开户情况
     * @param message
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selectJxAccount")
    public ResponseData selectJxAccount(@RequestBody OrderMessage message) throws Exception{
        if (message == null){
            return ResponseData.error(OpenPageConstant.NULL_DATA);
        }
        if (StringUtils.isBlank(message.getFundCode())){
            return ResponseData.error(OpenPageConstant.NULL_CAPITAL);
        }
        if (StringUtils.isBlank(message.getUserId())){
            return ResponseData.error(OpenPageConstant.NULL_USERID);
        }
        if (StringUtils.isBlank(message.getOrderNo())){
            return ResponseData.error(OpenPageConstant.NULL_ORDERNO);
        }
        if (StringUtils.isBlank(message.getBankAccount())){
            return ResponseData.error(OpenPageConstant.NULL_BANK);
        }
        return jxBusiness.selectJxAccount(message);
    }
}
