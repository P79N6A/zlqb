package com.nyd.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.order.api.WithHoldOrderContract;
import com.nyd.order.dao.OrderDetailDao;
import com.nyd.order.dao.mapper.OrderMapper;
import com.nyd.order.dao.mapper.WithholdOrderMapper;
import com.nyd.order.entity.WithholdOrder;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.dto.CreatePayOrderDto;
import com.nyd.order.model.dto.RequestData;
import com.nyd.order.model.dto.SubmitWithholdDto;
import com.nyd.order.service.WithHoldOrderService;
import com.nyd.order.service.util.HttpUtil;
import com.nyd.order.service.util.OrderProperties;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 2017/11/14
 */
@Service("withHoldOrderContract")
public class WithHoldOrderContractImpl implements WithHoldOrderService ,WithHoldOrderContract{
    private static Logger LOGGER = LoggerFactory.getLogger(WithHoldOrderContractImpl.class);
    

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private WithholdOrderMapper withholdOrderMapper;
    
    @Autowired
    private OrderProperties orderProperties;
    
    
    
	@Override
	public ResponseData resendWithholdOrder() {
		
		List<WithholdOrder> list = null;
		try {
			list = withholdOrderMapper.getObjectsForTask();
		} catch (Exception e) {
			LOGGER.error("查询待处理划扣订单异常：" + e.getMessage());
			return ResponseData.error("待处理划扣订单异常");
		}
		if( list == null || list.size() == 0) {
			return ResponseData.success();
		}
		for(WithholdOrder wh : list) {
			LOGGER.info("处理代扣订单开始：" + JSON.toJSONString(wh));
			String payOrderNo = "";
			try {
				if(StringUtils.isBlank(wh.getPayOrderNo())) {
					payOrderNo = create(wh);
				}else {
					payOrderNo = wh.getPayOrderNo();
				}
				if(StringUtils.isBlank(payOrderNo)) {
					continue;
				}
				resend(wh,payOrderNo);
			}catch(Exception e) {
				LOGGER.error("处理划扣订单异常：",e);
				continue;
			}
		}
		return ResponseData.success();
	}
	
	private void resend(WithholdOrder wh, String payOrderNo) throws Exception{
		String create_order_url ="http://" + orderProperties.getCommonPayIp() + ":" + orderProperties.getCommonPayPort() + "/common/pay/createOrder";;

		   String withhold_url = "http://" + orderProperties.getCommonPayIp() + ":" + orderProperties.getCommonPayPort() + "/common/pay/submitWithhold";

		//调用代扣接口
        SubmitWithholdDto submitWithholdDto = new SubmitWithholdDto();
        submitWithholdDto.setPayOrderNo(payOrderNo);
        submitWithholdDto.setWithholdAmount(Double.valueOf(wh.getPayAmount().toString()));
        RequestData requestData = new RequestData();
        requestData.setData(submitWithholdDto);
        requestData.setRequestAppId("nyd");
        requestData.setRequestId(UUID.randomUUID().toString());
        requestData.setRequestTime(JSON.toJSONString(new Date()));
        String json = JSON.toJSONString(requestData);
        String withholdOrderNo = null;
        String sendPost = HttpUtil.sendPost(withhold_url,json);
        LOGGER.info("划扣请求响应信息：" + sendPost);
        JSONObject jsonObject = JSONObject.parseObject(sendPost);
        JSONObject data = jsonObject.getJSONObject("data");
        withholdOrderNo = data.getString("withholdOrderNo");
        wh.setWithholdOrderNo(withholdOrderNo);
        wh.setOrderStatus(1);
        try {
			withholdOrderMapper.update(wh);
		} catch (Exception e) {
			LOGGER.error("发起代扣更新代扣订单异常：",e);
			throw e;
		}
		
	}

	private String create(WithholdOrder wh) throws Exception{
		if(StringUtils.isBlank(wh.getMemberId())) {
			return null;
		}
		OrderInfo order = orderMapper.getOrderByMemberId(wh.getMemberId());
		if(order == null) {
			return null;
		}
		List<OrderDetailInfo> details = null;
		try {
			details = orderDetailDao.getObjectsByOrderNo(order.getOrderNo());
		} catch (Exception e) {
			LOGGER.error("查询订单详情异常：",e);
			throw e;
		}
		if(details == null || details.size() == 0) {
			return null;
		}
		OrderDetailInfo detail = details.get(0);
		
		String payOrderNo = "";
		try {
			payOrderNo = createWithholdOrder(order,detail,wh);
		}catch(Exception e) {
			LOGGER.error("创建代扣订单异常：",e);
			throw e;
		}
		if(StringUtils.isBlank(payOrderNo)) {
			return null;
		}
		wh.setPayOrderNo(payOrderNo);
		try {
			withholdOrderMapper.update(wh);
		} catch (Exception e) {
			LOGGER.error("创建代扣订单更新代扣订单异常：" ,e);
			throw e;
		}
		return payOrderNo;
	}

	private String createWithholdOrder(OrderInfo order,OrderDetailInfo detail,WithholdOrder withhold) throws Exception {
		String create_order_url ="http://" + orderProperties.getCommonPayIp() + ":" + orderProperties.getCommonPayPort() + "/common/pay/createOrder";;

		   String withhold_url = "http://" + orderProperties.getCommonPayIp() + ":" + orderProperties.getCommonPayPort() + "/common/pay/submitWithhold";

		 CreatePayOrderDto createPayOrderDto = new CreatePayOrderDto();
         createPayOrderDto.setBankcardNo(order.getBankAccount());
         createPayOrderDto.setBusinessOrderNo(order.getOrderNo());
         createPayOrderDto.setBusinessOrderType("评估报告");
         createPayOrderDto.setIdNumber(detail.getIdNumber());
         createPayOrderDto.setMobile(detail.getMobile());
         createPayOrderDto.setPayAmount(Double.valueOf(withhold.getPayAmount().toString()));
         createPayOrderDto.setPayChannelCode("yeepay");
         if(StringUtils.isBlank(order.getAppName())){
             createPayOrderDto.setMerchantCode("nyd");
         }else{
             createPayOrderDto.setMerchantCode(order.getAppName());
         }
         createPayOrderDto.setPayType(1);
         createPayOrderDto.setCallbackUrl(orderProperties.getWithholdCallbackUrl());
         RequestData requestData = new RequestData();
         requestData.setData(createPayOrderDto);
         requestData.setRequestAppId("nyd");
         requestData.setRequestId(UUID.randomUUID().toString());
         requestData.setRequestTime(JSON.toJSONString(new Date()));
         String json = JSON.toJSONString(requestData);
         String payOrderNo = null;
         String sendPost = HttpUtil.sendPost(create_order_url,json);
         LOGGER.info("创建订单请求响应信息：" + sendPost);
         JSONObject jsonObject = JSONObject.parseObject(sendPost);
         JSONObject data = jsonObject.getJSONObject("data");
         payOrderNo = data.getString("payOrderNo");
         return payOrderNo;
	}


}
