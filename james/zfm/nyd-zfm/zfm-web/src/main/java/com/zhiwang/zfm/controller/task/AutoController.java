//package com.zhiwang.zfm.controller.task;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//
//import javax.annotation.Resource;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//
//import net.sf.json.JSONObject;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.nyd.order.model.common.CommonResponse;
//import com.nyd.zeus.api.payment.PaymentRiskRecordService;
//import com.zhiwang.zfm.common.response.StatusConstants;
//
///**
// * 功能说明： 自动跑批
// * 修改人：yuanhao
// * 修改内容：
// * 修改注意点：
// */
//@Api(description="自动跑批api")
//@RestController
//@RequestMapping("/api/auto/")
//public class AutoController {
//	
//	private Logger logger = LoggerFactory.getLogger(AutoController.class);
//	
//	@Resource
//	private TaskOrder taskOrder;
//
//	@Resource
//	private TaskRisk taskRisk;
//	
//	@Resource
//	private PaymentRiskRecordService paymentRiskRecordService;
//	
//	
//	
//	/**
//	 * 功能说明：自动逾期订单明细
//	 * 修改人：yuanhao
//	 * 修改内容：
//	 * 修改注意点：
//	 *//*
//	@GetMapping("autoOrderOver")
//	@ApiOperation(value = "自动逾期订单明细")
//	@Produces(value = MediaType.APPLICATION_JSON)
//	public CommonResponse<JSONObject> autoOrderOver()  {
//		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
//		try{
//			taskOrder.autoOrderOver();
//			common.setSuccess(true);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("自动逾期订单出现异常!");
//			common.setCode(StatusConstants.SYS_ERROR);
//			common.setMsg(StatusConstants.SYS_ERROR_MSG);
//			common.setSuccess(false);
//		}
//		return common;
//	}
//	*//**
//	 * 功能说明：自动还款
//	 * 修改人：yuanhao
//	 * 修改内容：
//	 * 修改注意点：
//	 *//*
//	@GetMapping("autoOrderPayment")
//	@ApiOperation(value = "自动还款订单明细(待还)")
//	@Produces(value = MediaType.APPLICATION_JSON)
//	public CommonResponse<JSONObject> autoOrderPayment()  {
//		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
//		try{
//			taskOrder.autoOrderpayment();
//			common.setSuccess(true);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("自动还款出现异常!");
//			common.setCode(StatusConstants.SYS_ERROR);
//			common.setMsg(StatusConstants.SYS_ERROR_MSG);
//			common.setSuccess(false);
//		}
//		return common;
//	}
//	*//**
//	 * 功能说明：自动验证是否有效
//	 * 修改人：yuanhao
//	 * 修改内容：
//	 * 修改注意点：
//	 *//*
//	@GetMapping("autoCheckAuth")
//	@ApiOperation(value = "认证是否有效并更新")
//	@Produces(value = MediaType.APPLICATION_JSON)
//	public CommonResponse<JSONObject> autoCheckAuth()  {
//		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
//		try{
//			taskOrder.autoCheckauth();
//			common.setSuccess(true);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("认证是否有效异常!");
//			common.setCode(StatusConstants.SYS_ERROR);
//			common.setMsg(StatusConstants.SYS_ERROR_MSG);
//			common.setSuccess(false);
//		}
//		return common;
//	}
//	*//**
//	 * 功能说明：自动验证是否有效
//	 * 修改人：yuanhao
//	 * 修改内容：
//	 * 修改注意点：
//	 *//*
//	@GetMapping("autoRunprocessingpayrecord")
//	@ApiOperation(value = "还款处理中跑批")
//	@Produces(value = MediaType.APPLICATION_JSON)
//	public CommonResponse<JSONObject> autoRunprocessingpayrecord()  {
//		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
//		try{
//			taskOrder.autoRunprocessingpayrecord();
//			common.setSuccess(true);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("还款处理中跑批异常!");
//			common.setCode(StatusConstants.SYS_ERROR);
//			common.setMsg(StatusConstants.SYS_ERROR_MSG);
//			common.setSuccess(false);
//		}
//		return common;
//	}*/
//	
//	/**
//	 * 功能说明：放款跑批
//	 * 修改内容：
//	 * 修改注意点：
//	 */
//	@GetMapping("conductLoan")
//	@ApiOperation(value = "放款跑批")
//	@Produces(value = MediaType.APPLICATION_JSON)
//	public CommonResponse<JSONObject> loan()  {
//		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
//		try{
//			taskOrder.conductLoan();
//			common.setSuccess(true);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("还款跑批异常!");
//			common.setCode(StatusConstants.SYS_ERROR);
//			common.setMsg(StatusConstants.SYS_ERROR_MSG);
//			common.setSuccess(false);
//		}
//		return common;
//	}
//	/**
//	 * 功能说明：风控扣款
//	 * 修改内容：
//	 * 修改注意点：
//	 */
//	@GetMapping("riskReadyData")
//	@ApiOperation(value = "风控扣款跑批")
//	@Produces(value = MediaType.APPLICATION_JSON)
//	public CommonResponse<JSONObject> riskReadyData()  {
//		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
//		try{
//			taskRisk.readyData();
//			common.setSuccess(true);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("风控扣款跑批异常!");
//			common.setCode(StatusConstants.SYS_ERROR);
//			common.setMsg(StatusConstants.SYS_ERROR_MSG);
//			common.setSuccess(false);
//		}
//		return common;
//	}
//	/**
//	 * 功能说明：风控扣款
//	 * 修改内容：
//	 * 修改注意点：
//	 */
//	@GetMapping("riskProcessData")
//	@ApiOperation(value = "风控扣款处理中跑批")
//	@Produces(value = MediaType.APPLICATION_JSON)
//	public CommonResponse<JSONObject> riskProcessData()  {
//		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
//		try{
//			taskRisk.processData();
//			common.setSuccess(true);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("风控扣款处理中异常!");
//			common.setCode(StatusConstants.SYS_ERROR);
//			common.setMsg(StatusConstants.SYS_ERROR_MSG);
//			common.setSuccess(false);
//		}
//		return common;
//	}
//	/**
//	 * 功能说明：风控扣款
//	 * 修改内容：
//	 * 修改注意点：
//	 */
//	@GetMapping("riskInvalid")
//	@ApiOperation(value = "风控扣款失效跑批")
//	@Produces(value = MediaType.APPLICATION_JSON)
//	public CommonResponse<JSONObject> riskInvalid()  {
//		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
//		try{
//			taskRisk.doInvalid();
//			common.setSuccess(true);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("风控扣款失效跑批异常!");
//			common.setCode(StatusConstants.SYS_ERROR);
//			common.setMsg(StatusConstants.SYS_ERROR_MSG);
//			common.setSuccess(false);
//		}
//		return common;
//	}
//	/**
//	 * 功能说明：获取订单的扣款状况
//	 * 修改内容：
//	 * 修改注意点：
//	 */
//	@GetMapping("riskCost")
//	@ApiOperation(value = "风控扣款总额")
//	@Produces(value = MediaType.APPLICATION_JSON)
//	public CommonResponse<com.alibaba.fastjson.JSONObject> riskCost(String orderNo)  {
//		CommonResponse<com.alibaba.fastjson.JSONObject> common = new CommonResponse<com.alibaba.fastjson.JSONObject>();
//		try{
//			com.alibaba.fastjson.JSONObject json = paymentRiskRecordService.queryOrderCostMoney(orderNo).getData();
//			common.setData(json);
//			common.setSuccess(true);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("风控扣款总额异常!");
//			common.setCode(StatusConstants.SYS_ERROR);
//			common.setMsg(StatusConstants.SYS_ERROR_MSG);
//			common.setSuccess(false);
//		}
//		return common;
//	}
//	
//	/**
//	 * 功能说明：放款流水跑批
//	 * 修改内容：
//	 * 修改注意点：
//	 */
///*	@GetMapping("conductExecuteLoanRecord")
//	@ApiOperation(value = "放款流水跑批")
//	@Produces(value = MediaType.APPLICATION_JSON)
//	public CommonResponse<JSONObject> conductExecuteLoanRecord()  {
//		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
//		try{
//			taskOrder.conductExecuteLoanRecord();
//			common.setSuccess(true);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("放款流水跑批异常!");
//			common.setCode(StatusConstants.SYS_ERROR);
//			common.setMsg(StatusConstants.SYS_ERROR_MSG);
//			common.setSuccess(false);
//		}
//		return common;
//	}*/
//	
//
//	/*/**
//	　　* @description: 合利宝认证查询
//	　　* @param ${tags}
//	　　* @return ${return_type}
//	　　* @throws
//	　　* @date 2019/7/2 11:08
//	　　*/
//	/*@GetMapping("helibaoCustRecord")
//	@ApiOperation(value = "合利宝认证跑批")
//	@Produces(value = MediaType.APPLICATION_JSON)
//	public CommonResponse<JSONObject> helibaoCust(){
//		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
//		try {
//			taskOrder.helibaoCustResult();
//			common.setSuccess(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("合利宝认证跑批异常!");
//			common.setCode(StatusConstants.SYS_ERROR);
//			common.setMsg(StatusConstants.SYS_ERROR_MSG);
//			common.setSuccess(false);
//		}
//		return common;
//	}
//	
//	@GetMapping("queryHelibaoResult")
//	@ApiOperation(value = "合利宝认证结果查询跑批")
//	public CommonResponse<JSONObject> queryHelibaoResult(){
//		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
//		try {
//			taskOrder.queryHelibaoResult();
//			common.setSuccess(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			e.printStackTrace();
//			logger.error("合利宝认证结果查询异常!");
//			common.setCode(StatusConstants.SYS_ERROR);
//			common.setMsg(StatusConstants.SYS_ERROR_MSG);
//			common.setSuccess(false);
//		}
//		return common;
//	}*/
//	
//}
