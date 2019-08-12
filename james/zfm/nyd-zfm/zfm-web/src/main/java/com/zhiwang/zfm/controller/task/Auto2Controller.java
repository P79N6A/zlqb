package com.zhiwang.zfm.controller.task;

import javax.annotation.Resource;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.api.payment.PaymentRiskRecordService;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.StatusConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(description="自动跑批api")
@RestController
@RequestMapping("/api/auto2/")
public class Auto2Controller {
	
	private Logger logger = LoggerFactory.getLogger(Auto2Controller.class);

	@Resource
	private TaskRisk taskRisk;
	
	@Resource
	private TaskOrder taskOrder;

	@Resource
	private PaymentRiskRecordService paymentRiskRecordService;
	
	@GetMapping("autoOrderOver")
	@ApiOperation(value = "逾期跑批")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<JSONObject> autoOrderOver()  {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try{
			taskOrder.autoOrderOver();
			common.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("逾期跑批异常!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	
	@GetMapping("autoOrderpayment")
	@ApiOperation(value = "还款跑批")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<JSONObject> autoOrderpayment()  {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try{
			taskOrder.autoOrderpayment();
			common.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("还款跑批异常!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	
	@GetMapping("autoOrderpaymentIng")
	@ApiOperation(value = "合利宝处理中跑批")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<JSONObject> autoOrderpaymentIng()  {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try{
			taskOrder.autoOrderpaymentIng();
			common.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("合利宝处理中跑批!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	
	@GetMapping("conductLoan")
	@ApiOperation(value = "放款跑批")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<JSONObject> loan()  {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try{
			taskOrder.conductLoan();
			common.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("还款跑批异常!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	/**
	 * 功能说明：风控扣款
	 * 修改内容：
	 * 修改注意点：
	 */
	@GetMapping("riskReadyData")
	@ApiOperation(value = "风控扣款跑批")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<JSONObject> riskReadyData()  {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try{
			taskRisk.readyData();
			common.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("风控扣款跑批异常!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	/**
	 * 功能说明：风控扣款
	 * 修改内容：
	 * 修改注意点：
	 */
	@GetMapping("riskProcessData")
	@ApiOperation(value = "风控扣款处理中跑批")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<JSONObject> riskProcessData()  {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try{
			taskRisk.processData();
			common.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("风控扣款处理中异常!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	/**
	 * 功能说明：风控扣款
	 * 修改内容：
	 * 修改注意点：
	 */
	@GetMapping("riskInvalid")
	@ApiOperation(value = "风控扣款失效跑批")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<JSONObject> riskInvalid()  {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try{
			taskRisk.doInvalid();
			common.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("风控扣款失效跑批异常!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	/**
	 * 功能说明：测试
	 * 修改内容：
	 * 修改注意点：
	 */
	@GetMapping("testChangjie")
	@ApiOperation(value = "changjie测试")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<JSONObject> testChangjie(String one,String two,String three,String four,String five,String six,String seven,String eight,String nine,String P,int times)  {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try{
			paymentRiskRecordService.testChangjie(one,two,three,four,five,six,seven,eight,nine,P,times);
			common.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("changjie测试异常!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	/**
	 * 功能说明：测试
	 * 修改内容：
	 * 修改注意点：
	 */
	@GetMapping("testXunlian")
	@ApiOperation(value = "xunlian测试")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<JSONObject> testXunlian(String one,String two,String three,String four,String five,String P,int times)  {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try{
			paymentRiskRecordService.testXunlian(one,two,three,four,five,P,times);
			common.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("xunlian测试异常!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	/**
	 * 功能说明：获取订单的扣款状况
	 * 修改内容：
	 * 修改注意点：
	 */
	@GetMapping("riskCost")
	@ApiOperation(value = "风控扣款总额")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<com.alibaba.fastjson.JSONObject> riskCost(String orderNo)  {
		CommonResponse<com.alibaba.fastjson.JSONObject> common = new CommonResponse<com.alibaba.fastjson.JSONObject>();
		try{
			com.alibaba.fastjson.JSONObject json = paymentRiskRecordService.queryOrderCostMoney(orderNo).getData();
			common.setData(json);
			common.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("风控扣款总额异常!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	/**
	 * 功能说明：获取订单的扣款状况
	 * 修改内容：
	 * 修改注意点：
	 */
	@GetMapping("noticeOrder")
	@ApiOperation(value = "通知订单系统")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<com.alibaba.fastjson.JSONObject> noticeOrder()  {
		CommonResponse<com.alibaba.fastjson.JSONObject> common = new CommonResponse<com.alibaba.fastjson.JSONObject>();
		try{
			taskRisk.doNoticeInvalid();
			common.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("通知订单系统!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	
	/**
	 * 功能说明：退款处理
	 * 修改内容：
	 * 修改注意点：
	 */
	@GetMapping("refundLoan")
	@ApiOperation(value = "退款跑批")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<com.alibaba.fastjson.JSONObject> refundLoan()  {
		CommonResponse<com.alibaba.fastjson.JSONObject> common = new CommonResponse<com.alibaba.fastjson.JSONObject>();
		try{
			taskOrder.refundLoan();
			common.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("退款跑批系统异常!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	
	/**
	 * 功能说明：放款流水跑批
	 * 修改内容：
	 * 修改注意点：
	 */
	@GetMapping("conductExecuteLoanRecord")
	@ApiOperation(value = "放款流水跑批")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<JSONObject> conductExecuteLoanRecord()  {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try{
			taskOrder.conductExecuteLoanRecord();
			common.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("放款流水跑批异常!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
//	
//
//	/*/**
//	　　* @description: 合利宝认证查询
//	　　* @param ${tags}
//	　　* @return ${return_type}
//	　　* @throws
//	　　* @author ${}
//	　　* @date 2019/7/2 11:08
//	　　*/
	@GetMapping("helibaoCustRecord")
	@ApiOperation(value = "合利宝认证跑批")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<JSONObject> helibaoCust(){
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try {
			taskOrder.helibaoUserRegister();
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("合利宝认证跑批异常!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	
	@GetMapping("queryHelibaoResult")
	@ApiOperation(value = "合利宝认证结果查询跑批")
	public CommonResponse<JSONObject> queryHelibaoResult(){
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try {
			taskOrder.helibaoUserSuccessResult();
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			logger.error("合利宝认证结果查询异常!");
			common.setCode(StatusConstants.SYS_ERROR);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setSuccess(false);
		}
		return common;
	}
	
}
