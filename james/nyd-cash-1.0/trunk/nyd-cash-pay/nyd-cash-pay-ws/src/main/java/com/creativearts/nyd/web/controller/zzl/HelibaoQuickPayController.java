package com.creativearts.nyd.web.controller.zzl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creativearts.nyd.collectionPay.model.zzl.CommonResponse;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.BankCardUnbindVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.BankCardbindVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.BindCardSendValidateCodeVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.BindPaySendValidateCodeVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.ConfirmBindCardVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.ConfirmBindPayVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.FirstPayConfirmPayVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.FirstPaySendValidateCodeVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.QueryOrderVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.QuickPayBindCardPreOrderVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.QuickPayBindPayPreOrderVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.QuickPayFirstPayPreOrderVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BankCardUnbindResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BankCardbindResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BindCardPreOrderResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BindCardSendValidateCodeResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BindPaySendValidateCodeResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.ConfirmBindCardResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.ConfirmBindPayResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QueryOrderResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QuickPayBindPayPreOrderResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QuickPayConfirmPayResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QuickPayCreateOrderResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QuickPaySendValidateCodeResponseVo;
import com.creativearts.nyd.pay.service.zzl.helibao.ConfigCacheManager;
import com.nyd.pay.api.zzl.HelibaoQuickPayService;




@RestController
@RequestMapping("/api/quickPay")
@Api(description="合利宝-标准快捷")
public class HelibaoQuickPayController{
	
	
	 @Autowired
	 private ConfigCacheManager configCacheManager;
	 
	 @Autowired
	 private HelibaoQuickPayService helibaoQuickPayService;

		

	//--------------首次支付预下单
	@PostMapping("/quickPayFirstPayPreOrder")
	@ApiOperation(value = "4.1 首次支付预下单")
	@Produces(value = MediaType.APPLICATION_JSON)
	public QuickPayCreateOrderResponseVo quickPayFirstPayPreOrder(@ModelAttribute QuickPayFirstPayPreOrderVo requestVo){
		return helibaoQuickPayService.quickPayFirstPayPreOrder(requestVo,"");
	}


	//----------------首次支付短信
	@PostMapping("/firstPaySendValidateCode")
	@ApiOperation(value = "4.2 首次支付短信")
	@Produces(value = MediaType.APPLICATION_JSON)
	public QuickPaySendValidateCodeResponseVo firstPaySendValidateCode(@ModelAttribute FirstPaySendValidateCodeVo requestVo){
		return helibaoQuickPayService.firstPaySendValidateCode(requestVo,"");
	}


	//---------------首次支付确认支付
	@PostMapping("/firstPayconfirmPay")
	@ApiOperation(value = "4.3 首次支付确认支付")
	@Produces(value = MediaType.APPLICATION_JSON)
	public QuickPayConfirmPayResponseVo firstPayconfirmPay(@ModelAttribute FirstPayConfirmPayVo requestVo){
		return helibaoQuickPayService.firstPayconfirmPay(requestVo,"");
	}


	//--------------绑卡预下单
	@PostMapping("/quickPayBindCardPreOrder")
	@ApiOperation(value = "4.4 绑卡预下单")
	@Produces(value = MediaType.APPLICATION_JSON)
	public BindCardPreOrderResponseVo quickPayBindCardPreOrder(@ModelAttribute QuickPayBindCardPreOrderVo requestVo){
		return helibaoQuickPayService.quickPayBindCardPreOrder(requestVo,"");
	}


	//---------------鉴权绑卡短信
	@PostMapping("/bindCardSendValidateCode")
	@ApiOperation(value = "4.5 鉴权绑卡短信")
	@Produces(value = MediaType.APPLICATION_JSON)
	public BindCardSendValidateCodeResponseVo bindCardSendValidateCode(@ModelAttribute BindCardSendValidateCodeVo requestVo){
		return helibaoQuickPayService.bindCardSendValidateCode(requestVo,"");
	}


	//--------------鉴权绑卡
	@PostMapping("/bindCard")
	@ApiOperation(value = "4.6 鉴权绑卡")
	@Produces(value = MediaType.APPLICATION_JSON)
	public ConfirmBindCardResponseVo bindCard(@ModelAttribute ConfirmBindCardVo requestVo){
		return helibaoQuickPayService.bindCard(requestVo,"");
	}



	//--------------绑卡支付预下单
	@PostMapping("/quickPayBindPayPreOrder")
	@ApiOperation(value = "4.7 绑卡支付预下单")
	@Produces(value = MediaType.APPLICATION_JSON)
	public QuickPayBindPayPreOrderResponseVo quickPayBindPayPreOrder(@ModelAttribute QuickPayBindPayPreOrderVo requestVo){
		return helibaoQuickPayService.quickPayBindPayPreOrder(requestVo,"");
	}



	//------------绑卡支付短信
	@PostMapping("/sendValidateCode")
	@ApiOperation(value = "4.8 绑卡支付短信")
	@Produces(value = MediaType.APPLICATION_JSON)
	public BindPaySendValidateCodeResponseVo sendValidateCode(@ModelAttribute BindPaySendValidateCodeVo requestVo){
		return helibaoQuickPayService.sendValidateCode(requestVo,"");
	}


	//----------------绑卡支付

	@PostMapping("/confirmBindPay")
	@ApiOperation(value = "4.9 绑卡支付")
	@Produces(value = MediaType.APPLICATION_JSON)
	public ConfirmBindPayResponseVo confirmBindPay(@ModelAttribute ConfirmBindPayVo requestVo){
		return helibaoQuickPayService.confirmBindPay(requestVo,"");
	}

	//----------------进入订单查询接口
	@PostMapping("/queryOrder")
	@ApiOperation(value = "4.11 进入订单查询接口")
	@Produces(value = MediaType.APPLICATION_JSON)
	public QueryOrderResponseVo queryOrder(@ModelAttribute QueryOrderVo requestVo){
		return helibaoQuickPayService.queryOrder(requestVo);
	}

	
	//----------------银行卡解绑
	@PostMapping("/bankCardUnbind")
	@ApiOperation(value = "4.12 银行卡解绑")
	@Produces(value = MediaType.APPLICATION_JSON)
	public BankCardUnbindResponseVo bankCardUnbind(@ModelAttribute BankCardUnbindVo requestVo){
		return helibaoQuickPayService.bankCardUnbind(requestVo,"");
	}
	
	//----------------用户绑定银行卡信息查询
	@PostMapping("/bankCardbindList")
	@ApiOperation(value = "4.13 用户绑定银行卡信息查询")
	@Produces(value = MediaType.APPLICATION_JSON)
	public BankCardbindResponseVo bankCardbindList(@ModelAttribute BankCardbindVo requestVo){
		return helibaoQuickPayService.bankCardbindList(requestVo,"");
	}
	
	@SuppressWarnings({ "rawtypes", "static-access" })
	@PostMapping("/clearCash")
	@ApiOperation(value = "清除cash缓存")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse clearCash() throws Exception {

		//清除缓存
		configCacheManager.clearCache();
		return new CommonResponse().success("");
	}
    

	
}
