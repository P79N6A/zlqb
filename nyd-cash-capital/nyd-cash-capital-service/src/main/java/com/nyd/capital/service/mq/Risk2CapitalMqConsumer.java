package com.nyd.capital.service.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.model.Risk2CapitalMessage;
import com.nyd.capital.service.CapitalService;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.msg.OrderMessage;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
public class Risk2CapitalMqConsumer implements RabbitmqMessageProcesser<String>{

    private static final Logger LOGGER = LoggerFactory.getLogger(Risk2CapitalMqConsumer.class);

    @Autowired
    CapitalService capitalService;
    @Autowired
    private OrderContract orderContract;
    
    //资金风控开关，1开启判断风控状态 0 关闭不判断风控状态
    private  String capitalRiskSwitch;


    @Override
    public void processMessage(String message) {
        LOGGER.info("接收资金风控回调信息Message:"+ message);
        Risk2CapitalMessage rMessage = JSON.parseObject(message, Risk2CapitalMessage.class);
        LOGGER.info("转换后回调信息Message:"+ JSON.toJSONString(rMessage));
        OrderInfo info = null;
        if("ymt".equals(rMessage.getProductType())) {
        	info = orderContract.getOrderByIbankOrderNo(rMessage.getOrderNo());
        	ResponseData<OrderInfo> response = orderContract.getOrderByOrderNo(info.getOrderNo());
            if(response == null || response.getData() == null) {
            	LOGGER.error("未查询到借款订单信息：{}",message);
            	return;
            }
            info = response.getData();
        }else {
        	ResponseData<OrderInfo> response = orderContract.getOrderByOrderNo(rMessage.getOrderNo());
            if(response == null || response.getData() == null) {
            	LOGGER.error("未查询到借款订单信息：{}",message);
            	return;
            }
            info = response.getData();
        }
        if(info == null) {
        	LOGGER.error("未查询到借款订单信息：{}",message);
        }
        LOGGER.info("查询订单信息：" + JSON.toJSONString(info));
        //风控状态开关
        LOGGER.info("是否适用风控结果开关：" + capitalRiskSwitch);
        if("1".equals(capitalRiskSwitch)) {
        	if(rMessage.getRiskStatus().equals("0")) {
            	info.setOrderStatus(1000);
            	//资金风控状态0，未推送1已推送，2资金风控通过4资金风控拒绝
            	info.setIfRisk(4);
            	try {
            		orderContract.updateOrderInfo(info);
            	}catch(Exception ex) {
            		LOGGER.error("资金风控拒绝，修改订单状态异常：{},{}", info.getOrderNo() , ex.getMessage());
            		return;
            	}
            	return;
            }
        }
        try {
        	info.setIfRisk(2);
    		orderContract.updateOrderInfo(info);
    	}catch(Exception ex) {
    		LOGGER.error("资金风控通过，修改订单状态异常：{},{}", info.getOrderNo() , ex.getMessage());
    	}
        OrderMessage oMessage = new OrderMessage();
        oMessage.setChannel(info.getChannel());
        oMessage.setFundCode(info.getFundCode());
        oMessage.setUserId(info.getUserId());
        oMessage.setOrderNo(rMessage.getOrderNo());
        oMessage.setIfTask(0);
        try {
        	capitalService.sendCapitalAfterRisk(oMessage);
        }catch (Exception e){
            LOGGER.error("推送资产异常：{}",e);
        }
    }



    @Value("${capital.risk.switch}")
	public void setCapitalRiskSwitch(String capitalRiskSwitch) {
		this.capitalRiskSwitch = capitalRiskSwitch;
	}

}
