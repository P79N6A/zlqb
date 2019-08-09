package com.nyd.user.ws.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.nyd.order.model.YmtKzjrBill.enums.BillStatusEnum;
import com.nyd.order.model.common.ChkUtil;
import com.nyd.user.api.zzl.UserForOrderPayBackServise;
import com.nyd.user.model.BankUser;
import com.nyd.user.model.request.PayBackQueryReq;
import com.nyd.user.model.vo.PayBackQueryVO;
import com.nyd.zeus.api.zzl.ZeusForWHServise;
import com.nyd.zeus.api.zzl.ZeusForOrderPayBackServise;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.helibao.PaymentVo;
import com.nyd.zeus.model.helibao.util.chanpay.DateUtils;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 订单还款相关接口
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class OrderPayBackController {
	private static Logger LOGGER = LoggerFactory
			.getLogger(OrderPayBackController.class);

	@Autowired
	private ZeusForOrderPayBackServise zeusForOrderPayBackServise;
	
	@Autowired
	private UserForOrderPayBackServise userForOrderPayBackServise;
	
	@Autowired
	private ZeusForWHServise zeusForWHServise;
	

	@RequestMapping(value = "/order/payBack/query", method = RequestMethod.POST, produces = "application/json")
	public ResponseData<PayBackQueryVO> queryPayBack(@RequestBody PayBackQueryReq req)
			throws Throwable {
		LOGGER.info("payBackQuery:{}", req.getOrderNo());
		ResponseData<PayBackQueryVO> resp = new ResponseData<>();
		BillInfo bill = zeusForOrderPayBackServise.queryBillByOrderNo(req.getOrderNo());
		if(ChkUtil.isEmpty(bill))
			return (ResponseData<PayBackQueryVO>) resp.error("未查询到还款信息");
		
		PayBackQueryVO vo = new PayBackQueryVO();
		// 还款中
		if(BillStatusEnum.REPAY_ING.getCode().equals(bill.getBillStatus()))
			vo.setRemainDays(getOverDay(new Date(), bill.getPromiseRepaymentDate()));
		// 逾期中
		else if(BillStatusEnum.REPAY_OVEDUE.getCode().equals(bill.getBillStatus()))
			vo.setOverdueDays(bill.getOverdueDays());
		vo.setBillStatus(bill.getBillStatus());
		
		vo.setPromiseRepaymentDate(bill.getPromiseRepaymentDate().getTime());
		vo.setWaitRepayAmount(bill.getWaitRepayAmount());
		vo.setRepayPrinciple(bill.getRepayPrinciple());// 本金
		vo.setRepayInterest(bill.getShouldInterest());// 利息
		vo.setManagerFee(bill.getManagerFee());
		vo.setLateFee(bill.getLateFee());
		vo.setPenaltyFee(bill.getPenaltyFee());
		
		// soure 银行卡类型 1 畅捷 2 合利宝
		BankUser bank = userForOrderPayBackServise.queryBankInfoByUserId(bill.getUserId(), "2");
		if(!ChkUtil.isEmpty(bank)){
			vo.setBankAccount(bank.getBankAccount());
			vo.setAccountName(bank.getAccountName());
			vo.setIdNumber(bank.getIdNumber());
			vo.setReservedPhone(bank.getReservedPhone());
		}
		
		return (ResponseData<PayBackQueryVO>) resp.success(vo);
	}
	
	@RequestMapping(value = "/order/payBack", method = RequestMethod.POST, produces = "application/json")
	public ResponseData<?> payBack(@RequestBody PayBackQueryReq req)
			throws Throwable {
		LOGGER.info("payBack:{}", req.getOrderNo());
		BillInfo bill = zeusForOrderPayBackServise.queryBillByOrderNo(req.getOrderNo());
		if(ChkUtil.isEmpty(bill))
			return ResponseData.error("未查询到还款信息");
		
		if(BillStatusEnum.REPAY_SUCCESS.getCode().equals(bill.getBillStatus()))
			return ResponseData.error("订单已结清");
		
		// 交易状态(1:成功,2:失败,3:处理中)
		Long failCount = zeusForOrderPayBackServise.queryBillRepayCount(req.getOrderNo(), DateUtils.format(new Date(), DateUtils.STYLE_2), "2");
		if(failCount >= 2){
			ResponseData<?> resp = ResponseData.error("请明日再试");
			// 前端需要做特殊处理，给特殊值
			resp.setStatus("2");
			return resp;
		}
		
		// soure 银行卡类型 1 畅捷 2 合利宝
		BankUser bank = userForOrderPayBackServise.queryBankInfoByUserId(bill.getUserId(), "2");
		if(ChkUtil.isEmpty(bank))
			return ResponseData.error("银行卡信息缺失");
		
		PaymentVo paymentVo = new PaymentVo();
		paymentVo.setBillNo(bill.getBillNo());
		paymentVo.setBindId(bank.getProtocolNo());
		paymentVo.setBindUserId(bank.getHlbUserId());
		paymentVo.setMobile(bank.getReservedPhone());
		CommonResponse<?> repayResp = zeusForWHServise.activeRepayment(paymentVo);
		LOGGER.info("请求：{}， 返回：{}", JSONObject.toJSONString(paymentVo), JSONObject.toJSONString(repayResp));
		if(ChkUtil.isEmpty(repayResp))
			return ResponseData.error("数据缺失，请联系客服");
		if(repayResp.isSuccess() == false)
			return ResponseData.error(repayResp.getMsg());
		
		return ResponseData.success();
	}
	
	private Integer getOverDay(Date startDate, Date endDay) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			String startStr = sdf.format(startDate);
			String endStr = sdf.format(endDay);
			cal.setTime(sdf.parse(startStr));
			long startTime = cal.getTimeInMillis();
			cal.setTime(sdf.parse(endStr));
			long endTime = cal.getTimeInMillis();
			long between_days = (endTime - startTime) / (1000 * 3600 * 24);
			return Integer.parseInt(String.valueOf(between_days));
		} catch (ParseException e) {
			LOGGER.error("payBackQuery解析天数异常");
			LOGGER.error(e.getMessage(),e);
		}
		return 0;
	}

}
