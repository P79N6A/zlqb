package com.zhiwang.zfm.controller.webapp.pay.helibao;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.zeus.api.zzl.HelibaoQuickPayService;
import com.nyd.zeus.model.helibao.vo.pay.req.BankCardbindVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.BankCardbindResponseVo;







@RestController
@RequestMapping("/api/asynchronous")
@Api(description="合利宝-异步通知")
public class HelibaoQuickPayNoticeController{
	
	 private static final Logger log = LoggerFactory.getLogger(HelibaoQuickPayNoticeController.class);
	
	 @Autowired
	 private HelibaoQuickPayService helibaoQuickPayService;

	//----------------异步通知接口
	@PostMapping("/quickPayConfirmPay")
	@ApiOperation(value = "4.10 标准快捷异步通知")
	@Produces(value = MediaType.APPLICATION_JSON)
	public String quickPayConfirmPay(){
		//return helibaoQuickPayService.quickPayConfirmPay(requestVo);
		return "success";
	}

	//----------------异步通知接口
	@PostMapping("/entrustedLoanPay")
	@ApiOperation(value = "4.8 委托代付异步通知")
	@Produces(value = MediaType.APPLICATION_JSON)
	public String notfyOrderQueryResVo(){
		//log.info("--------委托代付结果通知----------"+JSON.toJSONString(notifyData, SerializerFeature.WriteMapNullValue));
		//helibaoEntrustedLoanService.notfyOrderQueryResVo(notifyData);
		return "success";

	}
	
	@PostMapping("/bankCardbindList")
	@ApiOperation(value = "用户绑定银行卡信息查询")
	@Produces(value = MediaType.APPLICATION_JSON)
	public BankCardbindResponseVo bankCardbindList(@ModelAttribute BankCardbindVo requestVo){
		return helibaoQuickPayService.bankCardbindList(requestVo);
	}


}
