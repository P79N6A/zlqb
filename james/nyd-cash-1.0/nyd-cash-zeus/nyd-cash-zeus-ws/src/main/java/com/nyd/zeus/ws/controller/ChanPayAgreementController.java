package com.nyd.zeus.ws.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.api.zzl.chanpay.ChangJiePaymentService;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.helibao.util.StatusConstants;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieCancelCardVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieCardBinVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieMerchantVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJiePrePayVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieQueryBindVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieQueryMerchantVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangPayBindCardVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangPaySendMsgVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangjieQueryPayVO;
import com.nyd.zeus.model.helibao.vo.pay.resp.chanpay.ChangJieDFResp;
import com.nyd.zeus.model.helibao.vo.pay.resp.chanpay.ChangJiePayCommonResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *  功能说明：畅捷协议支付 修改人：hwt 修改内容： 修改注意点：
 */
@Controller
@RequestMapping("/api/changAgreement/")
@Api(description = " 畅捷协议支付api")
public class ChanPayAgreementController {

	private Logger logger = LoggerFactory.getLogger(ChanPayAgreementController.class);

	@Autowired
	private ChangJiePaymentService changJiePaymentService;
	


	
	/**
	 *  功能说明： 发送短信验证码接口 修改人：hwt 修改内容： 修改注意点：
	 */
	@ResponseBody
	@ApiOperation(value = " 发送短信验证码")
	@RequestMapping(value = "sendMsg", method = RequestMethod.POST, produces = "application/json")
	public CommonResponse<ChangJiePayCommonResp> sendMsg(@ModelAttribute ChangPaySendMsgVO changPaySendMsgVO) {

		CommonResponse<ChangJiePayCommonResp> common = new CommonResponse<ChangJiePayCommonResp>();

		try {
			common = changJiePaymentService.sendMsg(changPaySendMsgVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("  发送短信验证码异常" + e);
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	

	/**
	 *  功能说明： 确认绑卡接口 修改人：hwt 修改内容： 修改注意点：
	 */
	@ResponseBody
	@PostMapping("/bindCard")
	@ApiOperation(value = " 确认绑卡接口")
	@RequestMapping(value = "/bindCard", method = RequestMethod.POST, produces = "application/json")
	public CommonResponse<ChangJiePayCommonResp> bindCard(@ModelAttribute ChangPayBindCardVO changPayBindCardVO) {

		CommonResponse<ChangJiePayCommonResp> common = new CommonResponse<ChangJiePayCommonResp>();

		try {
			common = changJiePaymentService.bindCard(changPayBindCardVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("  确认绑卡接口异常" + e);
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	
	
	
	/**
	 *  功能说明： 取消绑卡接口 修改人：hwt 修改内容： 修改注意点：
	 */
	@ResponseBody
	@PostMapping("/cancelBind")
	@ApiOperation(value = " 取消绑卡接口")
	@RequestMapping(value = "/cancelBind", method = RequestMethod.POST, produces = "application/json")
	public CommonResponse<ChangJiePayCommonResp> cancelBind(@ModelAttribute ChangJieCancelCardVO cancelCardVO) {

		CommonResponse<ChangJiePayCommonResp> common = new CommonResponse<ChangJiePayCommonResp>();

		try {
			common = changJiePaymentService.cancelBindCard(cancelCardVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" 取消绑卡接口异常" + e);
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	
	
	
	
	/**
	 *  功能说明： 绑卡查询接口 修改人：hwt 修改内容： 修改注意点：
	 */
	@ResponseBody
	@PostMapping("/queryBind")
	@ApiOperation(value = " 绑卡查询接口")
	@RequestMapping(value = "/queryBind", method = RequestMethod.POST, produces = "application/json")
	public CommonResponse<ChangJiePayCommonResp> queryBind(@ModelAttribute ChangJieQueryBindVO queryBindVO) {

		CommonResponse<ChangJiePayCommonResp> common = new CommonResponse<ChangJiePayCommonResp>();

		try {
			common = changJiePaymentService.queryBind(queryBindVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" 取消绑卡接口异常" + e);
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	
	
	/**
	 *  功能说明 支付接口 修改人：hwt 修改内容： 修改注意点：
	 */
	@ResponseBody
	@PostMapping("/prePay")
	@ApiOperation(value = " 支付")
	@RequestMapping(value = "/prePay", method = RequestMethod.POST, produces = "application/json")
	public CommonResponse<ChangJiePayCommonResp> prePay(@ModelAttribute ChangJiePrePayVO changJiePrePayVO) {

		CommonResponse<ChangJiePayCommonResp> common = new CommonResponse<ChangJiePayCommonResp>();

		try {
			common = changJiePaymentService.prePay(changJiePrePayVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("  畅捷支付异常" + e);
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	
	
	/**
	 *  功能说明 支付接口 修改人：hwt 修改内容： 修改注意点：
	 */
	@ResponseBody
	@ApiOperation(value = " 畅捷支付查询")
	@RequestMapping(value = "/cjqueryPay", method = RequestMethod.POST, produces = "application/json")
	public CommonResponse<ChangJiePayCommonResp> queryPay(@ModelAttribute ChangjieQueryPayVO changjieQueryPayVO) {

		CommonResponse<ChangJiePayCommonResp> common = new CommonResponse<ChangJiePayCommonResp>();

		try {
			common = changJiePaymentService.queryPay(changjieQueryPayVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("  富友协议支付异常" + e);
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}

	/**
	 *  功能说明 代付接口 （转账给客户）接口 修改人：hwt 修改内容： 修改注意点：
	 */
	@PostMapping("/suspay")
	@ApiOperation(value = " 代付接口")
	@ResponseBody
	@RequestMapping(value = "/suspay", method = RequestMethod.POST, produces = "application/json")
	public CommonResponse<ChangJieDFResp> suspay(@ModelAttribute ChangJieMerchantVO changJieMerchantVO) {

		CommonResponse<ChangJieDFResp> common = new CommonResponse<ChangJieDFResp>();

		try {
			common = changJiePaymentService.payForAnother(changJieMerchantVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("  chang'jie代付接口异常" + e);
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	
	
	/**
	 *  功能说明 畅捷代付查询接口 （转账给客户）接口 修改人：hwt 修改内容： 修改注意点：
	 */
	@ResponseBody
	@PostMapping("/querysuspay")
	@ApiOperation(value = " 代付查询接口")
	@RequestMapping(value = "/querysuspay", method = RequestMethod.POST, produces = "application/json")
	public CommonResponse<ChangJieDFResp> querysuspay(@ModelAttribute ChangJieQueryMerchantVO changJieQueryMerchantVO) {

		CommonResponse<ChangJieDFResp> common = new CommonResponse<ChangJieDFResp>();

		try {
			common = changJiePaymentService.queryPayForAnother(changJieQueryMerchantVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("  代付查询异常" + e);
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	
	/**
	 *  卡bin查询接口
	 */
	@ResponseBody
	@ApiOperation(value = "卡bin查询接口")
	@RequestMapping(value = "/queryCardBin", method = RequestMethod.POST, produces = "application/json")
	public CommonResponse<JSONObject> queryCardBin(@ModelAttribute ChangJieCardBinVO changJieCardBinVO) {

		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try {
			common = changJiePaymentService.queryCardBin(changJieCardBinVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("   卡bin查询异常" + e);
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	
	
}
