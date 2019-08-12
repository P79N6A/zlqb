package com.zhiwang.zfm.controller.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.fabric.xmlrpc.base.Array;
import com.nyd.order.api.zzl.OrderForGYTServise;
import com.nyd.order.api.zzl.OrderForZQServise;
import com.nyd.order.api.zzl.THelibaoRecordService;
import com.nyd.order.entity.refund.RefundApplyEntity;
import com.nyd.order.model.TExecuteLoanRecord;
import com.nyd.order.model.THelibaoRecord;
import com.nyd.order.model.TOrderLoanRecord;
import com.nyd.order.model.common.ChkUtil;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.user.api.zzl.UserForWHServise;
import com.nyd.user.api.zzl.UserForZQServise;
import com.nyd.user.model.BankUser;
import com.nyd.zeus.api.zzl.ZeusForGYTServise;
import com.nyd.zeus.api.zzl.ZeusForWHServise;

import com.nyd.zeus.model.common.req.BillInfoTask;
import com.nyd.zeus.model.common.response.BillInfoVO;
import com.nyd.zeus.model.common.response.BillRepayVo;
import com.nyd.zeus.model.helibao.PaymentVo;
import com.zhiwang.zfm.common.util.DateUtils;
import com.zhiwang.zfm.service.api.sys.UserService;

/**
 * 功能说明： 自动执行
 * 修改人：yuanhao
 * 修改内容：
 * 修改注意点：
 */
@Component
public class TaskOrder {

	private Logger logger = LoggerFactory.getLogger(TaskOrder.class);
	
	@Autowired
	private OrderForZQServise orderForZQServise;
	
	@Autowired
	private UserForZQServise userForZQServise;
	
	@Autowired
	private OrderForGYTServise orderForGYTServise;

	@Autowired
	private ZeusForWHServise zeusForWHServise;
	
	@Autowired
	private UserForWHServise userForWHServise;
	
	@Autowired
	private THelibaoRecordService tHelibaoRecordService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ZeusForGYTServise zeusForGYTServise;
	
	//是否在执行自动逾期
	private static volatile boolean OVER_RUN = false;
	private static volatile boolean PAYMENT_RUN = false;
	private static volatile boolean PAYMENT_RUNING = false;
	//是否在执行自动逾期
	private static boolean LOAN_RUN = false;
	//是否在执行自动退款
	private static boolean REFUND_RUN = false;
	//是否在执行初始化出勤记录
 	private static boolean ATTENDANCE_RUN = false;
//	private static boolean ENGINETWO_RUN = false;
	private static boolean LOAN_RUN_SERI = false;
//	private static boolean LOAN_RUN_HLB_UPLOAD = false;
//	private static boolean BANK_QUERY_AUTH_STATUS = false;
//	private static boolean LOAN_RUN_HLB_QUERY = false;
	
	/**
	 * 自动逾期
	 */
	//@Scheduled(cron="${auto_orderover}")
	public void autoOrderOver(){
		
		logger.info("自动逾期开始>>>>");
		if(OVER_RUN){return;}
		logger.info("自动逾期开始>>>>"+OVER_RUN);
		OVER_RUN = true;
		//若为空则直接结束
		BillInfoTask task = new BillInfoTask();
		String beforeDay = DateUtils.getBefore(new Date(), 1, DateUtils.STYLE_2);
		task.setEndTime(beforeDay+" 23:59:59");
		List<BillInfoVO> list = zeusForWHServise.queryOverDueList(task);
		logger.info("自动逾期列表:"+JSONObject.toJSONString(list));
		if(list == null || list.size() == 0){
			logger.info("自动逾期结束<<<");
			OVER_RUN = false;
			return;
		}
		try{
			for(BillInfoVO bill:list){
				PaymentVo paymentVo = new PaymentVo();
				try{
					paymentVo.setBillNo(bill.getBillNo());
					zeusForWHServise.batchOverDueTask(paymentVo);
					logger.info("自动逾期成功!参数:"+JSONObject.toJSONString(paymentVo));
				}
				catch (Exception e) {
					logger.error(e.getMessage(),e);
					logger.error("自动逾期异常!"+JSONObject.toJSONString(paymentVo));
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			logger.error("自动逾期异常!");
		}finally {
			OVER_RUN =false;
		}
		logger.info("自动逾期结束<<<");
	}
	
	/**
	 * 自动还款
	 */
	//@Scheduled(cron="${auto_orderpayment}")
	public void autoOrderpayment(){
		
		logger.info("自动还款开始>>>>");
		if(PAYMENT_RUN){return;}
		try{
		PAYMENT_RUN = true;
		BillInfoTask task = new BillInfoTask();
		String beforeDay = DateUtils.getCurrentTime(DateUtils.STYLE_2);
		task.setStartTime(beforeDay+" 00:00:00");
		task.setEndTime(beforeDay+" 23:59:59");
		List<BillInfoVO> list = zeusForWHServise.queryPayList(task);
		if(list == null || list.size() == 0){
			logger.info("自动还款结束<<<");
			PAYMENT_RUN = false;
			return;
		}
		for(BillInfoVO bill:list){
				PaymentVo paymentVo = new PaymentVo();
				try{
					BankUser bank = userForWHServise.queryBankInfoByUserId(bill.getUserId(), "2");
					paymentVo.setBillNo(bill.getBillNo());
					paymentVo.setBindId(bank.getProtocolNo());
					paymentVo.setBindUserId(bank.getHlbUserId());
					paymentVo.setMobile(bank.getReservedPhone());
					zeusForWHServise.batchRepayTask(paymentVo);
					logger.info("自动还款成功!参数:"+JSONObject.toJSONString(paymentVo));
				}catch (Exception e) {
					logger.error(e.getMessage(),e);
					logger.error("自动还款异常!"+JSONObject.toJSONString(paymentVo));
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			logger.error("自动还款异常!");
		}finally {
			PAYMENT_RUN =false;
		}
		logger.info("自动还款结束<<<");
	}
	
	/**
	 *合利宝还款处理中
	 */
	//@Scheduled(cron="${auto_orderpaymentIng}")
	public void autoOrderpaymentIng(){
		
		logger.info("合利宝还款处理中开始>>>>");
		//还款跑批 不进行还款处理中查询
		if(PAYMENT_RUN){
			return;
		}
		if(PAYMENT_RUNING){return;}
		PAYMENT_RUNING = true;
		try{
		BillInfoTask task = new BillInfoTask();
		List<BillRepayVo> list = zeusForWHServise.queryPayProceList(task);
		if(list == null || list.size() == 0){
			logger.info("还款还款处理中结束<<<");
			PAYMENT_RUNING = false;
			return;
		}
		
			for(BillRepayVo repay:list){
				PaymentVo paymentVo = new PaymentVo();
				try{
					paymentVo.setBillNo(repay.getBillNo());
					zeusForWHServise.batchRepayTask(paymentVo);
					logger.info("还款处理中!参数:"+JSONObject.toJSONString(paymentVo));
				}catch (Exception e) {
					logger.error(e.getMessage(),e);
					logger.error("还款处理中异常!"+JSONObject.toJSONString(paymentVo));
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			logger.error("还款处理中异常!");
		}finally {
			PAYMENT_RUNING =false;
		}
		logger.info("还款处理中结束<<<");
	}
	
	
	
	
	/**
	 * 检测授权是否过期
	 * 功能说明： 
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	 */
	/*@Scheduled(cron="${auto_checkauth}")
	public void autoCheckauth(){
		//获得授权成功的列表
		logger.info("自动检测授权过期开始>>>");
		custAuthService.checkAuth();
		logger.info("自动检测授权过期结束<<<");
	}*/
	
	/**
	 * 放款跑批
	 */
	@Scheduled(cron="${auto_executeLoan}")
	public void conductLoan(){
		//查询待放款类别数据
		//拿到type为1的 调用第三方进行 放款
		//其他的 直接修改数据 crm_order order_loan_record status为1
		logger.info("查询未放款订单开始>>>>");
		//防止重复
		if(LOAN_RUN){return;}
		LOAN_RUN = true;
		List<TOrderLoanRecord> orderLoanRecords = orderForZQServise.getOrderLoanRecords();
		if(orderLoanRecords == null || orderLoanRecords.size() == 0){
			logger.info("未查询到待放款数据,放款跑批结<<<<");
			//放失败重复调用
			LOAN_RUN = false;
			return;
		}
		try {
			for (TOrderLoanRecord orderLoanRecord : orderLoanRecords) {
				
				 try {
//					//查询银行卡信息
					JSONObject bankInfo = userForZQServise.getAccountInfo(orderLoanRecord.getUserNo());
					if(ChkUtil.isEmpty(bankInfo)){
						logger.info("自动放款查询银行卡,银行卡信息为空!userNo:"+orderLoanRecord.getUserNo());
					}else{
						CommonResponse<JSONObject> common = orderForZQServise.executeOrderLoan(orderLoanRecord,bankInfo);
						if(common.isSuccess()){
							logger.info("自动放款成功!参数:{}", JSONObject.toJSONString(orderLoanRecord));
						}
						
					}
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
					logger.error("执行跑批放款异常,请求参数:{}",JSONObject.toJSONString(orderLoanRecord));
				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			logger.error("自动放款异常!");
		}finally {
			LOAN_RUN =false;
		}
		logger.info("自动放款结束<<<");
	}
	
	/**
	 * 跑批执行放款流水表
	 */
	@Scheduled(cron="${auto_executeLoanRecord}")
	public void conductExecuteLoanRecord(){
		logger.info("跑批执行放款流水开始>>>>");
		try {
			//防止重复
			if(LOAN_RUN_SERI){
				logger.info("跑批执行放款流水结束，已有处理中的流水<<<");
				return;
			}
			LOAN_RUN_SERI = true;
			List<TExecuteLoanRecord> baseList = orderForZQServise.getOrderExecuteLoanRecord();
			if(baseList == null || baseList.size() == 0){
				logger.info("跑批执行放款流水结束<<<");
				//放失败重复调用
				LOAN_RUN_SERI = false;
				return;
			}
		
			for (TExecuteLoanRecord tExecuteLoanRecord : baseList) {
				try {
					//查询银行卡信息
					JSONObject bankInfo = userForZQServise.getAccountInfo(tExecuteLoanRecord.getUserNo());
					if(ChkUtil.isEmpty(bankInfo)){
						logger.info("自动放款查询银行卡,银行卡信息为空!userNo:"+tExecuteLoanRecord.getUserNo());
					}else{
						CommonResponse<JSONObject> common = orderForZQServise.queryTransactionResult(tExecuteLoanRecord,bankInfo);
						if(common.isSuccess()){
							logger.info("跑批执行放款流水成功!参数:{}", JSONObject.toJSONString(tExecuteLoanRecord));
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			logger.error("跑批执行放款流水成功异常!");
		}finally {
			LOAN_RUN_SERI =false;
		}
		logger.info("跑批执行放款流水放款结束<<<");
	}
	

	/*@Scheduled(cron="${helibao_user_upload}")
	public void helibaoCustResult(){
		if(LOAN_RUN_HLB_UPLOAD){
			return;
		}
		LOAN_RUN_HLB_UPLOAD = true;
		
		logger.info("合利宝用户上传资质失败查询开始");
		List<HelibaoCustRecordVO> helibaoList = helibaoCustRecordService.getHelibaoRecordFail();
		if(helibaoList == null || helibaoList.size() == 0) {
			logger.info("未查询到合利宝用户上传失败订单,跑批结束<<<<");
			LOAN_RUN_HLB_UPLOAD = false;
			return;
		}
		try {
			for(HelibaoCustRecordVO helibaoCustRecordVO : helibaoList) {
				try {
					CommonResponse commonResponse = helibaoCustRecordService.userRegister(helibaoCustRecordVO);
					if(commonResponse.isSuccess() && StatusConstants.SUCCESS_CODE.equals(commonResponse.getCode())){
						logger.info("执行失败上传合利宝资质成功!参数:{}", JSONObject.toJSONString(helibaoCustRecordVO));
						return;
					}
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
					e.printStackTrace();
					logger.error("调用合利宝资质上传异常，异常信息{}，客户id{}", helibaoCustRecordVO.getCustInfoId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("执行上传合利宝资质异常，异常信息{}，请求参数", e,e.getMessage(),JSONObject.toJSONString(helibaoList));
		}finally{
			LOAN_RUN_HLB_UPLOAD = false;
		}
	}

	@Scheduled(cron="${helibao_user_query}")
	public void queryHelibaoResult(){
		if(LOAN_RUN_HLB_QUERY){
			return;
		}
		LOAN_RUN_HLB_QUERY = true;
		logger.info("合利宝认证查询开始");

		List<HelibaoCustRecordVO> helibaoQueryList = helibaoCustRecordService.getHelibaoRecordSuccess();
		if(helibaoQueryList == null || helibaoQueryList.size() == 0) {
			logger.info("未查询到合利宝认证订单,跑批结束<<<<");
			LOAN_RUN_HLB_QUERY = false;
			return;
		}
		try {
			for (HelibaoCustRecordVO helibaoCustRecordVO : helibaoQueryList) {
				CommonResponse helibaoQueryResult = helibaoCustRecordService.queryHelibaoResult(helibaoCustRecordVO);
				if(helibaoQueryResult.isSuccess() && StatusConstants.SUCCESS_CODE.equals(helibaoQueryResult.getCode())){
					logger.info("合利宝认证查询成功!参数:{}", JSONObject.toJSONString(helibaoCustRecordVO));
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("执行合利宝认证查询异常，异常信息{}，请求参数", e,e.getMessage(),JSONObject.toJSONString(helibaoQueryList));
		}finally{
			LOAN_RUN_HLB_QUERY = false;
		}

	}*/
	
//	//@Scheduled(cron="0 0/5 * * * ?")
//	public void test() {
//		//getHelibaoRecordFail
//		// 获取需要合利宝进行认证的数据
//		List<THelibaoRecord> list =tHelibaoRecordService.getHelibaoRecordFail();
//		for (THelibaoRecord t : list) {
//			// 逐个对数据进行注册 认证等
//			CommonResponse result= 	tHelibaoRecordService.userRegister(t);
//			System.err.println("逐个对数据进行注册 认证等===>>>"+JSONObject.toJSONString(result));
//			
//		}
//		// 查询资质上传成功的数据
//		List<THelibaoRecord> list2 = tHelibaoRecordService.getHelibaoRecordSuccess();
//		System.err.println("查询资质上传成功的数据===》》》"+JSONObject.toJSONString(list2));
//		/*//查询合利宝结果
//		THelibaoRecord vo =new THelibaoRecord();
//		vo.setUserId("");
//		vo.setUserNumber("");
//		tHelibaoRecordService.queryHelibaoResult(vo);*/
//		
//	}
//	
	
	/**
	 * 合利宝用户资质认证 跑批 
	 */
	@Scheduled(cron="${helibao_User_Register}")
	public void helibaoUserRegister() {
		//getHelibaoRecordFail
		// 获取需要合利宝进行认证的数据
		logger.info("跑批开始>>>>");
		try {
			List<THelibaoRecord> list =tHelibaoRecordService.getHelibaoRecordFail();
			for (THelibaoRecord t : list) {
				// 逐个对数据进行注册 认证等
				CommonResponse result= 	tHelibaoRecordService.userRegister(t);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			logger.error("合利宝用户资质认证跑批--系统异常!");
		}
		logger.info("合利宝用户资质认证跑批--跑批更新状态结束<<<");	
		
	}
	/**
	 * 合利宝用户资质认证结果 跑批 
	 */
	@Scheduled(cron="${helibao_User_Success}")
	public void helibaoUserSuccessResult() {
		//getHelibaoRecordFail
		// 获取需要合利宝进行认证的数据
		logger.info("合利宝用户资质认证结果跑批开始>>>>");
		try {
			List<THelibaoRecord> list = tHelibaoRecordService.getHelibaoRecordSuccess();
			for (THelibaoRecord t : list) {
				CommonResponse result = tHelibaoRecordService.queryHelibaoResult(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			logger.error("合利宝用户资质认证结果跑批--系统异常!");
		}
		logger.info("合利宝用户资质认证结果跑批--跑批更新状态结束<<<");	
		
	}
	//跑批处理退款状态为处理中订单
	@Scheduled(cron="${auto_refundLoan}")
	public void refundLoan(){
		logger.info("退款跑批--跑批处理退款状态为处理中订单开始>>>>");
		//防止重复
		if(REFUND_RUN){return;}
		REFUND_RUN = true;
		List<RefundApplyEntity> refundList = orderForGYTServise.queryProcessing();
		logger.info("退款跑批--本次跑批处理退款状态为处理中订单条数为：{}",refundList.size());
		if(refundList == null || refundList.size() == 0){
			logger.info("退款跑批--未查询到退款状态为处理中,退款跑批结束<<<<");
			REFUND_RUN = false;
			return ;
		}
		try {
			for (RefundApplyEntity info : refundList) {
				 try {
					 Boolean status = orderForGYTServise.updateRefundStatus(info);
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
					logger.error("退款跑批系统异常,请求参数:{}",JSONObject.toJSONString(info));
				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			logger.error("退款跑批--系统异常!");
		}finally {
			REFUND_RUN =false;
		}
		logger.info("退款跑批--跑批处理退款状态为处理中订单结束<<<");
	}
	
//	//跑批初始化信审专员出勤记录
//	//@Scheduled(cron="${auto_attendanceLoan}")
//	public void attendanceLoan(){
//		logger.info("初始化出勤记录跑批开始>>>>");
//		//防止重复
//		if(ATTENDANCE_RUN){return;}
//		ATTENDANCE_RUN = true;
//		List<com.zhiwang.zfm.common.response.bean.sys.CreditUserVo> userList = userService.getCreditUser();
//		logger.info("初始化出勤记录跑批--本次跑批处理信审专员人数为：{}",userList.size());
//		if(userList == null || userList.size() == 0){
//			logger.info("初始化出勤记录跑批--未查询到信审专员,退款跑批结束<<<<");
//			ATTENDANCE_RUN = false;
//			return ;
//		}
//		try {
//			try {
//				List<CreditUserVo> voList = JSONArray.parseArray(JSONArray.toJSONString(userList), CreditUserVo.class);
//				zeusForGYTServise.saveAttendance(voList);
//			} catch (Exception e) {
//				logger.error(e.getMessage(),e);
//				logger.error("初始化出勤记录跑批系统异常");
//			}
//		}catch (Exception e) {
//			logger.error(e.getMessage(),e);
//			logger.error("初始化出勤记录跑批--系统异常!");
//		}finally {
//			ATTENDANCE_RUN =false;
//		}
//		logger.info("初始化出勤记录跑批--跑批处理退款状态为处理中订单结束<<<");
//	}
	
	
	public static void main(String[] args) {
		String beforeDay = DateUtils.getBefore(new Date(), 1, DateUtils.STYLE_2);
		System.out.println(beforeDay);
	}
}

