package com.nyd.zeus.service.impls;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.nyd.order.api.zzl.OrderForZLQServise;
import com.nyd.order.model.OrderInfo;
import com.nyd.zeus.api.BillRepayService;
import com.nyd.zeus.api.zzl.HelibaoQuickPayService;
import com.nyd.zeus.api.zzl.ZeusSqlService;
import com.nyd.zeus.dao.BillDao;
import com.nyd.zeus.dao.OverdueBillDao;
import com.nyd.zeus.dao.enums.BillStatusEnum;
import com.nyd.zeus.dao.enums.UrgeStatusEnum;
import com.nyd.zeus.entity.BillExtendInfo;
import com.nyd.zeus.entity.BillProduct;
import com.nyd.zeus.entity.BillRepay;
import com.nyd.zeus.entity.BillRepayParam;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.OverdueBillInfo;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.helibao.PaymentVo;
import com.nyd.zeus.model.helibao.enums.AppTypeEnum;
import com.nyd.zeus.model.helibao.enums.DealSceneTypeEnum;
import com.nyd.zeus.model.helibao.util.Uuid;
import com.nyd.zeus.model.helibao.vo.pay.req.ConfirmBindPayVo;
import com.nyd.zeus.model.helibao.vo.pay.req.QueryOrderVo;
import com.nyd.zeus.model.helibao.vo.pay.req.QuickPayBindPayPreOrderVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.ConfirmBindPayResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.QueryOrderResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.QuickPayBindPayPreOrderResponseVo;
import com.nyd.zeus.service.enums.RequestTypeEnum;
import com.nyd.zeus.service.lock.RedisLock;


@Service
@Transactional
public class BillRepayServiceImpl implements BillRepayService {
	private Logger logger = LoggerFactory.getLogger(BillRepayServiceImpl.class);
	
	@Autowired
	private ZeusSqlService zeusSqlService;
	
	@Autowired
	private HelibaoQuickPayService helibaoQuickPayService;
	
	@Autowired
	private OrderForZLQServise orderForZLQServise;
	
	@Autowired
	private OverdueBillDao overdueBillDao;
	
	@Autowired
	private BillDao billDao;
	
	/*@Autowired
	private Redisson redisson;*/
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	public final  String HELIBAO_KEY	= "HELIBAO_KEY";
	//下午4点还款
//	private final int referenceHour = 16;
	
	//private final BigDecimal referenceMoney= new BigDecimal(1000);
	
//	private final BigDecimal lateFeeMax= new BigDecimal(100);
	
	private  String SUCCESS_CODE = "0000";
	private  String READY_SUCCESS_MSG = "SUCCESS";
	private  String READY_PROCESS_MSG = "DOING";
	
	
	private String nullCode = "4";
	private String successCode = "2";
	
	@Override
	public CommonResponse activeRepayment(PaymentVo paymentVo) {
		logger.info("用户主动还款请求参数:"+JSONObject.toJSONString(paymentVo));
		CommonResponse common = new CommonResponse();
		String key = HELIBAO_KEY+paymentVo.getBillNo();
		RedisLock redisLock = new RedisLock(redisTemplate, key, 10000, 20000);
		try {
			if (redisLock.lock()) {
			logger.info("用户主动还款请redisKey:"+redisLock.getLockKey());
			//common = payBillBylock(paymentVo.getBillNo(),paymentVo.getBindId(),paymentVo.getBindUserId(),paymentVo.getMobile(),null,"","1");
			paymentVo.setRequstType(RequestTypeEnum.ACTIVE_REPAYMENT.getCode());
			common = payBillBylock(paymentVo);
			}
		} catch (Exception e) {
			logger.error("主动还款异常:"+JSONObject.toJSONString(paymentVo));
			logger.error(e.getMessage(),e);
		}finally{
			redisLock.unlock();
		}
		return common;
	}

	
	@Override
	public CommonResponse batchRepayTask(PaymentVo paymentVo) {
		logger.info("跑批还款请求参数:"+JSONObject.toJSONString(paymentVo));
		CommonResponse common = new CommonResponse();
		String key = HELIBAO_KEY+paymentVo.getBillNo();
		RedisLock redisLock = new RedisLock(redisTemplate, key, 10000, 20000);
		try {
			if (redisLock.lock()) {
			logger.info("跑批还款redisKey:"+redisLock.getLockKey());
			paymentVo.setRequstType(RequestTypeEnum.BATCH_REPAYTASK.getCode());
			common = payBillBylock(paymentVo);
			//根据返回状态判断是否
			int breakValue = 0;
			while(true){
				breakValue ++;
				if(breakValue>10){
					break;
				}
				if("1".equals(common.getCode())){
					String billSql = "select * from t_bill where bill_no = '%s' and delete_flag='0' ";
					List<BillInfo> billList = zeusSqlService.queryT(String.format(billSql, paymentVo.getBillNo()), BillInfo.class);
				    if(null == billList || billList.size()<1){
				    	break;
				    }
				    BillInfo info = billList.get(0);
				    if(BillStatusEnum.REPAY_SUCESS.getCode().equals(info.getBillStatus())){
				    	break;
				    }
				    BigDecimal waitMoney = info.getWaitRepayAmount();
				    if(waitMoney.compareTo(BigDecimal.ZERO)==0){
				    	break;
				    }
				   common = payBillBylock(paymentVo);
				}else{
					break;
				}
			}
			}
		} catch (Exception e) {
			logger.error("还款跑批异常:"+JSONObject.toJSONString(paymentVo));
			logger.error(e.getMessage(),e);
		}finally{
			redisLock.unlock();
		}
		return common;
	}
	
	@Override
	public CommonResponse batchOverDueTask(PaymentVo paymentVo) {
		logger.info("跑批逾期请求参数:"+JSONObject.toJSONString(paymentVo));
		CommonResponse common = new CommonResponse();
		String key = HELIBAO_KEY+paymentVo.getBillNo();
		RedisLock redisLock = new RedisLock(redisTemplate, key, 10000, 20000);
		try {
			if (redisLock.lock()) {
			logger.info("跑批逾期redisKey:"+redisLock.getLockKey());
		    paymentVo.setRequstType(RequestTypeEnum.BATCH_OVERDUETASK.getCode());
			common = payBillBylock(paymentVo);
			}
		} catch (Exception e) {
			logger.error("逾期跑批异常:"+JSONObject.toJSONString(paymentVo));
			logger.error(e.getMessage(),e);
		}finally{
			redisLock.unlock();
		}
		return common;
	}

	@Override
	public CommonResponse mannagerRepayment(PaymentVo paymentVo) {
		logger.info("主动代扣请求参数:"+JSONObject.toJSONString(paymentVo));
		CommonResponse common = new CommonResponse();
		String key = HELIBAO_KEY+paymentVo.getBillNo();
		RedisLock redisLock = new RedisLock(redisTemplate, key, 10000, 20000);
		try {
			if (redisLock.lock()) {
			logger.info("主动代扣redisKey:"+redisLock.getLockKey());
	        paymentVo.setRequstType(RequestTypeEnum.MANAGE_REPAYMENT.getCode());
			common = payBillBylock(paymentVo);
			}
			} catch (Exception e) {
			logger.error("主动代扣异常:"+JSONObject.toJSONString(paymentVo));
			logger.error(e.getMessage(),e);
		}finally{
			redisLock.unlock();
		}
		logger.info("主动代扣返回报文:"+JSONObject.toJSONString(common));
		return common;
	}
	
	@Override
	public CommonResponse flatAccount(PaymentVo paymentVo) {
		logger.info("平账接口请求参数:"+JSONObject.toJSONString(paymentVo));
		CommonResponse common = new CommonResponse();
		String key = HELIBAO_KEY+paymentVo.getBillNo();
		RedisLock redisLock = new RedisLock(redisTemplate, key, 10000, 20000);
		try {
			if (redisLock.lock()) {
			logger.info("平账接口redisKey:"+redisLock.getLockKey());
	        paymentVo.setRequstType(RequestTypeEnum.FLAT_ACCOUNT.getCode());
			common = payBillBylock(paymentVo);
			}
			} catch (Exception e) {
			logger.error("平账异常:"+JSONObject.toJSONString(paymentVo));
			logger.error(e.getMessage(),e);
		}finally{
			redisLock.unlock();
		}
		logger.info("平账返回报文:"+JSONObject.toJSONString(common));
		return common;
	}

	/**
	 * 调用次方法需加锁
	 * @param billNo
	 * @param requstType 1 主动还款，2跑批还款  3 跑批逾期 4 管理系统代扣 5 平账接口
	 * CommonResponse.code 1 成功 2 失败 3 处理中 4 无账单数据
	 * @return
	 */
	private CommonResponse payBillBylock(PaymentVo paymentVo){
		logger.info("调用次方法需加锁:"+JSONObject.toJSONString(paymentVo));
		String billNo = paymentVo.getBillNo();
		String bindId = paymentVo.getBindId();
		String bindUserId = paymentVo.getBindUserId();
		String mobile = paymentVo.getMobile();
		BigDecimal money = paymentVo.getPayMoney();
		String settleStatus = paymentVo.getSettleStatus();
		String requstType = paymentVo.getRequstType();
		String remark = paymentVo.getRemark();
		Date nowDay = new Date();
		CommonResponse common = new CommonResponse();
		String billSql = "select * from t_bill where bill_no = '%s' and delete_flag='0' ";
		List<BillInfo> billList = zeusSqlService.queryT(String.format(billSql, billNo), BillInfo.class);
		if(null == billList || billList.size()<1){
			common.setCode(nullCode);
			common.setMsg("无还款账单");
			return common;
		}
		BillInfo bill = billList.get(0);
		String billStatus = bill.getBillStatus();
		
		String paramSql = "select * from t_bill_repay_param";
		List<BillRepayParam> paramList = zeusSqlService.queryT(paramSql, BillRepayParam.class);
		BillRepayParam param = paramList.get(0);
		int referenceHour = param.getReferenceHour();
		BigDecimal referenceMoney = param.getReferenceMoney();
		BigDecimal lateFeeMax = param.getLatefeeMax();
		
		//还款
		if(BillStatusEnum.REPAY_SUCESS.getCode().equals(billStatus)){
			common.setCode(successCode);
			common.setMsg("还款成功");
			return common;
		}
		
		
		//1 成功，2 失败， 3处理中
		CommonResponse sendResult = null;
		String payMoney = "";
		//主动还款 全额还款
		if("1".equals(requstType)){
			payMoney = bill.getWaitRepayAmount().toString();
		}
		//跑批代扣
		if("2".equals(requstType)){
			if(getHour(nowDay)>=referenceHour){
			   payMoney = getMoney(bill.getWaitRepayAmount(),referenceMoney).toString();
			}else{
			   payMoney = bill.getWaitRepayAmount().toString();
			}
		}
		
		
		//逾期跑批
		if("3".equals(requstType) && !(BillStatusEnum.REPAY_SUCESS.getCode().equals(billStatus))){
			Date promiseDate = bill.getPromiseRepaymentDate();
			if(promiseDate.getTime()<nowDay.getTime()){
				OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
				Integer overDay = getOverDay(promiseDate,nowDay);
				overDay = Math.abs(overDay);
				overdueBillInfo.setUserId(bill.getUserId());
				overdueBillInfo.setBillNo(billNo);
				overdueBillInfo.setOrderNo(bill.getOrderNo());
				overdueBillInfo.setBillStatus(BillStatusEnum.REPAY_OVEDUE.getCode());
				overdueBillInfo.setOverdueDays(overDay);
				overdueBillInfo.setCreateTime(nowDay);
				overdueBillInfo.setDeleteFlag(0);
				overdueBillInfo.setUpdateTime(nowDay);
				saveOrUpdateOverdueInfo(bill,overdueBillInfo,lateFeeMax);
			}
			return common;
		}
		//管理系统代扣 或 平账
		if("4".equals(requstType) || "5".equals(requstType)){
			BigDecimal waitMoney = bill.getWaitRepayAmount();
			if(money.compareTo(waitMoney)==1){
				common.setCode(nullCode);
				common.setMsg("代扣金额超出剩余应还金额");
				return common;
			}
			payMoney = money.toString();
		}
		//平账接口
		if("5".equals(requstType)){
			//未结清
			if("2".equals(settleStatus)){
				BillRepay pay = new BillRepay();
				pay.setId(UUID.randomUUID().toString().replace("-", "")+Uuid.getUuid24());
				pay.setBillNo(billNo);
				pay.setUserId(bill.getUserId());
				pay.setOrderNo(bill.getOrderNo());
				pay.setOutOrderNumber("");
				pay.setRepayAmount(money);
				pay.setPayType(requstType);
				pay.setCreateTime(nowDay);
				pay.setResultCode("1");
				pay.setEndTime(nowDay);
				pay.setRemark(remark);
				saveOrUpdateBillRepay(bill, pay, true);
			}
			//已结清
			if("1".equals(settleStatus)){
				BigDecimal clearAmount = money;//结清金额
				BigDecimal repayAmount =money;//实际还款金额
				BigDecimal couponDerateAmount = BigDecimal.ZERO;//减免金额
				BigDecimal waitMoney = bill.getWaitRepayAmount();
				BigDecimal reductionMoney = waitMoney.subtract(money);
				if(reductionMoney.compareTo(BigDecimal.ZERO)==1){
					clearAmount = waitMoney;
					couponDerateAmount = reductionMoney;
					saveBillRepay(bill,requstType,"4",couponDerateAmount,nowDay,remark);
				}
				clearBill(bill,clearAmount,repayAmount,couponDerateAmount);
				saveBillRepay(bill,requstType,"1",money,nowDay,remark);
			}
			common.setSuccess(true);
			common.setCode("1");
			common.setMsg("平账成功");
			return common;
		}
		String exitSql = "select * from t_bill_repay where result_code='3' and bill_no ='%s'";
		List<BillRepay> repayList = zeusSqlService.queryT(String.format(exitSql, bill.getBillNo()), BillRepay.class);
		//如果流水有处理中 做查询接口 没有做请求
		BigDecimal queryMount = BigDecimal.ZERO;
		if(null != repayList && repayList.size()>0){
			String outOrderNumber = repayList.get(0).getOutOrderNumber();
			 queryMount = repayList.get(0).getRepayAmount();
			sendResult = queryHelibao(outOrderNumber);
		}else{
			sendResult = sendHelibao(billNo,bindId,bindUserId,payMoney,mobile);
		}
		BillRepay pay = new BillRepay();
		//发送结果
		if(sendResult.isSuccess()){
			if("1".equals(requstType)){
				String custSql = "select * from t_bill_extend_info where order_no = '%s'";
				List<BillExtendInfo> custList = zeusSqlService.queryT(String.format(custSql, bill.getOrderNo()), BillExtendInfo.class);
				if(null != custList && custList.size()>0){
					BillExtendInfo custInfo = custList.get(0);
					pay.setRemark(custInfo.getUserName());
				}
			}
			if("2".equals(requstType)){
				pay.setRemark("系统");
			}
			if("4".equals(requstType)){
				pay.setRemark("代扣");
			}
			pay.setId(UUID.randomUUID().toString().replace("-", "")+Uuid.getUuid24());
			pay.setBillNo(billNo);
			pay.setUserId(bill.getUserId());
			pay.setOrderNo(bill.getOrderNo());
			pay.setOutOrderNumber((String)sendResult.getData());
			pay.setRepayAmount(new BigDecimal(payMoney));
			pay.setPayType(requstType);
			pay.setCreateTime(nowDay);
			pay.setResultCode(sendResult.getCode());
			pay.setEndTime(nowDay);
			saveOrUpdateBillRepay(bill,pay,sendResult.isSuccess());
		}else{
			//查询还款查询接口返回成功 或 失败
			if(!"3".equals(sendResult.getCode())){
				pay.setRepayAmount(queryMount);
				pay.setBillNo(billNo);
				pay.setResultCode(sendResult.getCode());
				pay.setEndTime(nowDay);
				saveOrUpdateBillRepay(bill,pay,sendResult.isSuccess());
			}
		}
		
		// CommonResponse.code 1 成功 2 失败 3 处理中 4 无账单数据
		if("1".equals(sendResult.getCode()))
			common.setSuccess(true);
		common.setCode(sendResult.getCode());
		common.setMsg(sendResult.getMsg());
		
		logger.info("还款【{}】返回报文:"+JSONObject.toJSONString(common),requstType);
		return common;
	}

	 private void clearBill(BillInfo bill,BigDecimal clearAmount,BigDecimal repayAmount,BigDecimal couponDerateAmount){

			//已还款金额
			BigDecimal alreadyRepayAmount = bill.getAlreadyRepayAmount().add(repayAmount);
			//剩余应还金额
			BigDecimal waitRepayAmout = bill.getWaitRepayAmount().subtract(clearAmount);
			if(waitRepayAmout.compareTo(BigDecimal.ZERO) != 1){
				waitRepayAmout = BigDecimal.ZERO;
			}
			String billStatus = BillStatusEnum.REPAY_SUCESS.getCode();
			//订单状态结清
			if(waitRepayAmout.compareTo(BigDecimal.ZERO)==0){
				
				String updBillSql ="update t_bill set bill_status='%s',wait_repay_amount='%s',already_repay_amount='%s',coupon_derate_amount='%s',actual_settle_date=now(),update_time=now(),actual_settle_date=now() where bill_no='%s' and delete_flag='0'";
				String upd = String.format(updBillSql,billStatus,waitRepayAmout,alreadyRepayAmount,couponDerateAmount,bill.getBillNo());
				logger.info("订单状态结清更新sql:"+upd);
				zeusSqlService.updateSql(upd);
				try {
					OrderInfo order = new OrderInfo();
					order.setMark("1");
					order.setOrderNo(bill.getOrderNo());
					orderForZLQServise.findOrderStatus(order);
				} catch (Exception e) {
					logger.error("订单结清推送order服务异常:"+JSONObject.toJSONString(bill));
					logger.error("订单结清推送order服务异常:"+e.getMessage());
				}
			}else{
				String updBillSql ="update t_bill set wait_repay_amount='%s',already_repay_amount='%s',actual_settle_date=now(),update_time=now() where bill_no='%s' and delete_flag='0'";
				String upd = String.format(updBillSql,waitRepayAmout,alreadyRepayAmount,bill.getBillNo());
				logger.info("订单状态未结清更新sql:"+upd);
				zeusSqlService.updateSql(upd);
			}
			
		
	 }
	 
     private void saveBillRepay(BillInfo bill,String requstType,String resultCode,BigDecimal repayAmount,Date nowDay,String remark){
    	    BillRepay pay = new BillRepay();
			pay.setId(UUID.randomUUID().toString().replace("-", "")+Uuid.getUuid24());
			pay.setBillNo(bill.getBillNo());
			pay.setUserId(bill.getUserId());
			pay.setOrderNo(bill.getOrderNo());
			pay.setOutOrderNumber("");
			pay.setRepayAmount(repayAmount);
			pay.setPayType(requstType);
			pay.setCreateTime(nowDay);
			pay.setResultCode(resultCode);
			pay.setEndTime(nowDay);
			pay.setRemark(remark);
    	    billDao.saveBillRepay(pay);
	 }
	 private void saveOrUpdateBillRepay(BillInfo bill,BillRepay pay,Boolean flag){
	   
		String code  = pay.getResultCode(); //1 成功 2 失败 3 处理
		//1 成功更新还款记录表
		if("1".equals(code)){
			//已还款金额
			BigDecimal alreadyRepayAmount = bill.getAlreadyRepayAmount().add(pay.getRepayAmount());
			//剩余应还金额
			BigDecimal waitRepayAmout = bill.getWaitRepayAmount().subtract(pay.getRepayAmount());
			if(waitRepayAmout.compareTo(BigDecimal.ZERO) != 1){
				waitRepayAmout = BigDecimal.ZERO;
			}
			String billStatus = BillStatusEnum.REPAY_SUCESS.getCode();
			//订单状态结清
			if(waitRepayAmout.compareTo(BigDecimal.ZERO)==0){
				
				String updBillSql ="update t_bill set bill_status='%s',wait_repay_amount='%s',already_repay_amount='%s',actual_settle_date=now(),update_time=now(),actual_settle_date=now() where bill_no='%s' and delete_flag='0'";
				String upd = String.format(updBillSql,billStatus,waitRepayAmout,alreadyRepayAmount,pay.getBillNo());
				logger.info("订单状态结清更新sql:"+upd);
				zeusSqlService.updateSql(upd);
				try {
					OrderInfo order = new OrderInfo();
					order.setMark("1");
					order.setOrderNo(bill.getOrderNo());
					orderForZLQServise.findOrderStatus(order);
				} catch (Exception e) {
					logger.error("订单结清推送order服务异常:"+JSONObject.toJSONString(bill));
					logger.error("订单结清推送order服务异常:"+e.getMessage());
				}
			}else{
				String updBillSql ="update t_bill set wait_repay_amount='%s',already_repay_amount='%s',actual_settle_date=now(),update_time=now() where bill_no='%s' and delete_flag='0'";
				String upd = String.format(updBillSql,waitRepayAmout,alreadyRepayAmount,pay.getBillNo());
				logger.info("订单状态未结清更新sql:"+upd);
				zeusSqlService.updateSql(upd);
			}
			
		}
		//发送插入流水表
        if(flag){
        	billDao.saveBillRepay(pay);
       }else{
			//查询接口 更新流水表
			String updPaySql = "update t_bill_repay set result_code='%s',end_time=now() where bill_no ='%s' and result_code='%s'";
			String upd = String.format(updPaySql, pay.getResultCode(),pay.getBillNo(),"3");
			logger.info("查询接口 更新流水sql:"+upd);
			zeusSqlService.updateSql(upd);
		}
	}
	//更新逾期数据
	private void saveOrUpdateOverdueInfo(BillInfo bill,OverdueBillInfo overdueBillInfo,BigDecimal lateFeeMax) {
		try {
			
			String overProduct = "select * from t_bill_product where order_no='%s'";
			List<BillProduct> productList = zeusSqlService.queryT(String.format(overProduct, bill.getOrderNo()), BillProduct.class);
			if(null == productList || productList.size()<1){
				logger.info("订单产品没有数据");
				return;
			}
			BillProduct pro = productList.get(0);
			//产品定义 滞纳金
			BigDecimal lateFee = pro.getLateFee();
			
			//已罚滞纳金
			BigDecimal waitLateFee = bill.getLateFee();
			BigDecimal toTalLateFee = waitLateFee.add(lateFee);
			logger.info("已罚滞纳金"+toTalLateFee+"="+waitLateFee+"+"+lateFee);
			if(waitLateFee.compareTo(lateFeeMax)==-1){
				if(toTalLateFee.compareTo(lateFeeMax)==1){
					BigDecimal moreFee = toTalLateFee.subtract(lateFeeMax);
					lateFee = lateFee.subtract(moreFee);
					toTalLateFee = lateFeeMax;
				}
			}else{
				lateFee = BigDecimal.ZERO;
				toTalLateFee = lateFeeMax;
			}
			//应还本金
			BigDecimal repayPrinciple = bill.getRepayPrinciple();
			//产品定义 罚息率
			BigDecimal pennaltyRate = pro.getPenaltyRate();
			//已罚逾期费
			BigDecimal waitPenaltyFee = bill.getPenaltyFee();
			//当天罚息 本金*罚息*当前逾期天数(1)
			BigDecimal penaltyFeeDay = repayPrinciple.multiply(pennaltyRate);
			//保留两位小数
			penaltyFeeDay = penaltyFeeDay.divide(new BigDecimal(1),2,BigDecimal.ROUND_UP);
			BigDecimal totalPenaltyFeeDay = waitPenaltyFee.add(penaltyFeeDay);
			if(waitPenaltyFee.compareTo(repayPrinciple)==-1){
				//罚息费大于本金 = 本金
				if(totalPenaltyFeeDay.compareTo(repayPrinciple)==1){
					BigDecimal morePenaltyFee = totalPenaltyFeeDay.subtract(repayPrinciple);
					penaltyFeeDay = penaltyFeeDay.subtract(morePenaltyFee);
					totalPenaltyFeeDay = repayPrinciple;
				}
			}else{
				penaltyFeeDay = BigDecimal.ZERO;
				totalPenaltyFeeDay = repayPrinciple;
			}
			
			//已逾期天数
			int overDue =bill.getOverdueDays();
			//总逾期天数
			int totalOverDue = overDue+1;
			
			//剩余应还金额
			BigDecimal waitRepayAmount = bill.getWaitRepayAmount();
			//本次剩余还款金额=剩余应还金额+逾期滞纳金+逾期罚息费
			BigDecimal totalCurRepayAmount = waitRepayAmount.add(lateFee).add(penaltyFeeDay);
			
			String updBillSql = "update t_bill set bill_status='%s',wait_repay_amount='%s',late_fee='%s',overdue_days='%s',penalty_fee='%s',urge_status='%s' where bill_no='%s' and delete_flag='0'";
			String upd = String.format(updBillSql, BillStatusEnum.REPAY_OVEDUE.getCode(),totalCurRepayAmount,toTalLateFee,totalOverDue,totalPenaltyFeeDay,UrgeStatusEnum.URGE_ONE.getCode(),overdueBillInfo.getBillNo());
			logger.info("逾期更新sql:"+upd);
			zeusSqlService.updateSql(upd);
			overdueBillInfo.setBillStatus(BillStatusEnum.REPAY_OVEDUE.getCode());
			overdueBillInfo.setOverdueFine(lateFee);
			overdueBillInfo.setOverdueAmount(penaltyFeeDay);
			overdueBillInfo.setOverdueDays(1);
			overdueBillDao.save(overdueBillInfo);
			
		} catch (Exception e) {
			logger.error("更新逾期数据异常");
			logger.error(e.getMessage(),e);
		}
	}

	


	private CommonResponse queryHelibao(String outOrderNumber) {
		CommonResponse common = new CommonResponse();
		common.setSuccess(false);
		try {
			QueryOrderVo requestVo = new QueryOrderVo();
			requestVo.setP2_orderId(outOrderNumber);
			QueryOrderResponseVo  confirmResponse =helibaoQuickPayService.queryOrder(requestVo);;
			logger.info(" ###合利宝 支付支付返回结果 ：" + JSONObject.toJSONString(confirmResponse));
			// 返回码  返回信息
			String orderStatus = confirmResponse.getRt9_orderStatus();
			String respMsg = confirmResponse.getRt3_retMsg();
			// 成功
			if(READY_SUCCESS_MSG.equalsIgnoreCase(orderStatus) ){
				common.setCode("1");
				common.setMsg(respMsg);
			}else if(READY_PROCESS_MSG.equalsIgnoreCase(orderStatus)){
				common.setCode("3");
				common.setMsg(respMsg);
			}else{
				common.setCode("2");
				common.setMsg(respMsg);
			}
		} catch (Exception e) {
			logger.error("合利宝还款接口查询异常："+outOrderNumber);
			logger.error(e.getMessage(),e);
		}
		return common;
	}

	private CommonResponse sendHelibao(String billNo,String bindId, String outUserId,
			String payMoney, String mobile) {
		CommonResponse common = new CommonResponse();
		common.setSuccess(true);
		try {
			 // 预支付请求
			QuickPayBindPayPreOrderVo requestVo = new QuickPayBindPayPreOrderVo();
			requestVo.setP3_bindId(bindId); // 绑卡ID
			requestVo.setP4_userId(outUserId);
			requestVo.setP8_orderAmount(payMoney); // 交易金额
            requestVo.setP11_terminalType("OTHER"); // 终端类型
			requestVo.setP12_terminalId(billNo);  // 终端标识
            requestVo.setUserAccount(mobile);		// 用户注册账号
			requestVo.setAppType(AppTypeEnum.OTHER.getCode());          // 应用类型
            requestVo.setDealSceneType(DealSceneTypeEnum.REPAYMENT.getCode());     // 业务场景
			
			// 第一步  预支付
			logger.info(" ###合利宝 预支付支付请求结果 ：" + JSONObject.toJSONString(requestVo));
			QuickPayBindPayPreOrderResponseVo responseVo = 	helibaoQuickPayService.quickPayBindPayPreOrder(requestVo);
			logger.info(" ###合利宝 预支付支付返回结果 ：" + JSONObject.toJSONString(responseVo));
			
			if(!SUCCESS_CODE.equals(responseVo.getRt2_retCode())){
				common.setCode("2");
				common.setMsg(responseVo.getRt3_retMsg());
				common.setData(responseVo.getRt5_orderId());
				return common;
			}
			
			// 第二步支付
			ConfirmBindPayVo confirmVo = new ConfirmBindPayVo();
			confirmVo.setP3_orderId(responseVo.getRt5_orderId());
			ConfirmBindPayResponseVo  confirmResponse = helibaoQuickPayService.confirmBindPay(confirmVo);
			logger.info(" ###合利宝 支付支付返回结果 ：" + JSONObject.toJSONString(confirmResponse));
			
			// 返回码  返回信息
			String respCode = confirmResponse.getRt2_retCode();
			String respMsg = confirmResponse.getRt3_retMsg();
			String  orderStatus = confirmResponse.getRt9_orderStatus();
			
			common.setData(responseVo.getRt5_orderId());
			// 成功
			if(READY_SUCCESS_MSG.equalsIgnoreCase(orderStatus) ){
				common.setCode("1");
				common.setMsg(respMsg);
			}else if(READY_PROCESS_MSG.equalsIgnoreCase(orderStatus)){
				common.setCode("3");
				common.setMsg(respMsg);
			}else{
				common.setCode("2");
				common.setMsg(respMsg);
			}
		} catch (Exception e) {
			common.setCode("2");
			common.setMsg("请求合利宝异常");
			logger.error("请求合利宝还款异常："+billNo);
			logger.error(e.getMessage(),e);
		}
			
		return common;
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
			logger.error("逾期跑批异常解析逾期天数异常");
			logger.error(e.getMessage(),e);
		}
		return 0;
	}

	public  int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	private   BigDecimal getMoney(BigDecimal b1,BigDecimal b2){
		BigDecimal flag = new BigDecimal(2);
		//BigDecimal compareMoney = b1.multiply(flag);
		if(b1.compareTo(b2) == -1 ){
			return b1;
		}
		BigDecimal halfManey = b1.divide(flag,2,BigDecimal.ROUND_UP);
		return halfManey;
	}
	
	private static void dif(){
		BigDecimal a = new BigDecimal("1");
		BigDecimal b = new BigDecimal("3");
		BigDecimal halfManey = a.divide(b,2,BigDecimal.ROUND_UP);
		System.out.println(halfManey);
	}
	
	public static void main(String[] args) {
		dif();
	}


	

	
	
	
}
