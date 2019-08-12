package com.creativearts.nyd.web.controller.alipay;


import com.creativearts.nyd.pay.config.alipay.AliPayApiConfig;
import com.creativearts.nyd.web.controller.BaseController;

public abstract class AliPayApiController extends BaseController {

	public abstract AliPayApiConfig getApiConfig();
}