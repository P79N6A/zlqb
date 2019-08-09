package com.nyd.order.ws.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.nyd.order.api.zzl.THelibaoRecordService;
import com.nyd.order.model.OrderCashOut;
import com.nyd.order.model.THeLiBaoLoanRecord;
import com.nyd.order.model.THelibaoRecord;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.user.api.zzl.UserForSLHServise;
import com.nyd.user.model.vo.UserBankInfo;
import com.nyd.zeus.api.payment.PaymentRiskRecordService;
import com.tasfe.framework.support.model.ResponseData;

@RestController
@RequestMapping("order")
public class CashOutController {
	 private static Logger logger = LoggerFactory.getLogger(CashOutController.class);
	 @Autowired
	 THelibaoRecordService tHelibaoRecordService;
	 @Autowired
	 private PaymentRiskRecordService paymentRiskRecordService;
	  @Autowired
	  UserForSLHServise userForSLHServise;
	  @RequestMapping(value="/query/orderTHelibaoRecord",method = RequestMethod.POST, produces = "application/json")
		 public ResponseData queryOrderTHelibaoRecord(@RequestBody THeLiBaoLoanRecord t) {
		  UserBankInfo userBind= userForSLHServise.getuserByUserId(t.getUserId());
		  ResponseData comm = new ResponseData<>();
		     comm.setStatus("0");
			 comm.setCode("200");
			 comm.setMsg("提现成功！");
			 comm.setData(userBind);
		try {
			  com.nyd.zeus.model.common.CommonResponse<JSONObject> json = paymentRiskRecordService.queryOrderCostMoney(t.getOrderNo());
				double costMoney = json.getData().getDoubleValue("costMoney");
				logger.info("costMoney >>>>>  风控扣款金额为：》》》》"+costMoney);
				if(costMoney<198) {
					 comm.setStatus("0");
					 comm.setCode("-1");
					 comm.setMsg("请确保银行卡充足余额，否则影响放款");
					 return comm;
				}
				if(userForSLHServise.queryUserByUserId(t.getUserId())) {
				 THeLiBaoLoanRecord data= tHelibaoRecordService.getTHeLiBaoLoanRecord(t.getOrderNo());
				 THelibaoRecord hlb =  tHelibaoRecordService.getTHelibaoRecord(t.getUserId());
				 if(data!=null&&hlb!=null&&hlb.getFailCount()<=20) {
					 logger.info("银行卡已经绑定并且扣过服务费》》》》》服务费金额为："+costMoney);
					 tHelibaoRecordService.updateStatus(t.getOrderNo());
					 return comm;
				 }
				 
				 if(hlb==null) {
					 THelibaoRecord T = new THelibaoRecord();
					 T.setId(uuid());
					 T.setCreateTime(new Date());
					 T.setOrderNo(t.getOrderNo());
					 T.setStatus(0);
					 T.setUserNumber(null);
					 T.setUserId(t.getUserId());
					 try {
						 tHelibaoRecordService.insertOrderRecord(T);
					} catch (Exception e) {
						e.printStackTrace();
					}
					 
				 }
				 if(data==null) {
					 THeLiBaoLoanRecord tr = new THeLiBaoLoanRecord();
					 tr.setId(uuid());
					 tr.setCreateTime(new Date());
					 tr.setOrderNo(t.getOrderNo());
					 tr.setUpdateTime(null);
					 tr.setSeqNo("");
					 tr.setSysUserId(null);
					 tr.setRemark("");
					 tr.setStatus(0);
					 tr.setUserId(t.getUserId());
					 tr.setType(1);
					 try {
						 tHelibaoRecordService.insertTHeLiBaoLoanRecord(tr);
					} catch (Exception e) {
						e.printStackTrace();
					}
					 tHelibaoRecordService.updateStatus(t.getOrderNo());
				 }
				 }else {
					 comm.setStatus("0");
					 comm.setCode("-2");
					 comm.setMsg("未绑定银行卡！");
					 logger.info("银行卡未绑定！");
					 return comm;
				 }
				 logger.info("银行卡已经绑定并且扣过服务费》》》》》服务费金额为："+costMoney);
		} catch (Exception e) {
			logger.info("提现失败！");
			e.printStackTrace();
			return comm.error("提现失败！");
		}
			return comm;
			 
		 }
		 
	   
	   
	   @RequestMapping(value="/query/orderCashOut",method = RequestMethod.POST, produces = "application/json")
		 public CommonResponse queryOrderCashOut(@RequestBody THeLiBaoLoanRecord t) {
			 CommonResponse comm = new CommonResponse<>();
			OrderCashOut data =  tHelibaoRecordService.getOrder(t.getOrderNo());
			BigDecimal sum = null;
			sum=(data.getInterest().add(data.getManagerFee())).add(data.getRealLoanAmount());
			data.setSum(sum);
			if(data!=null) {
				comm.setData(data);
				comm.setSuccess(true);
				return comm;
			}else {
				return comm.success("查询失败！");
			}
		 }
	   
		 public static String uuid() {
			return UUID.randomUUID().toString().replaceAll("-", "");
			 
		 }
}
