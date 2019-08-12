package com.creativearts.nyd.web.controller.weixinpay;


import com.creativearts.nyd.pay.service.weixinpay.WxPayApiConfig;
import com.creativearts.nyd.web.controller.BaseController;

public abstract class WxPayApiController  extends BaseController {
	public abstract WxPayApiConfig getApiConfig();
}