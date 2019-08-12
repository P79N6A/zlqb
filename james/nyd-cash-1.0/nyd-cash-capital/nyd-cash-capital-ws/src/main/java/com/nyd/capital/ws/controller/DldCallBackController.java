package com.nyd.capital.ws.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.nyd.capital.service.utils.Constants;
import com.nyd.order.model.msg.OrderMessage;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.model.dld.LoanCallBackParams;
import com.nyd.capital.service.CapitalService;
import com.nyd.capital.service.dld.config.DldConfig;
import com.nyd.capital.service.dld.service.DldService;
import com.nyd.capital.service.dld.utils.D2DUtil;

/**
 *
 * @author zhangdk
 *
 */
@RestController
@RequestMapping("/capital/dld")
public class DldCallBackController {
	Logger logger = LoggerFactory.getLogger(DldCallBackController.class);

	@Autowired
	private DldService dldService;
	@Autowired
	private DldConfig dldConfig;
	@Autowired
	private RedisTemplate redisTemplate;
    @Autowired
    private CapitalService capitalService;

	@RequestMapping(value = "/callback", produces = "application/json")
	public String callback(@RequestBody LoanCallBackParams callback) {
		String result = null;
		String sign = callback.getSignature();
		callback.setDldShopKey(dldConfig.getShopKEY());
		logger.info("回调信息" + JSON.toJSONString(callback));
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			logger.error("线程异常",e1);
			return "fail";
		}
		Map<String, Object> re = callback.getSignMap();
		/*
		 * re.remove("signature"); D2DUtil.setSignature(re,dldConfig.getShopKEY());
		 */
		boolean check = D2DUtil.checkSignature(re, sign);
		if (!check) {
			// if(null != sign && !sign.equals((String)re.get("Signature"))) {
			logger.info("******验签失败：" + re.get("signature"));
			result = "fail";
		} else {
			try {
				result = dldService.callBack(re);
				result = "success";
			} catch (Exception e) {
				logger.error("订单处理失败：" + e.getMessage());
				result = "fail";
			}
			if ("fail".equals(result)) {
				redisTemplate.delete(Constants.DLD_CALLBACK_PREFIX + (String)re.get("orderid"));
			}
		}
		return result;
	}

	@RequestMapping(value = "/callback/reset", produces = "application/json")
	public String callbackByList(@RequestBody List<LoanCallBackParams> callback) {
		String result = null;
		logger.info("回调信息" + JSON.toJSONString(callback));
		for (LoanCallBackParams call : callback) {
			try {
				result = dldService.callBackRetry(call);
				result = "success";
			} catch (Exception e) {
				logger.error("订单处理失败：" + e.getMessage());
				result = "fail";
			}
			if ("fail".equals(result)) {
				redisTemplate.delete(Constants.DLD_CALLBACK_PREFIX+call.getOrderid());
			}
		}
		return result;
	}

	@RequestMapping(value = "/index", produces = "text/plain;charset=UTF-8")
	public String index() {
		return "欢迎index";
	}

	@RequestMapping(value = "/errerHandle", produces = "application/json")
	public ResponseData errerHandle(@RequestBody OrderMessage message) {
		return dldService.errerHandle(message);
	}
	@RequestMapping(value = "/dldQueryOrder", produces = "application/json")
	public ResponseData dldQueryOrder(@RequestBody OrderMessage message) {
		return capitalService.queryKdlcWaitLoan("dld");
	}

}
