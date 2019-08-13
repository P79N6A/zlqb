package com.nyd.zeus.service.impls.zzl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.api.zzl.ZeusForPaymentServise;
import com.nyd.zeus.api.zzl.ZeusSqlService;
import com.nyd.zeus.api.zzl.xunlian.XunlianPayService;
import com.nyd.zeus.dao.PaymentRiskFlowDao;
import com.nyd.zeus.dao.PaymentRiskRecordDao;
import com.nyd.zeus.entity.PaymentRiskFlow;
import com.nyd.zeus.model.PaymentRiskRecordPayResult;
import com.nyd.zeus.model.PaymentRiskRecordVo;
import com.nyd.zeus.model.PaymentRiskRequestCommon;
import com.nyd.zeus.model.PaymentRiskRequestXunlian;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.xunlian.XunlianReqPaymentVO;
import com.nyd.zeus.model.xunlian.req.XunlianPaymentVO;
import com.nyd.zeus.model.xunlian.resp.XunlianPayResp;

@Service(value = "zeusForPaymentServise")
@Transactional
public class ZeusForPaymentServiseImpl implements ZeusForPaymentServise {

	private Logger logger = LoggerFactory.getLogger(ZeusForPaymentServiseImpl.class);
	public final static String STYLE_1 = "yyyy-MM-dd HH:mm:ss";
	public final static String STYLE_2 = "yyyy-MM-dd";
	@Autowired
	private ZeusSqlService zeusSqlService;

	@Autowired
	private XunlianPayService xunlianPayService;

	@Autowired
	private PaymentRiskFlowDao paymentRiskFlowDao;

	@Autowired
	private PaymentRiskRecordDao paymentRiskRecordDao;

	private static final String SUCCESS_MSG = "S";
	private static final String FAIL_MSG = "F";
	private static final String PROCESS_MSG = "P";
	
	public static String CHANNEL2 = "xunlian";

	@Override
	public CommonResponse<String> xunlianPay(XunlianReqPaymentVO xunlianReqPaymentVO) {
		CommonResponse<String> common = new CommonResponse<String>();
		logger.info(" 讯联代扣 请求参数为 ： " + JSONObject.toJSONString(xunlianReqPaymentVO));
		//先做白名单查询
		//String queryExclude = " select * from t_payment_risk_exclude_info where status = 1 and order_no'" + xunlianReqPaymentVO.getOrderNo() + "'";
		//List infoList = zeusSqlService.query(queryExclude);
//		ExcludeInfo info = new ExcludeInfo();
//		info.setOrderNo(xunlianReqPaymentVO.getOrderNo());
//		info.setStatus(1);
//		info.setCreateTime(new Date());
//		String insertSql = SqlHelper.getInsertSqlByBean(info);
//		zeusSqlService.insertSql(insertSql);
		String queryRemain = " select * from t_payment_risk_record where  status in (0,2) and order_no = '"
				+ xunlianReqPaymentVO.getOrderNo() + "'";
		List<PaymentRiskRecordVo> list = zeusSqlService.queryT(queryRemain, PaymentRiskRecordVo.class);
		if (CollectionUtils.isEmpty(list)) {
			common.setSuccess(true);
			common.setMsg("该订单不处于代扣服务费节点");
			common.setCode("0");
			return common;
		}
		PaymentRiskRecordVo record = list.get(0);

		BigDecimal remainMoney = record.getRemainMoney();
		// 剩余金额大于0继续扣款
		int r=remainMoney.compareTo(new BigDecimal(0.00));
		if (r <= 0) {
			common.setCode("0");
			common.setMsg(" 剩余金额为0");
			common.setSuccess(true);
			return common;
		}

		try {
			PaymentRiskRequestCommon request = JSONObject.parseObject(record.getRequestText(),
					PaymentRiskRequestCommon.class);
			request.setMoney(remainMoney);

			record.setRequestTime(new SimpleDateFormat(STYLE_1).format(new Date()));
			record.setRequestText(JSONObject.toJSONString(request));

			PaymentRiskRecordPayResult result = pay(request, remainMoney);

			// 开始支付
			record.setResponseText(JSONObject.toJSONString(result));
			record.setSeriNo(result.getSeriNo());
			// 确认结果
			String resultStatus = result.getStatus();

			// 成功
			if (SUCCESS_MSG.equals(resultStatus)) {
				record.setStatus(1);
				common.setCode("1");
				// 如果扣款成功了， 则需要 减去剩余金额， 因为本次是第一次 所以直接清空0
				record.setRemainMoney(new BigDecimal(0d));
				common.setMsg("扣款成功!");
			}
			if (FAIL_MSG.equals(resultStatus)) {
				record.setStatus(0);
				common.setCode("0");
				common.setMsg("扣款失败!");
			}
			if (PROCESS_MSG.equals(resultStatus)) {
				record.setStatus(-1);
				// 设置备份金额
				common.setCode("-1");
				record.setBackupMoney(record.getRemainMoney());
				common.setMsg("交易处理中!");

			}
			record.setUpdateTime(new SimpleDateFormat(STYLE_1).format(new Date()));
			paymentRiskRecordDao.update(record);
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" 讯联主动扣款异常：" + e.getMessage(),e);
			common.setMsg("操作异常!");
			common.setSuccess(false);
		}
		
		return common;

	}

	private PaymentRiskFlow beforeSaveFlow(String requestText) {
		PaymentRiskFlow flow = new PaymentRiskFlow();
		flow.setRequestText(requestText);
		flow.setRequestTime(new Date());
		flow.setStatus("F");
		return flow;
	}

	private PaymentRiskRecordPayResult pay(PaymentRiskRequestCommon request, BigDecimal thisMoney) {
		// 获取还款的渠道code
		String channel = request.getChannelCode();
		PaymentRiskRecordPayResult result = new PaymentRiskRecordPayResult();
		// 请求时间开始
		result.setRequestTime(new Date());
		result.setMoney(thisMoney);
		// 默认直接失败
		result.setStatus("F");
		result.setSeriNo("");
		// 2 个支付渠道
		
		if (channel.equals(CHANNEL2)) {
			try {
				// 转到channel2 的请求类
				PaymentRiskRequestXunlian xunlian = JSONObject.parseObject(request.getChannelJson(),
						PaymentRiskRequestXunlian.class);
				XunlianPaymentVO xunlianPaymentVO = new XunlianPaymentVO();
				xunlianPaymentVO.setAccount(xunlian.getAccount());
				xunlianPaymentVO.setAmount(new DecimalFormat("#.##").format(thisMoney));
				xunlianPaymentVO.setName(xunlian.getName());
				xunlianPaymentVO.setProtocolId(xunlian.getProtocolId());
				PaymentRiskFlow flow = beforeSaveFlow(JSONObject.toJSONString(xunlianPaymentVO));
				logger.info("------------PaymentRiskRecordServiceImpl-----pay-----xunlian,参数:{}",
						JSONObject.toJSONString(xunlianPaymentVO));
				CommonResponse<XunlianPayResp> xlResult = xunlianPayService.pay(xunlianPaymentVO);
				logger.info("------------PaymentRiskRecordServiceImpl-----pay-----xunlian,结果:{}",
						JSONObject.toJSONString(xlResult));
				// 响应参数
				result.setResponseText(JSONObject.toJSONString(xlResult));
				// 确认结果 流水
				XunlianPayResp xunlianResp = xlResult.getData();
				String resultStatus = xunlianResp.getRetFlag();
				String seriNo = xunlianResp.getPayOrderId();
				result.setSeriNo(seriNo);
				// 畅杰的三个状态值
				String CJ_SUCCESS_MSG = "T";
				String CJ_FAIL_MSG = "F";
				String CJ_PROCESS_MSG = "P";
				if (resultStatus.equals(CJ_SUCCESS_MSG)) {
					result.setStatus(SUCCESS_MSG);
				}
				if (resultStatus.equals(CJ_FAIL_MSG)) {
					result.setStatus(FAIL_MSG);
				}
				if (resultStatus.equals(CJ_PROCESS_MSG)) {
					result.setStatus(PROCESS_MSG);
				}
				// flow
				flow.setResponseText(JSONObject.toJSONString(result.getResponseText()));
				flow.setSeriNo(seriNo);
				flow.setStatus(result.getStatus());
				flow.setMoney(thisMoney);
				flow.setOrderNo(request.getOrderNo());
				afterSaveFlow(flow);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		}
		// 响应时间
		result.setResponseTime(new Date());
		return result;
	}

	private void afterSaveFlow(PaymentRiskFlow flow) {
		try {
			if (flow.getMoney() == null) {
				flow.setMoney(new BigDecimal(0));
			}
			flow.setResponseTime(new Date());
			paymentRiskFlowDao.save(flow);
			// if(SUCCESS_MSG.equals(flow.getStatus())) {
			// try {
			// // 开始发送短信
			// String sql ="SELECT * from t_payment_risk_record_extend where
			// order_no='"+flow.getOrderNo()+"' ";
			// JSONObject configJson = zeusSqlService.queryOne(sql);
			// if(configJson == null || configJson.isEmpty()) {
			// return;
			// }
			// // 短信数据
			// BigDecimal prince = (BigDecimal)
			// orderNosMap.get(flow.getOrderNo()+"_prince");
			// BigDecimal sumAmount = (BigDecimal)
			// orderNosMap.get(flow.getOrderNo()+"_sumAmount");
			// sumAmount = sumAmount.add(flow.getMoney());
			// orderNosMap.put(flow.getOrderNo()+"_sumAmount",sumAmount);
			//
			// // 判断是否合理
			// if(sumAmount.doubleValue()>prince.doubleValue()) {
			// sumAmount = prince;
			// }
			//
			// SmsRequest vo = new SmsRequest();
			// vo.setAppName(configJson.getString("app_name"));
			// vo.setCellphone(configJson.getString("cell_phone"));
			// vo.setSmsType(Integer.parseInt(configJson.getString("sms_type")));
			// Map<String,Object> map = new HashMap<String,Object>();
			// map.put("custName",configJson.getString("cust_name"));
			// map.put("prince",new DecimalFormat("##.##").format(prince));
			// map.put("amount",new
			// DecimalFormat("##.##").format(flow.getMoney()));
			// map.put("sumAmount",new
			// DecimalFormat("##.##").format(sumAmount));
			// map.put("phone", configJson.getString("phone"));
			// vo.setMap(map);
			// logger.info("-===================--请求afterSaveFlow--sendSingleSms-
			// 参数："+JSONObject.toJSONString(vo));
			// com.tasfe.framework.support.model.ResponseData smsData =
			// iSendSmsService.sendSingleSms(vo);
			// logger.info("---请求afterSaveFlow--sendSingleSms-
			// 结果："+JSONObject.toJSONString(smsData));
			// } catch (Exception e) {
			// logger.error("发送短信异常");
			// logger.error(e.getMessage(),e);
			// }
			// }

		} catch (Exception e) {
			logger.error("保存主动扣款流水异常!");
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public CommonResponse<String> judgeStatus(XunlianReqPaymentVO xunlianReqPaymentVO) {
		CommonResponse<String> common = new CommonResponse<String>();
		logger.info(" 讯联判断是否有代扣权限 请求参数为 ： " + JSONObject.toJSONString(xunlianReqPaymentVO));
		String queryRemain = " select * from t_payment_risk_record where status in (0,2) and order_no = '"
				+ xunlianReqPaymentVO.getOrderNo() + "'";
		List<PaymentRiskRecordVo> list = zeusSqlService.queryT(queryRemain, PaymentRiskRecordVo.class);
		if (CollectionUtils.isEmpty(list)) {
			common.setSuccess(true);
			common.setMsg(" 未找到订单");
			common.setCode("0");
			return common;
		}
		PaymentRiskRecordVo record = list.get(0);
		BigDecimal remainMoney = record.getRemainMoney();
		// 剩余金额大于0继续扣款
		int r=remainMoney.compareTo(new BigDecimal(0.00));
		if (r <= 0) {
			common.setMsg(" 剩余金额为0");
			common.setCode("0");
			common.setSuccess(true);
			return common;
		}


		PaymentRiskRequestCommon request = JSONObject.parseObject(record.getRequestText(),
				PaymentRiskRequestCommon.class);
		String channel = request.getChannelCode();
		if(CHANNEL2.equals(channel)){	
			common.setSuccess(true);
			common.setCode("1");
			return common;
		}
		common.setMsg("非讯联不处理");
		common.setSuccess(true);
		common.setCode("0");
		return common;
	}

}
