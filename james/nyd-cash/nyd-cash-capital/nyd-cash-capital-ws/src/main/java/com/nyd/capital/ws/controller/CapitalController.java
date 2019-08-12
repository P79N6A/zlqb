package com.nyd.capital.ws.controller;

import com.nyd.capital.service.CapitalService;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.order.model.msg.OrderMessage;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuqiu
 */
@RestController
@RequestMapping("/capital")
public class CapitalController {
    Logger logger = LoggerFactory.getLogger(CapitalController.class);

    @Autowired
    private CapitalService capitalService;
    @RequestMapping(value = "/sendCapital")
    public ResponseData sendCapital(@RequestBody OrderMessage message){
        if (message == null){
            return ResponseData.error(OpenPageConstant.NULL_DATA);
        }
        if (message.getChannel() == null){
                message.setChannel(BorrowConfirmChannel.NYD.getChannel());
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
        return capitalService.newSendCapital(message,false);
    }

    @RequestMapping(value = "/sendCapitalForPocket")
    public ResponseData sendCapitalForPocket(@RequestBody OrderMessage message){
        if (message == null){
            return ResponseData.error(OpenPageConstant.NULL_DATA);
        }
        if (message.getChannel() == null){
                message.setChannel(BorrowConfirmChannel.NYD.getChannel());
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
        return capitalService.newSendCapital(message,true);
    }

    @RequestMapping(value = "/capitalReSend")
    public ResponseData capitalReSend(@RequestBody List<OrderMessage> message){
    	for(OrderMessage mess:message) {
    		try {
    			capitalService.sendCapitalAfterRisk(mess);
    		}catch(Exception e) {
    			logger.error("重新推送订单失败："+mess.getOrderNo()+"异常信息：" + e.getMessage());
    		}
    	}
        return ResponseData.success();
    }
    @RequestMapping(value = "/capitalReSendAfterRisk")
    public ResponseData capitalReSendAfterRisk(@RequestBody List<OrderInfo> orderInfos){
    	try {
			capitalService.reSendCapitalAfterRisk(orderInfos);
		} catch (Exception e) {
			logger.error("重新推送订单失败："+"异常信息：" + e.getMessage());
		}
    	return ResponseData.success();
    }


    @RequestMapping(value = "/capitalTask")
    public ResponseData doTask(@RequestBody OrderMessage message){
        return capitalService.queryLoan(message.getFundCode());
    }

    @RequestMapping(value = "/withholdTask")
    public ResponseData withholdTask(){
        return capitalService.withholdTask();
    }
}
