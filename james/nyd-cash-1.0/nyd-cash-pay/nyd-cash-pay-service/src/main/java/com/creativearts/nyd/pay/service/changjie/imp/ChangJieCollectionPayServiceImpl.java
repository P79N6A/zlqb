/**
 * Project Name:nyd-cash-pay-service
 * File Name:ChangJieCollectionPayServiceImpl.java
 * Package Name:com.creativearts.nyd.pay.service.changjie.imp
 * Date:2018年9月10日下午5:50:47
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.service.changjie.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.collectionPay.model.changjie.CardBinRequest;
import com.creativearts.nyd.collectionPay.model.changjie.CardBinResponse;
import com.creativearts.nyd.pay.service.changjie.ChangJieCollectionPayService;
import com.creativearts.nyd.pay.service.changjie.enums.ChangJieEnum;
import com.creativearts.nyd.pay.service.changjie.properties.ChangJieConfig;
import com.creativearts.nyd.pay.service.changjie.properties.ChangJiePublicQuickPayRequestConfig;
import com.creativearts.nyd.pay.service.changjie.util.ChangJieSupplyUtil;
import com.creativearts.nyd.pay.service.changjie.util.JsonUtil;
import com.nyd.pay.dao.CardBinDao;
import com.nyd.pay.entity.CardBin;
import com.creativearts.nyd.pay.service.changjie.util.ChangJieCollectionPayUtil;
import com.tasfe.framework.support.model.ResponseData;
import com.tasfe.framework.uid.service.BizCode;
import com.tasfe.framework.uid.service.IdGenerator;

/**
 * ClassName:ChangJieCollectionPayServiceImpl <br/>
 * Date:     2018年9月10日 下午5:50:47 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Service
public class ChangJieCollectionPayServiceImpl implements ChangJieCollectionPayService {
	Logger log = LoggerFactory.getLogger(ChangJieQuickPayServiceImpl.class);
	@Autowired
	private ChangJiePublicQuickPayRequestConfig request;
	
	@Autowired
	private ChangJieConfig changJieConfig;
	
	@Autowired
	private IdGenerator idGenerator;
	
	@Autowired
	private CardBinDao cardBinDao;
	/**
	 * 
	 * 卡BIN信息查询
	 * @see com.creativearts.nyd.pay.service.changjie.ChangJieCollectionPayService#getCardBin(com.creativearts.nyd.collectionPay.model.changjie.CardBinRequest)
	 */
	@Override
	public ResponseData getCardBin(CardBinRequest cardBinRequest) {
		log.info("卡BIN信息查询接口serviceImp请求参数打印:{}",JSON.toJSONString(cardBinRequest));
		try {
			cardBinRequest.setOutTradeNo("cj"+idGenerator.generatorId(BizCode.ORDER_NYD));
			Map<String, String> map = ChangJieCollectionPayUtil.getCardBinRequestParms(cardBinRequest,changJieConfig,request);
			log.info("卡BIN信息查询接口参数组装打印:{}",JSON.toJSONString(map));
			String result = ChangJieSupplyUtil.gatewayPost(map,request.getInputCharset(),changJieConfig.getZaoYiPrivateKey(),changJieConfig.getChangJieReqUrl());
			log.info("调用畅捷卡BIN信息查询接口响应结果:{}",JSON.toJSONString(result));
			CardBinResponse response= JsonUtil.readValueToBean(result,CardBinResponse.class);
			log.info("转换成CardBinResponse:{}",JSON.toJSONString(response));
			if(null == response || ChangJieEnum.ACCEPT_STATUS_FAIL.getMsg().equals(response.getAcceptStatus())) {
				log.error("调用畅捷卡BIN信息查询接口响应失败");
				return ResponseData.error("服务器开小差");
			}
			
			if(!ChangJieEnum.ORIGINAL_RET_CODE.getMsg().equals(response.getOriginalRetCode()) || !ChangJieEnum.IS_VALID.getMsg().equals(response.getIsValid())) {
				log.error("请输入正确银行卡号");
				return ResponseData.error("请输入正确银行卡号");
			}
			
			//这里要加if判断https请求不验签    || "P".equals(response.getAcceptStatus())
			if(!ChangJieEnum.CARD_TYPE_DC.getMsg().equals(response.getCardType())) {
				log.error("只支持借记卡");
				return ResponseData.error("只支持借记卡");
			}
			
			//根据卡Bin去查询卡的缩写
			List<CardBin> list = cardBinDao.queryCardBinList(response.getCardBin());
			
			if(CollectionUtils.isEmpty(list)) {
				log.error("畅捷响应的卡Bin在DB中查询不到结果,卡Bin表需要维护");
				return ResponseData.error("服务器开小差");
			}
			if(!cardBinRequest.getBankCode().equals(list.get(0).getBankCode())) {
				log.error("银行卡名称与输入银行卡号不匹配");
				return ResponseData.error("银行卡名称与输入银行卡号不匹配");
			}
			return ResponseData.success(response);
		} catch (Exception e) {
			log.info("卡BIN信息查询接口异常:{}",e);
			return ResponseData.error("服务器开小差");
		}
	}
}

