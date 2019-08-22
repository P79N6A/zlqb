package com.nyd.zeus.service.impls;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;
import com.nyd.zeus.api.payment.PaymentRiskRecordService;
import com.nyd.zeus.api.zzl.ZeusSqlService;
import com.nyd.zeus.api.zzl.chanpay.ChangJiePaymentService;
import com.nyd.zeus.api.zzl.hnapay.HnaPayPaymentService;
import com.nyd.zeus.api.zzl.liandong.LiandongPayPaymentService;
import com.nyd.zeus.api.zzl.xunlian.XunlianPayService;
import com.nyd.zeus.dao.PaymentRiskFlowDao;
import com.nyd.zeus.dao.PaymentRiskRecordDao;
import com.nyd.zeus.dao.PaymentRiskRecordExtendDao;
import com.nyd.zeus.entity.PaymentRiskFlow;
import com.nyd.zeus.model.PaymentRiskFlowVo;
import com.nyd.zeus.model.PaymentRiskRecordExtendVo;
import com.nyd.zeus.model.PaymentRiskRecordPayRequest;
import com.nyd.zeus.model.PaymentRiskRecordPayResult;
import com.nyd.zeus.model.PaymentRiskRecordQueryRequest;
import com.nyd.zeus.model.PaymentRiskRecordVo;
import com.nyd.zeus.model.PaymentRiskRequestChangjie;
import com.nyd.zeus.model.PaymentRiskRequestCommon;
import com.nyd.zeus.model.PaymentRiskRequestLiandong;
import com.nyd.zeus.model.PaymentRiskRequestXinsheng;
import com.nyd.zeus.model.PaymentRiskRequestXunlian;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.helibao.util.chanpay.ChkUtil;
import com.nyd.zeus.model.helibao.util.chanpay.DateUtils;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJiePrePayVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangjieQueryPayVO;
import com.nyd.zeus.model.helibao.vo.pay.resp.chanpay.ChangJiePayCommonResp;
import com.nyd.zeus.model.hnapay.req.HnaPayPreTransReq;
import com.nyd.zeus.model.hnapay.req.HnaPayQueryTransReq;
import com.nyd.zeus.model.hnapay.resp.HnaPayQueryTransResp;
import com.nyd.zeus.model.hnapay.resp.HnaPayTransResp;
import com.nyd.zeus.model.liandong.vo.LiandongPaymentVO;
import com.nyd.zeus.model.liandong.vo.LiandongQueryPaymentVO;
import com.nyd.zeus.model.liandong.vo.resp.LiandongPaymentResp;
import com.nyd.zeus.model.xunlian.req.XunlianPaymentVO;
import com.nyd.zeus.model.xunlian.req.XunlianQueryPayVO;
import com.nyd.zeus.model.xunlian.resp.XunlianPayResp;
import com.nyd.zeus.model.xunlian.resp.XunlianQueryPayResp;
import com.site.lookup.util.StringUtils;

@Service(value = "zeusForRiskPaymentServise")
@Transactional
public class PaymentRiskRecordServiceImpl implements PaymentRiskRecordService {
	private Logger logger = LoggerFactory
			.getLogger(PaymentRiskRecordServiceImpl.class);
	public final static String STYLE_1 = "yyyy-MM-dd HH:mm:ss";
	public final static String STYLE_2 = "yyyy-MM-dd";

	@Autowired
	private PaymentRiskRecordDao paymentRiskRecordDao;

	@Autowired
	private PaymentRiskFlowDao paymentRiskFlowDao;

	@Autowired
	private PaymentRiskRecordExtendDao paymentRiskRecordExtendDao;

	@Autowired
	private ISendSmsService iSendSmsService;

	@Autowired
	private ZeusSqlService zeusSqlService;

	@Autowired
	private ChangJiePaymentService changJiePaymentService;

	@Autowired
	private XunlianPayService xunlianPayService;

	@Autowired
	private HnaPayPaymentService hnaPayPaymentService;
	
	@Autowired
	private LiandongPayPaymentService liandongPayPaymentService;
	

	private static final String SUCCESS_MSG = "S";
	private static final String FAIL_MSG = "F";
	private static final String PROCESS_MSG = "P";
	
	// 2 个支付渠道
	String channel1 = "changjie";
	String channel2 = "xunlian";
	String channel3 = "xinsheng";
	String channel4 = "liandong";

	private Map<String, Object> orderNosMap = new HashMap();
	
	// 新生支付将返回的交易时间保存到req中
	private void saveSubmitTime4Xinsheng(PaymentRiskRecordVo paymentRiskRecord, PaymentRiskRecordPayResult result){
		JSONObject recordReq = JSONObject.parseObject(paymentRiskRecord.getRequestText());
		if(String.valueOf(recordReq.get("channelCode")).equals(channel3)){
			JSONObject req = recordReq.getJSONObject("channelJson");
			JSONObject resp = JSONObject.parseObject(result.getResponseText());
			req.put("submitTime", resp.get("submitTime"));
			recordReq.put("channelJson", req);
			paymentRiskRecord.setRequestText(JSONObject.toJSONString(recordReq));
		}
	}

	@Override
	public CommonResponse activeRepayment(PaymentRiskRequestCommon vo) {
		logger.info("风控调用还款主动还款请求参数:" + JSONObject.toJSONString(vo));
		// 开始的基础值赋值
		CommonResponse common = new CommonResponse();
		try {
			// 准备数据记录
			vo.setMoney(vo.getMoney().divide(new BigDecimal(1), 2,
					BigDecimal.ROUND_HALF_UP));
			PaymentRiskRecordVo paymentRiskRecord = new PaymentRiskRecordVo();
			paymentRiskRecord.setRequestTime(new SimpleDateFormat(STYLE_1)
					.format(new Date()));
			paymentRiskRecord.setOrderNo(vo.getOrderNo());
			paymentRiskRecord.setRemainMoney(vo.getMoney());
			paymentRiskRecord.setShouldMoney(vo.getMoney());
			paymentRiskRecord.setRequestText(JSONObject.toJSONString(vo));
			paymentRiskRecord.setStatus(0); // 设置默认
			paymentRiskRecord.setRiskStatus(-1);
			paymentRiskRecord.setRiskTime(new SimpleDateFormat(STYLE_1)
					.format(vo.getRiskTime()));
			paymentRiskRecord.setBackupMoney(new BigDecimal(0d));
			paymentRiskRecord.setNoticeStatus(0);
			// 赋值短信相关
			orderNosMap.put(vo.getOrderNo() + "_sumAmount", new BigDecimal(0d));
			orderNosMap.put(vo.getOrderNo() + "_prince", vo.getMoney());

			// 统一支付入口 开始 ↓↓↓↓↓

			PaymentRiskRecordPayResult result = pay(vo, vo.getMoney());
			// 统一支付入口 结果 ↑↑↑↑↑

			// 开始支付
			saveSubmitTime4Xinsheng(paymentRiskRecord, result);
			paymentRiskRecord.setResponseText(JSONObject.toJSONString(result));
			paymentRiskRecord.setSeriNo(result.getSeriNo());
			// 确认结果
			String resultStatus = result.getStatus();

			// 成功
			if (SUCCESS_MSG.equals(resultStatus)) {
				paymentRiskRecord.setStatus(1);
				// 如果扣款成功了， 则需要 减去剩余金额， 因为本次是第一次 所以直接清空0
				paymentRiskRecord.setRemainMoney(new BigDecimal(0d));
			}
			if (FAIL_MSG.equals(resultStatus)) {
				paymentRiskRecord.setStatus(0);
			}
			if (PROCESS_MSG.equals(resultStatus)) {
				paymentRiskRecord.setStatus(-1);
				// 设置备份金额
				paymentRiskRecord.setBackupMoney(paymentRiskRecord
						.getRemainMoney());

			}

			// 如果失败的话，则需要继续连续扣款处理
			if (paymentRiskRecord.getStatus() == 0) {
				nextPay(paymentRiskRecord, vo);
			}

			paymentRiskRecord.setUpdateTime(new SimpleDateFormat(STYLE_1)
					.format(new Date()));
			// 以下字段仅为异步处理使用，用于处理仅会执行一次交易
			paymentRiskRecord.setRecentMoney(vo.getMoney());
			paymentRiskRecord.setFailNum(0);
			paymentRiskRecord.setDealTime(new SimpleDateFormat(STYLE_1)
					.format(new Date()));
			paymentRiskRecordDao.save(paymentRiskRecord);

			// 流水后期记录>> 暂定
			common.setSuccess(true);
			common.setData(JSONObject.toJSONString(result));
			common.setMsg("操作结束!");
		} catch (Exception e) {
			logger.error("风控调用还款主动还款请求异常:" + JSONObject.toJSONString(vo));
			logger.error(e.getMessage(), e);
			common.setSuccess(false);
			common.setMsg("操作失败!");
		}
		return common;
	}

	@Override
	public CommonResponse batchRepayTask(PaymentRiskRecordVo paymentRiskRecord) {
		//
		CommonResponse common = new CommonResponse();
		if (needRemove(paymentRiskRecord.getOrderNo())) {
			return common;
		}
		try {
			// JSONObject vo =
			// JSONObject.parseObject(paymentRiskRecord.getRequestText());
			// 开始请求规则的判断 //
			Long days = getDays(
					new SimpleDateFormat(STYLE_1).parse(paymentRiskRecord
							.getRiskTime()), new Date());
			if (days > 3 || days <= 0) {
				return common;
			}
			// 总扣款金额
			BigDecimal paidMoney = new BigDecimal(0d);
			BigDecimal initMoney = paymentRiskRecord.getRemainMoney();
			BigDecimal remainMoney = paymentRiskRecord.getRemainMoney();
			BigDecimal backupMoney = new BigDecimal(0d);
			BigDecimal recentMoney = BigDecimal.ZERO;

			// 请求扣款
			// 本次请求是否异常
			boolean isError = false;
			boolean isProcess = false;
			boolean isSuccess = true; // 成功后就一直扣款

			// 赋值短信相关
			BigDecimal sumAmount = paymentRiskRecord.getShouldMoney().subtract(
					paymentRiskRecord.getRemainMoney());
			BigDecimal prince = paymentRiskRecord.getShouldMoney();
			orderNosMap.put(paymentRiskRecord.getOrderNo() + "_sumAmount",
					sumAmount);
			orderNosMap.put(paymentRiskRecord.getOrderNo() + "_prince", prince);

			BigDecimal twoBigdeBigDecimal = paymentRiskRecord.getShouldMoney()
					.divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);
			BigDecimal threeBigdeBigDecimal = paymentRiskRecord
					.getShouldMoney().divide(new BigDecimal(3), 2,
							BigDecimal.ROUND_HALF_UP);
			BigDecimal sixBigDecimal = paymentRiskRecord.getShouldMoney()
					.divide(new BigDecimal(6), 2, BigDecimal.ROUND_HALF_UP);
			int breakKey = 0; // 超过四次直接跳出
			while (isSuccess) {
				if (breakKey > 5)
					break;
				BigDecimal thisMoney = threeBigdeBigDecimal;
				breakKey++;
				// 不符合数据
				if (thisMoney.doubleValue() > remainMoney.doubleValue()
						|| thisMoney.doubleValue() <= 0
						|| remainMoney.doubleValue() <= 0) {
					isSuccess = false;
					break;
				}

				// 统一支付入口 开始 ↓↓↓↓↓
				PaymentRiskRequestCommon request = JSONObject.parseObject(
						paymentRiskRecord.getRequestText(),
						PaymentRiskRequestCommon.class);
				// 有可能是老数据
				if (ChkUtil.isEmpty(request.getChannelCode())) {
					PaymentRiskRequestChangjie cj = JSONObject.parseObject(
							paymentRiskRecord.getRequestText(),
							PaymentRiskRequestChangjie.class);
					request.setChannelCode("changjie");
					request.setChannelJson(JSONObject.toJSONString(cj));
				}
				recentMoney = thisMoney;
				PaymentRiskRecordPayResult result = pay(request, thisMoney);
				String resultStatus = result.getStatus();
				// 统一支付入口 结果 ↑↑↑↑↑
				saveSubmitTime4Xinsheng(paymentRiskRecord, result);
				paymentRiskRecord.setResponseText(JSONObject
						.toJSONString(result));
				paymentRiskRecord.setSeriNo(result.getSeriNo());
				// 确认结果
				// 成功
				if (SUCCESS_MSG.equals(resultStatus)) {
					paidMoney = paidMoney.add(thisMoney);
					remainMoney = remainMoney.subtract(thisMoney);
				}
				if (FAIL_MSG.equals(resultStatus)) {
					isSuccess = false;
				}
				if (PROCESS_MSG.equals(resultStatus)) {
					isSuccess = false;
					isProcess = true;
					backupMoney = backupMoney.add(thisMoney);
				}
			}

			// 失败数据
			if (!isSuccess && !isProcess) {
				// 走一次失败数据
				BigDecimal thisMoney = sixBigDecimal;

				// 不符合数据
				if (thisMoney.doubleValue() <= remainMoney.doubleValue()
						&& thisMoney.doubleValue() > 0
						&& remainMoney.doubleValue() > 0) {

					// 统一支付入口 开始 ↓↓↓↓↓
					PaymentRiskRequestCommon request = JSONObject.parseObject(
							paymentRiskRecord.getRequestText(),
							PaymentRiskRequestCommon.class);
					if (ChkUtil.isEmpty(request.getChannelCode())) {
						PaymentRiskRequestChangjie cj = JSONObject.parseObject(
								paymentRiskRecord.getRequestText(),
								PaymentRiskRequestChangjie.class);
						request.setChannelCode("changjie");
						request.setChannelJson(JSONObject.toJSONString(cj));
					}
					recentMoney = thisMoney;
					PaymentRiskRecordPayResult result = pay(request, thisMoney);
					String resultStatus = result.getStatus();
					// 统一支付入口 结果 ↑↑↑↑↑

					paymentRiskRecord.setResponseText(JSONObject
							.toJSONString(result));
					paymentRiskRecord.setSeriNo(result.getSeriNo());
					// 确认结果

					// 成功
					if (SUCCESS_MSG.equals(resultStatus)) {
						paidMoney = paidMoney.add(thisMoney);
						remainMoney = remainMoney.subtract(thisMoney);
					}
					if (FAIL_MSG.equals(resultStatus)) {
						isSuccess = false;
					}
					if (PROCESS_MSG.equals(resultStatus)) {
						isSuccess = false;
						isProcess = true;
						backupMoney = backupMoney.add(thisMoney);
					}
				}
			}

			paymentRiskRecord.setRemainMoney(paymentRiskRecord.getRemainMoney()
					.subtract(paidMoney));
			if (isProcess) {
				paymentRiskRecord.setStatus(-1);
				paymentRiskRecord.setBackupMoney(backupMoney);
			}
			if (paymentRiskRecord.getRemainMoney().doubleValue() == 0) {
				paymentRiskRecord.setStatus(1);
			}
			paymentRiskRecord.setUpdateTime(new SimpleDateFormat(STYLE_1)
					.format(new Date()));
			// 以下字段仅为异步处理使用，用于处理仅会执行一次交易
			paymentRiskRecord.setRecentMoney(recentMoney);
			paymentRiskRecord.setFailNum(0);
			paymentRiskRecord.setDealTime(new SimpleDateFormat(STYLE_1)
					.format(new Date()));
			paymentRiskRecordDao.update(paymentRiskRecord);

		} catch (Exception e) {
			logger.error("风控调用还款处理异常："
					+ JSONObject.toJSONString(paymentRiskRecord));
			logger.error(e.getMessage(), e);
		}
		// 流水后期记录>> 暂定
		common.setSuccess(true);
		common.setMsg("操作结束!");

		return common;
	}

	private void nextPay(PaymentRiskRecordVo paymentRiskRecord,
			PaymentRiskRequestCommon vo) {
		// 总扣款金额
		BigDecimal paidMoney = new BigDecimal(0d);
		BigDecimal remainMoney = paymentRiskRecord.getRemainMoney();
		BigDecimal backupMoney = new BigDecimal(0d);

		// 请求扣款
		// 本次请求是否异常
		boolean isError = false;
		boolean isProcess = false;
		boolean isSuccess = true; // 成功后就一直扣款

		BigDecimal twoBigdeBigDecimal = paymentRiskRecord.getShouldMoney()
				.divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal threeBigdeBigDecimal = paymentRiskRecord.getShouldMoney()
				.divide(new BigDecimal(3), 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal sixBigDecimal = paymentRiskRecord.getShouldMoney().divide(
				new BigDecimal(6), 2, BigDecimal.ROUND_HALF_UP);
		int breakKey = 0; // 超过四次直接跳出
		while (isSuccess) {
			if (breakKey > 5)
				break;
			BigDecimal thisMoney = threeBigdeBigDecimal;
			breakKey++;
			// 不符合数据
			if (thisMoney.doubleValue() > remainMoney.doubleValue()
					|| thisMoney.doubleValue() <= 0) {
				isSuccess = false;
				break;
			}

			// 统一支付入口 开始 ↓↓↓↓↓
			PaymentRiskRequestCommon request = JSONObject.parseObject(
					paymentRiskRecord.getRequestText(),
					PaymentRiskRequestCommon.class);
			PaymentRiskRecordPayResult result = pay(request, thisMoney);
			String resultStatus = result.getStatus();
			// 统一支付入口 结果 ↑↑↑↑↑
			saveSubmitTime4Xinsheng(paymentRiskRecord, result);
			paymentRiskRecord.setResponseText(JSONObject.toJSONString(result));
			paymentRiskRecord.setSeriNo(result.getSeriNo());

			// 确认结果
			// 成功
			if (SUCCESS_MSG.equals(resultStatus)) {
				paidMoney = paidMoney.add(thisMoney);
				remainMoney = remainMoney.subtract(thisMoney);
			}
			if (FAIL_MSG.equals(resultStatus)) {
				isSuccess = false;
			}
			if (PROCESS_MSG.equals(resultStatus)) {
				isSuccess = false;
				isProcess = true;
				backupMoney = backupMoney.add(thisMoney);
			}
		}

		// 失败数据
		if (!isSuccess && !isProcess) {
			// 走一次失败数据
			BigDecimal thisMoney = sixBigDecimal;

			// 不符合数据
			if (thisMoney.doubleValue() <= remainMoney.doubleValue()
					&& thisMoney.doubleValue() > 0) {

				// 统一支付入口 开始 ↓↓↓↓↓
				PaymentRiskRequestCommon request = JSONObject.parseObject(
						paymentRiskRecord.getRequestText(),
						PaymentRiskRequestCommon.class);
				PaymentRiskRecordPayResult result = pay(request, thisMoney);
				String resultStatus = result.getStatus();
				// 统一支付入口 结果 ↑↑↑↑↑

				paymentRiskRecord.setResponseText(JSONObject
						.toJSONString(result));
				paymentRiskRecord.setSeriNo(result.getSeriNo());
				// 确认结果

				// 成功
				if (SUCCESS_MSG.equals(resultStatus)) {
					paidMoney = paidMoney.add(thisMoney);
					remainMoney = remainMoney.subtract(thisMoney);
				}
				if (FAIL_MSG.equals(resultStatus)) {
					isSuccess = false;
				}
				if (PROCESS_MSG.equals(resultStatus)) {
					isSuccess = false;
					isProcess = true;
					backupMoney = backupMoney.add(thisMoney);
				}
			}
		}

		paymentRiskRecord.setRemainMoney(paymentRiskRecord.getRemainMoney()
				.subtract(paidMoney));
		if (isProcess) {
			paymentRiskRecord.setStatus(-1);
			paymentRiskRecord.setBackupMoney(backupMoney);
		}
		if (paymentRiskRecord.getRemainMoney().doubleValue() == 0) {
			paymentRiskRecord.setStatus(1);
		}
		paymentRiskRecord.setUpdateTime(new SimpleDateFormat(STYLE_1)
				.format(new Date()));
	}

	@Override
	public CommonResponse batchQueryRepayTask(
			PaymentRiskRecordVo paymentRiskRecord) {
		logger.info("风控调用查询还款还款开始");
		CommonResponse common = new CommonResponse();
		try {
			// 短信数据
			// 赋值短信相关
			BigDecimal sumAmount = paymentRiskRecord.getShouldMoney().subtract(
					paymentRiskRecord.getRemainMoney());
			BigDecimal prince = paymentRiskRecord.getShouldMoney();
			orderNosMap.put(paymentRiskRecord.getOrderNo() + "_sumAmount",
					sumAmount);
			orderNosMap.put(paymentRiskRecord.getOrderNo() + "_prince", prince);

			// 开始请求规则的判断 //
			// 请求扣款
			// 开始操作查询 统一入口
			// 请求 ↓↓↓↓↓
			PaymentRiskRecordPayRequest payRequest = JSONObject.parseObject(
					paymentRiskRecord.getRequestText(),
					PaymentRiskRecordPayRequest.class);
			PaymentRiskRecordQueryRequest request = new PaymentRiskRecordQueryRequest();
			request.setChannelCode(payRequest.getChannelCode());
			if (ChkUtil.isEmpty(payRequest.getChannelCode())) {
				request.setChannelCode("changjie");
			}
			//联动查询用
			JSONObject reqJson = JSONObject.parseObject(paymentRiskRecord.getRequestText());
			String channel = String.valueOf(reqJson.get("channelCode"));
			JSONObject ldChannelJson = JSONObject.parseObject(String.valueOf(reqJson.get("channelJson")));
			String merDate = null;
			if (channel.equals(channel3)) {
				String temp = String.valueOf(ldChannelJson.get("submitTime"));
				if (temp.length() >= 8)
					merDate = temp.substring(0, 8);
				logger.info(" 新生获取查询时间 json:[{}] ,submitTime:[{}]",
						ldChannelJson, merDate);
			} else if (channel.equals(channel4)) {
				logger.info(" 联动查询用 merDate:" + ldChannelJson);
				merDate = String.valueOf(ldChannelJson.get("mer_date"));
			}
			if(StringUtils.isNotEmpty(merDate)){
				request.setMer_date(merDate);
			}
			request.setSeriNo(paymentRiskRecord.getSeriNo());
			request.setOrderNo(paymentRiskRecord.getOrderNo());
			PaymentRiskRecordPayResult result = query(request,
					paymentRiskRecord);
			String resultStatus = result.getStatus();
			// 结果 ↑↑↑↑↑

			paymentRiskRecord.setResponseText(JSONObject.toJSONString(result));
			paymentRiskRecord.setSeriNo(paymentRiskRecord.getSeriNo());

			// 确认结果
			// 成功
			if (SUCCESS_MSG.equals(resultStatus)) {
				// 如果扣款成功了， 则需要 减去剩余金额， 因为本次是第一次 所以直接清空0
				paymentRiskRecord.setRemainMoney(paymentRiskRecord
						.getRemainMoney().subtract(
								paymentRiskRecord.getBackupMoney()));
				if (paymentRiskRecord.getRemainMoney().doubleValue() == 0) {
					paymentRiskRecord.setStatus(1);
				} else {
					paymentRiskRecord.setStatus(0);
				}
				paymentRiskRecord.setBackupMoney(new BigDecimal(0));
			}
			if (FAIL_MSG.equals(resultStatus)) {
				paymentRiskRecord.setStatus(0);
				paymentRiskRecord.setBackupMoney(new BigDecimal(0));
				// 异步处理使用，主要用于实时不返回的交易，为兼容线上逻辑，线上已经有的数据失败次数均为-1不进行处理
				if (ChkUtil.isNotEmpty(paymentRiskRecord.getFailNum())
						&& paymentRiskRecord.getFailNum() >= 0)
					paymentRiskRecord
							.setFailNum(paymentRiskRecord.getFailNum() + 1);
			}
			if (PROCESS_MSG.equals(resultStatus)) {
				paymentRiskRecord.setStatus(-1);
			}
			paymentRiskRecord.setUpdateTime(new SimpleDateFormat(STYLE_1)
					.format(new Date()));
			paymentRiskRecordDao.update(paymentRiskRecord);
			logger.info("追加扣款... resultStatus：{} ，failNum：{}", resultStatus,
					paymentRiskRecord.getFailNum());
			if (!PROCESS_MSG.equals(resultStatus)
					&& paymentRiskRecord.getFailNum() >= 0) {// 仅处理非成功和失败，线上已经有的数据失败次数均为-1不进行处理
				if ("xinsheng".equals(payRequest.getChannelCode())) {
					xinshengContinuePay(paymentRiskRecord, resultStatus);
				}
			}

		} catch (Exception e) {
			logger.error("风控调用还款处理异常");
			logger.error(e.getMessage(), e);
		}

		return common;
	}

	// TODO 此处添加新生，参考batchRepayTask方法，根据失败次数和上次扣款次数进行扣款
	// 当天主动触发后
	// 前置批处理：（第一次失败0次 扣款198 如果失败更新为1次）
	// 第二次失败1次 recentMoney198 扣款066 
	// 第三次失败2次 recentMoney066 扣款033 不论成功失败都停止
	// 次日批处理触发后
	// 前置批处理：（第一次失败0次 扣款066 如果失败更新为1次 并扣款33）
	// 第二次失败1次 recentMoney066 扣款033 不论成功失败都停止
	// 第三次失败2次 recentMoney033 停止
	public void xinshengContinuePay(PaymentRiskRecordVo paymentRiskRecord,
			String resultStatus) {
		logger.info("xinshengContinuePay start... {}", JSONObject.toJSONString(paymentRiskRecord));
		if (needRemove(paymentRiskRecord.getOrderNo()))
			return;
		try {
			// 仅对当天的数据进行追加扣款
			if (!String.valueOf(paymentRiskRecord.getDealTime()).startsWith(
					new SimpleDateFormat(STYLE_2).format(new Date())))
				return;

			// 总扣款金额
			BigDecimal remainMoney = paymentRiskRecord.getRemainMoney();
			BigDecimal backupMoney = new BigDecimal(0d);

			// 请求扣款
			// 本次请求是否异常
			// 赋值短信相关
			BigDecimal sumAmount = paymentRiskRecord.getShouldMoney().subtract(
					paymentRiskRecord.getRemainMoney());
			BigDecimal prince = paymentRiskRecord.getShouldMoney();
			orderNosMap.put(paymentRiskRecord.getOrderNo() + "_sumAmount",
					sumAmount);
			orderNosMap.put(paymentRiskRecord.getOrderNo() + "_prince", prince);

			BigDecimal thisMoney = BigDecimal.ZERO;
			BigDecimal secondMoney = paymentRiskRecord.getShouldMoney().divide(
					new BigDecimal(3), 2, BigDecimal.ROUND_HALF_UP);
			BigDecimal thirdMoney = paymentRiskRecord.getShouldMoney().divide(
					new BigDecimal(6), 2, BigDecimal.ROUND_HALF_UP);
			
			// 最小金额不论成功与否都停止
			if (paymentRiskRecord.getRecentMoney().compareTo(thirdMoney) == 0) {
				return;
			}
			// 优化逻辑，成功继续扣原金额，失败扣下一档位，由超扣逻辑跳出
			if (SUCCESS_MSG.equals(resultStatus)) {
				logger.info("xinshengContinuePay last success");
				// 最大金额成功停止
				if (paymentRiskRecord.getRecentMoney().compareTo(
						paymentRiskRecord.getShouldMoney()) == 0)
					return;
				else
					thisMoney = paymentRiskRecord.getRecentMoney();
			} else if (FAIL_MSG.equals(resultStatus)) {
				logger.info("xinshengContinuePay last fail");
				// 失败则扣下一个档位的钱
				if (paymentRiskRecord.getRecentMoney().compareTo(
						paymentRiskRecord.getShouldMoney()) == 0)
					thisMoney = secondMoney;
				else if (paymentRiskRecord.getRecentMoney().compareTo(
						secondMoney) == 0)
					thisMoney = thirdMoney;
				else
					return;
			} else {
				return;
			}
			// 特殊情况扣款金额大于剩余金额，则将剩余金额全部扣掉
			if (thisMoney.compareTo(remainMoney) == 1
					&& remainMoney.compareTo(BigDecimal.ZERO) == 1) {
				thisMoney = remainMoney;
			}
			logger.info("xinshengContinuePay thisMoney: {}", thisMoney);
			// 超扣统一控制逻辑
			if (thisMoney.compareTo(remainMoney) == 1
					|| thisMoney.compareTo(BigDecimal.ZERO) == -1
					|| remainMoney.compareTo(BigDecimal.ZERO) == -1)
				return;
			
			// 统一支付入口 开始 ↓↓↓↓↓
			PaymentRiskRequestCommon request = JSONObject.parseObject(
					paymentRiskRecord.getRequestText(),
					PaymentRiskRequestCommon.class);
			PaymentRiskRecordPayResult result = pay(request, thisMoney);
			// String resultStatus = result.getStatus();
			// 统一支付入口 结果 ↑↑↑↑↑
			saveSubmitTime4Xinsheng(paymentRiskRecord, result);
			paymentRiskRecord.setResponseText(JSONObject.toJSONString(result));
			paymentRiskRecord.setSeriNo(result.getSeriNo());

			// 结果只有处理中
			backupMoney = backupMoney.add(thisMoney);
			paymentRiskRecord.setStatus(-1);
			paymentRiskRecord.setBackupMoney(backupMoney);

			paymentRiskRecord.setUpdateTime(new SimpleDateFormat(STYLE_1)
					.format(new Date()));
			// 以下字段仅为异步处理使用，用于处理仅会执行一次交易
			paymentRiskRecord.setRecentMoney(thisMoney);
			paymentRiskRecord.setDealTime(new SimpleDateFormat(STYLE_1)
					.format(new Date()));
			paymentRiskRecordDao.update(paymentRiskRecord);
		} catch (Exception e) {
			logger.error("风控调用还款处理异常："
					+ JSONObject.toJSONString(paymentRiskRecord));
			logger.error(e.getMessage(), e);
		}
	}

	private static Long getDays(Date time1, Date time2) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			String startStr = sdf.format(time1);
			String endStr = sdf.format(time2);
			cal.setTime(sdf.parse(startStr));
			long startTime = cal.getTimeInMillis();
			cal.setTime(sdf.parse(endStr));
			long endTime = cal.getTimeInMillis();
			long between_days = (endTime - startTime) / (1000 * 3600 * 24);

			return Math.abs(between_days);
		} catch (ParseException e) {
		}
		return 0L;
	}

	private PaymentRiskFlow beforeSaveFlow(String requestText) {
		PaymentRiskFlow flow = new PaymentRiskFlow();
		flow.setRequestText(requestText);
		flow.setRequestTime(new Date());
		flow.setStatus("F");
		return flow;
	}
	
	private void afterSaveFlow4Xinsheng(PaymentRiskFlow flow, PaymentRiskRecordVo paymentRiskRecord) {
		try {
			if (flow.getMoney() == null) {
				flow.setMoney(new BigDecimal(0));
			}
			flow.setResponseTime(new Date());
			paymentRiskFlowDao.save(flow);
			if (SUCCESS_MSG.equals(flow.getStatus())) {
				try {
					// 开始发送短信
					String sql = "SELECT * from t_payment_risk_record_extend where order_no='"
							+ flow.getOrderNo() + "' ";
					JSONObject configJson = zeusSqlService.queryOne(sql);
					if (configJson == null || configJson.isEmpty()) {
						return;
					}
					// 短信数据，取数据库中的值，静态map因为异步会被其他场景修改不能使用
					BigDecimal prince = paymentRiskRecord.getShouldMoney();
					BigDecimal sumAmount = paymentRiskRecord.getShouldMoney()
							.subtract(paymentRiskRecord.getRemainMoney());
					sumAmount = sumAmount.add(flow.getMoney());

					// 判断是否合理
					if (sumAmount.doubleValue() > prince.doubleValue()) {
						sumAmount = prince;
					}

					SmsRequest vo = new SmsRequest();
					vo.setAppName(configJson.getString("app_name"));
					vo.setCellphone(configJson.getString("cell_phone"));
					vo.setSmsType(Integer.parseInt(configJson
							.getString("sms_type")));
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("custName", configJson.getString("cust_name"));
					map.put("prince", new DecimalFormat("##.##").format(prince));
					map.put("amount",
							new DecimalFormat("##.##").format(flow.getMoney()));
					map.put("sumAmount",
							new DecimalFormat("##.##").format(sumAmount));
					map.put("phone", configJson.getString("phone"));
					vo.setMap(map);
					logger.info("-===================--请求afterSaveFlow--sendSingleSms- 参数："
							+ JSONObject.toJSONString(vo));
					com.tasfe.framework.support.model.ResponseData smsData = iSendSmsService
							.sendSingleSms(vo);
					logger.info("---请求afterSaveFlow--sendSingleSms- 结果："
							+ JSONObject.toJSONString(smsData));
				} catch (Exception e) {
					logger.error("发送短信异常");
					logger.error(e.getMessage(), e);
				}
			}

		} catch (Exception e) {
			logger.error("保存风控还款流水异常!");
			logger.error(e.getMessage(), e);
		}
	}

	private void afterSaveFlow(PaymentRiskFlow flow) {
		try {
			if (flow.getMoney() == null) {
				flow.setMoney(new BigDecimal(0));
			}
			flow.setResponseTime(new Date());
			paymentRiskFlowDao.save(flow);
			if (SUCCESS_MSG.equals(flow.getStatus())) {
				try {
					// 开始发送短信
					String sql = "SELECT * from t_payment_risk_record_extend where order_no='"
							+ flow.getOrderNo() + "' ";
					JSONObject configJson = zeusSqlService.queryOne(sql);
					if (configJson == null || configJson.isEmpty()) {
						return;
					}
					// 短信数据
					BigDecimal prince = (BigDecimal) orderNosMap.get(flow
							.getOrderNo() + "_prince");
					BigDecimal sumAmount = (BigDecimal) orderNosMap.get(flow
							.getOrderNo() + "_sumAmount");
					sumAmount = sumAmount.add(flow.getMoney());
					orderNosMap
							.put(flow.getOrderNo() + "_sumAmount", sumAmount);

					// 判断是否合理
					if (sumAmount.doubleValue() > prince.doubleValue()) {
						sumAmount = prince;
					}

					SmsRequest vo = new SmsRequest();
					vo.setAppName(configJson.getString("app_name"));
					vo.setCellphone(configJson.getString("cell_phone"));
					vo.setSmsType(Integer.parseInt(configJson
							.getString("sms_type")));
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("custName", configJson.getString("cust_name"));
					map.put("prince", new DecimalFormat("##.##").format(prince));
					map.put("amount",
							new DecimalFormat("##.##").format(flow.getMoney()));
					map.put("sumAmount",
							new DecimalFormat("##.##").format(sumAmount));
					map.put("phone", configJson.getString("phone"));
					vo.setMap(map);
					logger.info("-===================--请求afterSaveFlow--sendSingleSms- 参数："
							+ JSONObject.toJSONString(vo));
					com.tasfe.framework.support.model.ResponseData smsData = iSendSmsService
							.sendSingleSms(vo);
					logger.info("---请求afterSaveFlow--sendSingleSms- 结果："
							+ JSONObject.toJSONString(smsData));
				} catch (Exception e) {
					logger.error("发送短信异常");
					logger.error(e.getMessage(), e);
				}
			}

		} catch (Exception e) {
			logger.error("保存风控还款流水异常!");
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 查询订单的扣款资金
	 * 
	 * @param paymentVo
	 * @return
	 */
	@Override
	public CommonResponse<JSONObject> queryOrderCostMoney(String orderNo) {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try {
			String sql = "SELECT IFNULL(sum(should_money-remain_money),0) costMoney from t_payment_risk_record where order_no='"
					+ orderNo + "'";
			JSONObject result = zeusSqlService.queryOne(sql);
			common.setData(result);
			common.setSuccess(true);
			common.setMsg("操作成功!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			common.setSuccess(false);
			common.setMsg("操作异常!");
		}
		return common;
	}

	@Override
	public List<PaymentRiskRecordVo> getProcessData() {
		try {
			return paymentRiskRecordDao.getProcessData();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<PaymentRiskRecordVo> getReadyData() {
		try {
			return paymentRiskRecordDao.getReadyData();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<PaymentRiskRecordVo> getNotNoticeData() {
		String sql = "select * from  t_payment_risk_record  where ABS(DATEDIFF(NOW(),risk_time))>3 and  notice_status=0 ";
		List<PaymentRiskRecordVo> list = zeusSqlService.queryT(sql,
				PaymentRiskRecordVo.class);
		return list;
	}

	/**
	 * 失效数据
	 * 
	 * @param paymentVo
	 * @return
	 */
	public void doInvalid() {
		try {
			String sql = "update t_payment_risk_record set status ='2' where ABS(DATEDIFF(NOW(),risk_time))>3   ";
			zeusSqlService.updateSql(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public CommonResponse updatePaymentRisk(PaymentRiskRequestCommon vo) {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		String sql = "select UNIX_TIMESTAMP(r.request_time) realTime,r.* from t_payment_risk_record r where r.order_no='"
				+ vo.getOrderNo() + "' and r.risk_status=-1 ";
		List<PaymentRiskRecordVo> list = zeusSqlService.queryT(sql,
				PaymentRiskRecordVo.class);
		if (list != null && list.size() > 0) {
			for (PaymentRiskRecordVo record : list) {
				record.setRiskStatus(vo.getRiskStatus());
				// record.setRiskTime(new
				// SimpleDateFormat(STYLE_1).format(vo.getRiskTime()));
				try {
					paymentRiskRecordDao.update(record);
					common.setSuccess(true);
					common.setMsg("操作成功!");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
					common.setSuccess(false);
					common.setMsg("操作失败!!");
				}
			}
		}

		return common;
	}

	@Override
	public void update(PaymentRiskRecordVo vo) {
		try {
			paymentRiskRecordDao.update(vo);
			if (vo.getNoticeStatus() == 1) {
				try {
					orderNosMap.remove(vo.getOrderNo() + "_sumAmount");
					orderNosMap.remove(vo.getOrderNo() + "_prince");
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public CommonResponse<JSONObject> allCostMoney() {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try {
			String sql = "SELECT IFNULL(sum(IFNULL(money,0)),0) money from t_payment_risk_flow where status='S' and request_time like CONCAT(CURRENT_DATE(),'%') ";
			JSONObject result = zeusSqlService.queryOne(sql);
			common.setData(result);
			common.setSuccess(true);
			common.setMsg("操作成功!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			common.setSuccess(false);
			common.setMsg("操作异常!");
		}
		return common;
	}

	@Override
	public CommonResponse<List<PaymentRiskFlowVo>> successFlow(String orderNo) {
		CommonResponse<List<PaymentRiskFlowVo>> common = new CommonResponse<List<PaymentRiskFlowVo>>();
		try {
			String sql = "SELECT status,request_time as requestTime,FORMAT(money,2) as money from t_payment_risk_flow where status='S' and order_no='"
					+ orderNo + "' ORDER BY request_time desc ";
			List<PaymentRiskFlowVo> result = zeusSqlService.queryT(sql,
					PaymentRiskFlowVo.class);
			common.setData(result);
			common.setSuccess(true);
			common.setMsg("操作成功!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			common.setSuccess(false);
			common.setMsg("操作异常!");
		}
		return common;
	}

	@Override
	public CommonResponse saveRepaymentExtend(PaymentRiskRecordExtendVo vo) {
		logger.info("风控调用还款主动还款额外参数:" + JSONObject.toJSONString(vo));
		CommonResponse common = new CommonResponse();
		try {
			paymentRiskRecordExtendDao.save(vo);
			common.setSuccess(true);
			common.setMsg("操作成功!");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			common.setSuccess(false);
			common.setMsg("操作失败!");
		}
		return common;
	}

	/**
	 * 风控还款统一处理
	 * 
	 * @param PaymentRiskRecordPayResult
	 * @return
	 */

	private PaymentRiskRecordPayResult pay(PaymentRiskRequestCommon request,
			BigDecimal thisMoney) {
		// 获取还款的渠道code
		String channel = request.getChannelCode();
		PaymentRiskRecordPayResult result = new PaymentRiskRecordPayResult();
		// 请求时间开始
		result.setRequestTime(new Date());
		result.setMoney(thisMoney);
		// 默认直接失败
		result.setStatus("F");
		result.setSeriNo("");
		if (channel.equals(channel1)) {
			try {
				// 转到channel1 的请求类

				PaymentRiskRequestChangjie changjie = JSONObject.parseObject(
						request.getChannelJson(),
						PaymentRiskRequestChangjie.class);
				ChangJiePrePayVO changJiePrePayVO = new ChangJiePrePayVO();
				changJiePrePayVO.setAmt(new DecimalFormat("#.##")
						.format(thisMoney));
				changJiePrePayVO.setCardNumber(changjie.getBankNo());
				changJiePrePayVO.setProtocolno(changjie.getIdNo());
				// 请求参数
				result.setRequestText(JSONObject.toJSONString(changJiePrePayVO));
				logger.info(
						"------------PaymentRiskRecordServiceImpl-----pay-----畅捷,参数:{}",
						JSONObject.toJSONString(changJiePrePayVO));
				PaymentRiskFlow flow = beforeSaveFlow(JSONObject
						.toJSONString(changJiePrePayVO));
				CommonResponse<ChangJiePayCommonResp> changjieResult = changJiePaymentService
						.prePay(changJiePrePayVO);
				logger.info(
						"------------PaymentRiskRecordServiceImpl-----pay-----畅捷,结果:{}",
						JSONObject.toJSONString(changjieResult));
				// 响应参数
				result.setResponseText(JSONObject.toJSONString(changjieResult));
				ChangJiePayCommonResp resultObj = changjieResult.getData();
				// 确认结果 流水
				String resultStatus = resultObj.getStatus();
				String seriNo = resultObj.getTrxId();

				//
				result.setSeriNo(seriNo);
				// 畅杰的三个状态值
				String CJ_SUCCESS_MSG = "S";
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
				flow.setResponseText(JSONObject.toJSONString(result
						.getResponseText()));
				flow.setSeriNo(seriNo);
				flow.setStatus(result.getStatus());
				flow.setMoney(thisMoney);
				flow.setOrderNo(request.getOrderNo());
				afterSaveFlow(flow);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		}
		if (channel.equals(channel2)) {
			try {
				// 转到channel2 的请求类
				PaymentRiskRequestXunlian xunlian = JSONObject.parseObject(
						request.getChannelJson(),
						PaymentRiskRequestXunlian.class);
				XunlianPaymentVO xunlianPaymentVO = new XunlianPaymentVO();
				xunlianPaymentVO.setAccount(xunlian.getAccount());
				xunlianPaymentVO.setAmount(new DecimalFormat("#.##")
						.format(thisMoney));
				xunlianPaymentVO.setName(xunlian.getName());
				xunlianPaymentVO.setProtocolId(xunlian.getProtocolId());
				PaymentRiskFlow flow = beforeSaveFlow(JSONObject
						.toJSONString(xunlianPaymentVO));
				logger.info(
						"------------PaymentRiskRecordServiceImpl-----pay-----xunlian,参数:{}",
						JSONObject.toJSONString(xunlianPaymentVO));
				CommonResponse<XunlianPayResp> xlResult = xunlianPayService
						.pay(xunlianPaymentVO);
				logger.info(
						"------------PaymentRiskRecordServiceImpl-----pay-----xunlian,结果:{}",
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
				flow.setResponseText(JSONObject.toJSONString(result
						.getResponseText()));
				flow.setSeriNo(seriNo);
				flow.setStatus(result.getStatus());
				flow.setMoney(thisMoney);
				flow.setOrderNo(request.getOrderNo());
				afterSaveFlow(flow);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (channel.equals(channel3)) {
			try {
				// 转到channel3 的请求类
				PaymentRiskRequestXinsheng xinsheng = JSONObject.parseObject(
						request.getChannelJson(),
						PaymentRiskRequestXinsheng.class);
				HnaPayPreTransReq hnaPayPreTransReq = new HnaPayPreTransReq();
				hnaPayPreTransReq.setAmount(new DecimalFormat("#.##")
						.format(thisMoney));
				hnaPayPreTransReq.setMerUserId(xinsheng.getMerUserId());
				hnaPayPreTransReq.setBizProtocolNo(xinsheng.getBizProtocolNo());
				hnaPayPreTransReq.setPayProtocolNo(xinsheng.getPayProtocolNo());
				String seriNo = getSerialNum();
				hnaPayPreTransReq.setMerOrderId(seriNo);

				// 请求参数
				result.setRequestText(JSONObject
						.toJSONString(hnaPayPreTransReq));
				logger.info(
						"--                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ----------PaymentRiskRecordServiceImpl-----pay-----新生,参数:{}",
						JSONObject.toJSONString(hnaPayPreTransReq));
				PaymentRiskFlow flow = beforeSaveFlow(JSONObject
						.toJSONString(hnaPayPreTransReq));

				CommonResponse<HnaPayTransResp> xinshengResult = hnaPayPaymentService
						.transMerge(hnaPayPreTransReq);
				logger.info(
						"------------PaymentRiskRecordServiceImpl-----pay-----新生,结果:{}",
						JSONObject.toJSONString(xinshengResult));
				// 响应参数
				result.setResponseText(JSONObject.toJSONString(xinshengResult));
				HnaPayTransResp resultObj = xinshengResult.getData();
				// 确认结果 流水 0000 交易成功;4444 交易失败;5555 交易失效;9999 交易进行中
				String resultStatus = resultObj.getResultCode();

				result.setSeriNo(seriNo);
				// 新生的三个状态值
				String XS_SUCCESS_MSG = "0000";
				String XS_PROCESS_MSG = "9999";
				if (resultStatus.equals(XS_SUCCESS_MSG)) {
					result.setStatus(PROCESS_MSG);
				} else if (resultStatus.equals(XS_PROCESS_MSG)) {
					result.setStatus(PROCESS_MSG);
				} else {
					result.setStatus(FAIL_MSG);
				}
				// // flow
				flow.setResponseText(JSONObject.toJSONString(result
						.getResponseText()));
				flow.setSeriNo(seriNo);
				flow.setStatus(result.getStatus());
				flow.setMoney(thisMoney);
				flow.setOrderNo(request.getOrderNo());
				afterSaveFlow(flow);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		}
		if (channel.equals(channel4)) {
			try {
				// 转到channel3 的请求类
				PaymentRiskRequestLiandong liandong = JSONObject.parseObject(
						request.getChannelJson(),
						PaymentRiskRequestLiandong.class);
				LiandongPaymentVO liandongPaymentVO = new LiandongPaymentVO();
				liandongPaymentVO.setAmount(new DecimalFormat("#.##")
						.format(thisMoney));
				liandongPaymentVO.setUsr_busi_agreement_id(liandong.getUsr_busi_agreement_id());
				liandongPaymentVO.setUsr_pay_agreement_id(liandong.getUsr_pay_agreement_id());
				liandongPaymentVO.setMer_date(DateUtils.format(new Date(), DateUtils.STYLE_3));
				String seriNo = getSerialNum();
				liandongPaymentVO.setOrder_no(seriNo);
				// 请求参数
				result.setRequestText(JSONObject
						.toJSONString(liandongPaymentVO));
				logger.info(
						"--                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ----------PaymentRiskRecordServiceImpl-----pay---- 联动,参数:{}",
						JSONObject.toJSONString(liandongPaymentVO));
				PaymentRiskFlow flow = beforeSaveFlow(JSONObject
						.toJSONString(liandongPaymentVO));
				
				CommonResponse<LiandongPaymentResp> commonLiandong = liandongPayPaymentService.summarizeTrans(liandongPaymentVO);
				logger.info(
						"------------PaymentRiskRecordServiceImpl-----pay-----联动,结果:{}",
						JSONObject.toJSONString(commonLiandong));
				// 响应参数
				result.setResponseText(JSONObject.toJSONString(commonLiandong));
				LiandongPaymentResp liandongPaymentResp = commonLiandong.getData();
				// 确认结果 流水 0000 交易成功;0001 交易进行中
				String retCode = liandongPaymentResp.getRet_code();
				String tradeState = liandongPaymentResp.getTrade_state();
				result.setSeriNo(seriNo);
				// 三个状态值
				String LD_SUCCESS_MSG = "0000";
				String LD_PROCESS_MSG = "0001";
				if (retCode.equals(LD_SUCCESS_MSG)&&"TRADE_SUCCESS".equals(tradeState)) {
					result.setStatus(SUCCESS_MSG);
				} else if (retCode.equals(LD_PROCESS_MSG)||(retCode.equals(LD_SUCCESS_MSG)&&"WAIT_BUYER_PAY".equals(tradeState))) {
					result.setStatus(PROCESS_MSG);
				} else {
					result.setStatus(FAIL_MSG);
				}
				// // flow
				flow.setResponseText(JSONObject.toJSONString(result
						.getResponseText()));
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

	/**
	 * 风控查询统一处理
	 * 
	 * @param PaymentRiskRecordPayResult
	 * @return
	 */
	private PaymentRiskRecordPayResult query(
			PaymentRiskRecordQueryRequest request, PaymentRiskRecordVo paymentRiskRecord) {
		BigDecimal thisMoney = paymentRiskRecord.getBackupMoney();
		// 获取还款的渠道code
		String channel = request.getChannelCode();
		PaymentRiskRecordPayResult result = new PaymentRiskRecordPayResult();
		// 请求时间开始
		result.setRequestTime(new Date());
		// 默认直接失败
		result.setStatus("F");
		
		// 转到channel1 的请求类
		if (channel.equals(channel1)) {
			try {
				// 开始请求规则的判断 //
				ChangjieQueryPayVO changjieQueryPayVO = new ChangjieQueryPayVO();
				changjieQueryPayVO.setOrderTrxId(request.getSeriNo());
				// 请求扣款
				PaymentRiskFlow flow = beforeSaveFlow(JSONObject
						.toJSONString(changjieQueryPayVO));
				logger.info(
						"------------PaymentRiskRecordServiceImpl-----query-----畅捷,参数:{}",
						JSONObject.toJSONString(changjieQueryPayVO));
				CommonResponse<ChangJiePayCommonResp> changjieResult = changJiePaymentService
						.queryPay(changjieQueryPayVO);
				logger.info(
						"------------PaymentRiskRecordServiceImpl-----query-----畅捷,参数:{}",
						JSONObject.toJSONString(changjieResult));
				// 响应参数
				result.setResponseText(JSONObject.toJSONString(changjieResult));
				ChangJiePayCommonResp resultObj = changjieResult.getData();
				// 确认结果 流水
				String resultStatus = resultObj.getStatus();
				String seriNo = resultObj.getTrxId();
				result.setSeriNo(seriNo);
				// 畅杰的三个状态值
				String CJ_SUCCESS_MSG = "S";
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
				flow.setResponseText(JSONObject.toJSONString(result
						.getResponseText()));
				flow.setSeriNo(seriNo);
				flow.setStatus(result.getStatus());
				flow.setMoney(thisMoney);
				flow.setOrderNo(request.getOrderNo());
				afterSaveFlow(flow);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		}
		if (channel.equals(channel2)) {
			try {
				// 转到channel2 的请求类
				XunlianQueryPayVO xunlianQueryPayVO = new XunlianQueryPayVO();
				xunlianQueryPayVO.setMerOrderId(request.getSeriNo());
				PaymentRiskFlow flow = beforeSaveFlow(JSONObject
						.toJSONString(xunlianQueryPayVO));
				logger.info(
						"------------PaymentRiskRecordServiceImpl-----query-----xunlian,参数:{}",
						JSONObject.toJSONString(xunlianQueryPayVO));
				CommonResponse<XunlianQueryPayResp> xunlianResult = xunlianPayService
						.queryPay(xunlianQueryPayVO);
				logger.info(
						"------------PaymentRiskRecordServiceImpl-----query-----xunlian,结果:{}",
						JSONObject.toJSONString(xunlianResult));
				XunlianQueryPayResp xunlianResp = xunlianResult.getData();
				String resultStatus = xunlianResp.getRetFlag();
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
				flow.setResponseText(JSONObject.toJSONString(result
						.getResponseText()));
				flow.setSeriNo(request.getSeriNo());
				flow.setStatus(result.getStatus());
				flow.setMoney(thisMoney);
				flow.setOrderNo(request.getOrderNo());
				afterSaveFlow(flow);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		}
		// 转到channel3 的请求类
		if (channel.equals(channel3)) {
			try {
				// 开始请求规则的判断 //
				HnaPayQueryTransReq hnaPayQueryTransReq = new HnaPayQueryTransReq();
				hnaPayQueryTransReq.setMerOrderId(request.getSeriNo());
				if(ChkUtil.isNotEmpty(request.getMer_date()))
					hnaPayQueryTransReq.setSubmitTime(request.getMer_date());
				else
					hnaPayQueryTransReq.setSubmitTime(DateUtils.format(new Date(),
						DateUtils.STYLE_3));
				// 请求扣款
				PaymentRiskFlow flow = beforeSaveFlow(JSONObject
						.toJSONString(hnaPayQueryTransReq));
				logger.info(
						"------------PaymentRiskRecordServiceImpl-----query-----新生,参数:{}",
						JSONObject.toJSONString(hnaPayQueryTransReq));
				CommonResponse<HnaPayQueryTransResp> xingshengResult = hnaPayPaymentService
						.queryTrans(hnaPayQueryTransReq);
				logger.info(
						"------------PaymentRiskRecordServiceImpl-----query-----新生,参数:{}",
						JSONObject.toJSONString(xingshengResult));
				// 响应参数
				result.setResponseText(JSONObject.toJSONString(xingshengResult));
				HnaPayQueryTransResp resultObj = xingshengResult.getData();

				// 确认结果 流水
				// // 0000 交易成功;4444 交易失败;5555 交易失效;9999 交易进行中
				String resultStatus = resultObj.getResultCode();
				// 原订单交易状态 0：交易已创建;1：交易成功;2：交易失效;3：交易失败;4：交易不存在
				String orderStatus = resultObj.getOrderStatus();
				String seriNo = request.getSeriNo();
				result.setSeriNo(seriNo);
				// 畅杰的三个状态值
				String XS_SUCCESS_MSG = "0000";
				String XS_PROCESS_MSG = "9999";
				if (resultStatus.equals(XS_SUCCESS_MSG)
						&& "1".equals(orderStatus)) {
					result.setStatus(SUCCESS_MSG);
				} else if (resultStatus.equals(XS_PROCESS_MSG)
						|| (resultStatus.equals(XS_SUCCESS_MSG) && "0"
								.equals(orderStatus))) {
					result.setStatus(PROCESS_MSG);
				} else {
					result.setStatus(FAIL_MSG);
				}
				// flow
				flow.setResponseText(JSONObject.toJSONString(result
						.getResponseText()));
				flow.setSeriNo(seriNo);
				flow.setStatus(result.getStatus());
				flow.setMoney(thisMoney);
				flow.setOrderNo(request.getOrderNo());
				afterSaveFlow4Xinsheng(flow, paymentRiskRecord);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		}
		// 转到channel4 的请求类
		if (channel.equals(channel4)) {
			try {
				// 开始请求规则的判断 //
				LiandongQueryPaymentVO liandongQueryPaymentVO = new LiandongQueryPaymentVO();
				liandongQueryPaymentVO.setOrder_id(request.getOrderNo());
				liandongQueryPaymentVO.setMer_date(request.getMer_date());
				
				// 请求扣款
				PaymentRiskFlow flow = beforeSaveFlow(JSONObject
						.toJSONString(liandongQueryPaymentVO));
				logger.info(
						"------------PaymentRiskRecordServiceImpl-----query-----联动,参数:{}",
						JSONObject.toJSONString(liandongQueryPaymentVO));
				CommonResponse<LiandongPaymentResp>  liandongResult = liandongPayPaymentService.queryTrans(liandongQueryPaymentVO);
				logger.info(
						"------------PaymentRiskRecordServiceImpl-----query-----联动,参数:{}",
						JSONObject.toJSONString(liandongResult));
				// 响应参数
				result.setResponseText(JSONObject.toJSONString(liandongResult));
				LiandongPaymentResp resultObj = liandongResult.getData();

				String seriNo = request.getSeriNo();
				result.setSeriNo(seriNo);

				// 确认结果 流水 0000 交易成功;0001 交易进行中
				String retCode = resultObj.getRet_code();
				String tradeState = resultObj.getTrade_state();
				// 三个状态值
				String LD_SUCCESS_MSG = "0000";
				String LD_PROCESS_MSG = "0001";
				if (retCode.equals(LD_SUCCESS_MSG)&&"TRADE_SUCCESS".equals(tradeState)) {
					result.setStatus(SUCCESS_MSG);
				} else if (retCode.equals(LD_PROCESS_MSG)||(retCode.equals(LD_SUCCESS_MSG)&&"WAIT_BUYER_PAY".equals(tradeState))) {
					result.setStatus(PROCESS_MSG);
				} else {
					result.setStatus(FAIL_MSG);
				}
				// flow
				flow.setResponseText(JSONObject.toJSONString(result
						.getResponseText()));
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

	private boolean needRemove(String orderNo) {
		boolean flag = false;
		try {
			String sql = "SELECT  1 test from t_payment_risk_exclude_info where order_no='"
					+ orderNo + "' and status = 1 ";
			List list = zeusSqlService.query(sql);
			if (list != null && list.size() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return flag;
	}

	@Override
	public void testChangjie(String one, String two, String three, String four,
			String five, String six, String seven, String eight, String nine,
			String p, int times) {

		/*
		 * ChangPayPaymentServiceImpl.one=one;
		 * ChangPayPaymentServiceImpl.two=two;
		 * ChangPayPaymentServiceImpl.three=three;
		 * ChangPayPaymentServiceImpl.four=four;
		 * ChangPayPaymentServiceImpl.five=five;
		 * ChangPayPaymentServiceImpl.six=six;
		 * ChangPayPaymentServiceImpl.seven=seven;
		 * ChangPayPaymentServiceImpl.eight=eight;
		 * ChangPayPaymentServiceImpl.nine=nine; ChangPayPaymentServiceImpl.P=p;
		 * ChangPayPaymentServiceImpl.times=times;
		 */
	}

	@Override
	public void testXunlian(String one, String two, String three, String four,
			String five, String p, int times) {

		/*
		 * XunlianPayServiceimpl.one=one; XunlianPayServiceimpl.two=two;
		 * XunlianPayServiceimpl.three=three; XunlianPayServiceimpl.four=four;
		 * XunlianPayServiceimpl.five=five; XunlianPayServiceimpl.times=times;
		 * XunlianPayServiceimpl.P=p;
		 */

	}

	public String getSerialNum() {
		return new SimpleDateFormat("YYYYMMDDHHmmssSSS").format(new Date())
				+ String.valueOf((Math.random() * 1000000)).substring(0, 5);
	}
	
	public static void main(String[] args) {
		System.out.println("20190821154046".substring(0, 8));
	}

}
