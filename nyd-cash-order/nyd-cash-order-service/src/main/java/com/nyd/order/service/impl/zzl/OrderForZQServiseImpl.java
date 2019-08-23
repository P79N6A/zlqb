package com.nyd.order.service.impl.zzl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nyd.application.api.AgreeMentContract;
import com.nyd.order.api.zzl.OrderForZQServise;
import com.nyd.order.api.zzl.OrderSqlService;
import com.nyd.order.dao.OrderDao;
import com.nyd.order.dao.OrderDetailDao;
import com.nyd.order.dao.mapper.OrderLoanRecordMapper;
import com.nyd.order.dao.mapper.OrderMapper;
import com.nyd.order.dao.mapper.RefundApplyMapper;
import com.nyd.order.dao.mapper.TExecuteLoanRecordMapper;
import com.nyd.order.dao.mapper.THelibaoRecordMapper;
import com.nyd.order.entity.refund.RefundApplyEntity;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.TExecuteLoanRecord;
import com.nyd.order.model.THelibaoRecord;
import com.nyd.order.model.TOrderLoanRecord;
import com.nyd.order.model.common.ArithUtil;
import com.nyd.order.model.common.ChkUtil;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.order.model.common.PagedResponse;
import com.nyd.order.model.enums.OrderStatus;
import com.nyd.order.model.form.TExecuteLoanRecordSearchForm;
import com.nyd.order.model.form.TOrderLoanRecordSearchForm;
import com.nyd.order.model.refund.request.SaveRefundApplyRequest;
import com.nyd.order.model.vo.AgreementListVO;
import com.nyd.order.model.vo.CenerateContractVo;
import com.nyd.order.model.vo.ConfirmRefunVo;
import com.nyd.order.model.vo.DeductInfoVO;
import com.nyd.order.model.vo.OrderProductInfoVO;
import com.nyd.order.model.vo.OrderlistVo;
import com.nyd.order.model.vo.RefundInfoVO;
import com.nyd.order.model.vo.SaveRefunRecordVo;
import com.nyd.order.service.util.AmountUtil;
import com.nyd.order.service.util.DateUtil;
import com.nyd.product.api.ProductContract;
import com.nyd.user.api.zzl.UserForZQServise;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.PayFee;
import com.nyd.zeus.api.zzl.HelibaoEntrustedLoanService;
import com.nyd.zeus.api.zzl.ZeusForOrderPayBackServise;
import com.nyd.zeus.api.zzl.ZeusForZQServise;
import com.nyd.zeus.api.zzl.chanpay.ChangJiePaymentService;
import com.nyd.zeus.api.zzl.hnapay.HnaPayPaymentService;
import com.nyd.zeus.api.zzl.liandong.LiandongPayPaymentService;
import com.nyd.zeus.api.zzl.xunlian.XunlianPayService;
import com.nyd.zeus.model.BillExtendInfoVo;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.helibao.util.Uuid;
import com.nyd.zeus.model.helibao.util.chanpay.DateUtils;
import com.nyd.zeus.model.helibao.vo.entrustedloan.LoanConInfo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.OrderQueryResVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.OrderQueryVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.OrderResVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.OrderVo;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieMerchantVO;
import com.nyd.zeus.model.helibao.vo.pay.resp.chanpay.ChangJieDFResp;
import com.nyd.zeus.model.hnapay.req.HnaPayPayReq;
import com.nyd.zeus.model.hnapay.resp.HnaPayPayResp;
import com.nyd.zeus.model.liandong.vo.LiandongChargeVO;
import com.nyd.zeus.model.liandong.vo.resp.LiandongChargeResp;
import com.nyd.zeus.model.xunlian.req.XunlianChargeVO;
import com.nyd.zeus.model.xunlian.resp.XunlianChargeResp;

@Service(value ="orderForZQServise")
public class OrderForZQServiseImpl implements OrderForZQServise{
    private static Logger logger = LoggerFactory.getLogger(OrderForZQServiseImpl.class);
    private static final String ORDER_LOAN_KEY = "orderLoanKey";
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private ZeusForZQServise zeusForZQServise;
    
    @Autowired
    private ProductContract productContract;
    
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private OrderDetailDao orderDetailDao;
    
    @Autowired
    private RefundApplyMapper refundApplyMapper;
    
    @Autowired
    private OrderLoanRecordMapper<TOrderLoanRecord> tOrderLoanRecordMapper;
    
    @Autowired
    private TExecuteLoanRecordMapper<TExecuteLoanRecord> tExecuteLoanRecordMapper;
    
    @Autowired
    private OrderSqlService orderSqlService;
    
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Autowired
    private ChangJiePaymentService changJiePaymentService;
    
    @Autowired
    private THelibaoRecordMapper tHelibaoRecordMapper;
    
   
    @Autowired
    private HelibaoEntrustedLoanService helibaoEntrustedLoanService; 
    
    @Autowired
    private AgreeMentContract agreeMentContract;
    
    @Autowired
    private ZeusForOrderPayBackServise zeusForOrderPayBackServise;
    
    @Autowired
	private UserForZQServise userForZQServise;
    
    @Autowired
	private XunlianPayService xunlianPayService;
    
    @Autowired
	private HnaPayPaymentService hnaPayPaymentService;
    
    @Autowired
    private LiandongPayPaymentService liandongPayPaymentService;
    
	@Override
	public PagedResponse<List<OrderlistVo>> getOrderList(OrderlistVo vo) {
		logger.info("根据条件查询订单列表，请求参数：{}",vo);
		PagedResponse<List<OrderlistVo>> pageResponse = new PagedResponse<List<OrderlistVo>>();
		try{
			//校验参数
			if(ChkUtil.isEmptys(vo.getCustName()) && ChkUtil.isEmptys(vo.getMobile()) && ChkUtil.isEmptys(vo.getOrderStatus()) && ChkUtil.isEmptys(vo.getSource())){
				pageResponse.setCode("0");
				pageResponse.setMsg("请输入查询条件");
				pageResponse.setSuccess(false);
				return pageResponse;
			}
			List<OrderlistVo> resultList = orderMapper.getOrderList(vo);
			for (OrderlistVo orderlistVo : resultList) {
//				if(!ChkUtil.isEmptys(orderlistVo.getProcuctName())){
//					//查询产品名称
//					ProductInfo productConfigInfo = productContract.getProductInfo(orderlistVo.getProcuctName()).getData();
//			         if(productConfigInfo == null){
//			        	 orderlistVo.setProcuctName("--");
//			         }else{
//			        	 orderlistVo.setProcuctName(productConfigInfo.getProductName());
//			         }
//				}
				orderlistVo.setOrderStatus(OrderStatus.getDescription(Integer.parseInt(orderlistVo.getOrderStatus())));
			}
			long total = orderMapper.getOrderListCount(vo);
			pageResponse.setData(resultList);
			pageResponse.setTotal(total);
			pageResponse.setSuccess(true);
			pageResponse.setCode("1");
			pageResponse.setMsg("操作成功");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("根据条件查询订单列表异常，请求参数：{}",vo);
			logger.error(e.getMessage());
			pageResponse.setSuccess(false);
			pageResponse.setCode("0");
			pageResponse.setMsg("操作失败");
		}
		return pageResponse;
	}

	@Override
	public CommonResponse<OrderProductInfoVO> getOrderProduct(String orderNo) {
		CommonResponse<OrderProductInfoVO> common = new CommonResponse<OrderProductInfoVO>();
		common.setSuccess(false);
		OrderProductInfoVO vo = new OrderProductInfoVO();
		BillInfo billInfo = new BillInfo();
		OrderInfo orderInfo = new OrderInfo();
//		OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
		//
		try {
			List<OrderInfo> orderinfoList= orderDao.getObjectsByOrderNo(orderNo);
			
			if(orderinfoList.size()<=0){
				common.setCode("0");
				common.setMsg("订单信息不存在!");
				return common;
			}else{
				orderInfo  = orderinfoList.get(0);
			}
			
			//查询
			com.nyd.zeus.model.common.CommonResponse<BillInfo> billCommon = zeusForZQServise.queryBillInfoByOrderNO(orderNo);
			
			billInfo = billCommon.getData();
			
	 		 vo.setOrderNo(orderNo);		//订单编号
	 		 vo.setApplayTime(ChkUtil.isEmpty(orderInfo.getLoanTime())?"":DateUtil.dateToStringHms(orderInfo.getLoanTime()));	//借款时间
	 		 vo.setApplayAmount(ChkUtil.isEmpty(orderInfo.getLoanAmount())?"":ArithUtil.formatDouble(orderInfo.getLoanAmount().doubleValue())); 			//申请金额
	 		 vo.setApplayPeriods(String.valueOf(orderInfo.getBorrowTime()));	//产品期数
	 		 //年利率
	 		 String rate = ChkUtil.isEmpty(orderInfo.getAnnualizedRate())?"":ArithUtil.formatDouble(ArithUtil.mul(orderInfo.getAnnualizedRate().doubleValue(),100))+"%";
	 		 vo.setProductRate(rate+"日");	//产品利率
	 		 vo.setProdcutName(orderInfo.getAppName());
	 		 vo.setPurpose(ChkUtil.isEmpty(orderInfo.getLoanPurpose())?"":orderInfo.getLoanPurpose());		//借款用途
	 		 vo.setLoanTime(ChkUtil.isEmpty(orderInfo.getPayTime())?"":DateUtil.dateToString(orderInfo.getPayTime()));		//放款时间
	 		 vo.setLoanAmount(ChkUtil.isEmpty(orderInfo.getRealLoanAmount())?"":ArithUtil.formatDouble(orderInfo.getRealLoanAmount().doubleValue()));	//借款金额
	 		 // bill表可能没有生成数据，此处用order表的时间
	 		 vo.setRepaymentTime(ChkUtil.isEmpty(orderInfo.getPromiseRepaymentTime())?null:DateUtil.dateToString(orderInfo.getPromiseRepaymentTime()));	//还款日期 
	 		 if(!ChkUtil.isEmpty(billInfo)){
	 			// vo.setRepaymentTime(DateUtil.dateToStringHms(ChkUtil.isEmpty(billInfo.getPromiseRepaymentDate())?null:billInfo.getPromiseRepaymentDate()));	//还款日期 
		 		 if(!ChkUtil.isEmpty(billInfo.getRepayPrinciple())){
		 			 vo.setShouldPrincipal(ChkUtil.isEmpty(billInfo.getRepayPrinciple())?"":ArithUtil.formatDouble(billInfo.getRepayPrinciple().doubleValue()));		//应还本金
		 		 }
		 		 if(!ChkUtil.isEmpty( billInfo.getRepayInterest())){
		 			 vo.setShouldInterest(ArithUtil.formatDouble(billInfo.getShouldInterest().doubleValue()));			//应还利息
		 		 }
		 		 if(!ChkUtil.isEmpty(billInfo.getSyntheticalFee())){
		 			 // TODO Auto-generated method stub
		 			 vo.setShouldServiceFee(ArithUtil.formatDouble(billInfo.getManagerFee().doubleValue()));		//应还服务费
		 		 }
		 		 vo.setSettleTime(ChkUtil.isEmpty(billInfo.getActualSettleDate())?"":DateUtil.dateToString(billInfo.getActualSettleDate())); //结清时间
		 		 
		 		vo.setManagerFee(ChkUtil.isEmpty(billInfo.getManagerFee())?"":ArithUtil.formatDouble(billInfo.getManagerFee().doubleValue()));	//平台管理费
		 		if(null != billInfo.getAlreadyRepayAmount()){   //已还金额
		 			vo.setAlreadyAmount(ArithUtil.formatDouble(billInfo.getAlreadyRepayAmount().doubleValue())); 
		 		}
		 		if(null != billInfo.getWaitRepayAmount()){	//剩余应还
		 			vo.setRemainsAmount(ArithUtil.formatDouble(billInfo.getWaitRepayAmount().doubleValue()));
		 		}
		        
//		 		逾期罚息+滞纳金
		 		double penaltyFee = ChkUtil.isEmpty(billInfo.getPenaltyFee())?0.00:billInfo.getPenaltyFee().doubleValue();
		 		double lateFee = ChkUtil.isEmpty(billInfo.getLateFee())?0.00:billInfo.getLateFee().doubleValue();
		 		String overdueInterest = ArithUtil.formatDouble(ArithUtil.add(penaltyFee, lateFee));	//逾期罚息
		 		vo.setOverdueInterest(overdueInterest);	//逾期费用
	 		 }
	         
	 		//查询
	 		String assessmentAmount = "";	//实收评估费用
	         vo.setAssessmentAmount(assessmentAmount);
	         
	         common.setCode("1");
	         common.setMsg("操作成功");
	         common.setSuccess(true);
	         common.setData(vo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("订单详情-查询产品信息出现异常,请求参数:orderNo:"+orderNo);
			logger.error(e.getMessage());
			common.setCode("0");
			common.setMsg("操作失败");
		}
         return common;
	}

	
	
	@Override
	public PagedResponse<DeductInfoVO> getDeductInfo(String orderNo) {
		PagedResponse<DeductInfoVO> pageResponse = new PagedResponse<DeductInfoVO>();
		DeductInfoVO resultVo = new DeductInfoVO();
		try{
			//查询用户id
			String userIdSql = "select user_Id as userId from t_order where order_no = '"+orderNo+"'";
			String userId = orderSqlService.queryOne(userIdSql).getString("userId");
			//查询列表
			
			String deductListSql="select response_time as deductTime,money as deductAmount,if(`status`='S','成功',if(`status`='F','失败','处理中')) as deductResults from xxd_zeus.t_payment_risk_flow where order_no = '"+orderNo+"'";
			List<JSONObject> deductList = orderSqlService.query(deductListSql);
			//查询总金额
			String deductAmountSql="select SUM(money) as totalAmount from xxd_zeus.t_payment_risk_flow where order_no = '"+orderNo+"'";
			String totalAmount = ChkUtil.isEmpty(orderSqlService.queryOne(deductAmountSql))?"0.00":orderSqlService.queryOne(deductAmountSql).getString("totalAmount");
//			List<DeductInfoList> deductionList  = new ArrayList<DeductInfoVO.DeductInfoList>();
			//转换
//			for (JSONObject jsonObject : deductList) {
//				DeductInfoList deductInfo = new DeductInfoVO.DeductInfoList();
//				deductInfo.setDeductAmount(jsonObject.getString("deductAmount"));
//				deductInfo.setDeductResults(jsonObject.getString("deductResults"));
//				deductInfo.setDeductTime(jsonObject.getString("deductTime"));
//				deductionList.add(deductInfo);
//				
//			}
//			= BeanCommonUtils.copyListProperties(deductList, DeductInfoList.class);
			resultVo.setDeductionList(deductList);
			resultVo.setTotalAmount(totalAmount);
			pageResponse.setCode("1");
			pageResponse.setMsg("成功");
			pageResponse.setSuccess(true);
			pageResponse.setData(resultVo);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("订单详情-查询扣款列表出现异常,请求参数:orderNo:"+orderNo);
			logger.error(e.getMessage());
			pageResponse.setCode("0");
			pageResponse.setMsg("操作失败");
			pageResponse.setSuccess(false);
		}
		return pageResponse;
	}
	
	@Override
	public PagedResponse<RefundInfoVO> getRefundInfo(String orderNo) {
		PagedResponse<RefundInfoVO> pageResponse = new PagedResponse<RefundInfoVO>();
		// TODO Auto-generated method stub
		
		return pageResponse;
	}
	
	@Override
	public PagedResponse<List<AgreementListVO>> getAgreementList(String orderNo) {
		PagedResponse<List<AgreementListVO>> pageResponse = new PagedResponse<List<AgreementListVO>>();
		// TODO Auto-generated method stub
		
		
		return pageResponse;
	}
	
	//新增退款记录
	@Override
	public CommonResponse<Object> saveRefundRecord(SaveRefunRecordVo vo) {
		CommonResponse<Object> common = new CommonResponse<Object>();
		common.setSuccess(false);
		// TODO Auto-generated method stub
		//查询订单详细信息
		OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
		try {
			//根据日期/状态查询申请退款
			Map<String, String> map = new HashMap<String, String>();
			map.put("orderNo", vo.getOrderNo());
			map.put("refundDate", vo.getRefundTime());

			Long applayStatus = refundApplyMapper.queryIsApplay(map);
			if(applayStatus > 0){
				common.setSuccess(true);
				common.setCode("0");
				common.setMsg("已存在待处理的申请记录，请勿重复提交!");
				return common;
			}
//			
			List<OrderDetailInfo> orderDetailList = orderDetailDao.getObjectsByOrderNo(vo.getOrderNo());
			if(orderDetailList.size()<0){
				common.setCode("0");
				common.setMsg("订单信息不存在!");
				return common;
			}else{
				orderDetailInfo  = orderDetailList.get(0);
			}
			
			SaveRefundApplyRequest request = new SaveRefundApplyRequest();
			request.setId(UUID.randomUUID().toString().replace("-", "")+Uuid.getUuid24());
			request.setOrderNo(vo.getOrderNo());
			request.setPhone(orderDetailInfo.getMobile());
			request.setName(orderDetailInfo.getRealName());
			request.setCustId(orderDetailInfo.getUserId());
			request.setUserId(vo.getSysUserId());
			request.setUserName(vo.getSysUserName());
			request.setStatus("0");
			request.setRealRefundAmount("0.00");
			request.setRefundDate(vo.getRefundTime());		//期望退款日期
			request.setRefundAmount(vo.getRefundAmount());  //期望退款金额
			request.setApplyRemarks(vo.getRemark()); 		//备注
			request.setRemarks(vo.getRemark()); 			//备注
			request.setCreateTime(DateUtil.dateToStringHms(new Date()));
			request.setUpdateTime(DateUtil.dateToStringHms(new Date()));
			
			refundApplyMapper.save(request);
			common.setCode("1");
			common.setMsg("操作成功");
			common.setSuccess(true);
		} catch (Exception e) {
			logger.error("新增退款申请出现异常,请求参数:{},异常信息：{},{}",JSONObject.toJSONString(vo),e,e.getMessage());
			common.setCode("0");
			common.setMsg("操作失败");
		}
		return common;
	}
	
	//确认退款
	@Override
	public CommonResponse<Object> refundMoney(ConfirmRefunVo vo) {
		logger.info("退款处理--确认退款请求参数：{}",JSON.toJSONString(vo));
		CommonResponse<Object> common = new CommonResponse<Object>();
		try{
			//step1:拒绝
			if(0==vo.getIsResult()){
				//修改数据
				String updateSuccess = "UPDATE t_refund_apply set `status` = '4',remarks ='"+vo.getRemark()+"',update_time='"+DateUtil.dateToStringHms(new Date())+"' where id = '"+vo.getRefundId()+"'";
				orderSqlService.updateSql(updateSuccess);
				common.setCode("1");
				common.setMsg("操作成功");
				common.setSuccess(true);
				return common;
			}
			//step2:同意
			//step3:查询用户绑定卡号
			BankInfo bankInfo = userForZQServise.getAccountNo(vo.getUserId());
			if(ChkUtil.isEmpty(bankInfo) || ChkUtil.isEmpty(bankInfo.getBankAccount()) || ChkUtil.isEmpty(bankInfo.getChannelCode())){
				common.setCode("0");
				common.setMsg("用户绑卡信息或渠道不存在");
				common.setSuccess(false);
				return common;
			}
			//step4:仅有退款结果=“待处理或退款失败”的申请记录才可以发起【退款】操作
			String refundApplySql="select * from t_refund_apply where id = '"+vo.getRefundId()+"'";
			JSONObject refundApply = orderSqlService.queryOne(refundApplySql);
			if(ChkUtil.isEmpty(refundApply) || !refundApply.containsKey("status") 
					|| (!"0".equals(refundApply.getString("status")) && !"3".equals(refundApply.getString("status")))){
				common.setCode("1");
				common.setMsg("订单非待处理或处理失败状态,无法发起退款！");
				common.setSuccess(true);
				return common;
			}
			//step5：判断代付渠道
			RefundApplyEntity entity = new RefundApplyEntity();
			String serialNum = DateUtil.dateToYmdhms(new Date());
			Random random = new Random();
			int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;
			serialNum = serialNum+rannum;
			if(bankInfo.getSoure()==1 && "changjie".equals(bankInfo.getChannelCode())){
				//畅捷代付
				ChangJieMerchantVO cJieMerchantVO = new ChangJieMerchantVO();
				cJieMerchantVO.setSerialNum(serialNum);//流水号
				cJieMerchantVO.setAccntnm(refundApply.get("name").toString());//用户名
				cJieMerchantVO.setAccntno(bankInfo.getBankAccount());//银行卡号
				cJieMerchantVO.setAmt(vo.getRefundAmount());//金额
				cJieMerchantVO.setMobile(refundApply.get("phone").toString());
				com.nyd.zeus.model.common.CommonResponse<ChangJieDFResp> result = changJiePaymentService
						.payForAnother(cJieMerchantVO);
				logger.info("退款处理--确认退款畅捷返回参数：{}",JSON.toJSONString(result));
				if(!ChkUtil.isEmpty(result) && result.isSuccess()){
					ChangJieDFResp cJieDFResp = result.getData();
					if("0000".equals(cJieDFResp.getFinalCode())){//成功
						entity.setReal_refund_date(DateUtil.dateToString(new Date()));
						entity.setStatus("2");
					}else if("0001".equals(cJieDFResp.getFinalCode())){//处理中
						entity.setStatus("1");
					}else{//失败
						entity.setStatus("3");
					}
				}
			}else if(bankInfo.getSoure()==3 && "xunlian".equals(bankInfo.getChannelCode())){
				//查询用户身份证号
				String sql = "select * from xxd_user.t_user where user_id='"+vo.getUserId()+"'";
				JSONObject userInfo = orderSqlService.queryOne(sql);
				if(ChkUtil.isEmpty(userInfo) || !userInfo.containsKey("id_number") || ChkUtil.isEmpty(userInfo.getString("id_number"))){
					common.setCode("0");
					common.setMsg("用户信息不存在");
					common.setSuccess(false);
					return common;
				}
				//讯联代付
				XunlianChargeVO xunlianVo = new XunlianChargeVO();
				xunlianVo.setOrderId(serialNum);//流水号
				xunlianVo.setAccount(bankInfo.getBankAccount());//卡号
				xunlianVo.setAmount(vo.getRefundAmount());//金额
				xunlianVo.setBankName(bankInfo.getBankName());//银行名称
				xunlianVo.setName(bankInfo.getAccountName());//姓名
				xunlianVo.setIdCode(userInfo.getString("id_number"));//身份证号
				com.nyd.zeus.model.common.CommonResponse<XunlianChargeResp> result = xunlianPayService.charge(xunlianVo);
				logger.info("退款处理--确认退款讯联代付返回参数：{}",JSON.toJSONString(result));
				if(!ChkUtil.isEmpty(result) && result.isSuccess()){
					XunlianChargeResp xunlianResp = result.getData();
					if("T".equals(xunlianResp.getRetFlag())){//成功
						entity.setReal_refund_date(DateUtil.dateToString(new Date()));
						entity.setStatus("2");
					}else if("F".equals(xunlianResp.getRetFlag())){//失败
						entity.setStatus("3");
					}else{//处理中
						entity.setStatus("1");
					}
				}
			}else if(bankInfo.getSoure()==4 && "xinsheng".equals(bankInfo.getChannelCode())){
				//新生代付
				HnaPayPayReq payReq = new HnaPayPayReq();
				payReq.setMerOrderId(serialNum);//流水号
				payReq.setPayeeAccount(bankInfo.getBankAccount());//收款方账户
				payReq.setPayeeName(bankInfo.getAccountName());//收款方姓名
				payReq.setTranAmt(vo.getRefundAmount());//金额
				com.nyd.zeus.model.common.CommonResponse<HnaPayPayResp> result = hnaPayPaymentService.pay(payReq);
				logger.info("退款处理--确认退款新生代付返回参数：{}",JSON.toJSONString(result));
				if(!result.isSuccess()){//失败
					entity.setStatus("3");
				}else{//处理中
					if(!ChkUtil.isEmpty(result.getData())){
						entity.setSubmitTime(result.getData().getSubmitTime());//新生代付返回的提交时间，查询需用到
					}
					entity.setStatus("1");
				}
			}else if(bankInfo.getSoure()==5 && "liandong".equals(bankInfo.getChannelCode())){
				//联动支付
				serialNum = Uuid.getUuid26();
				LiandongChargeVO liandong = new LiandongChargeVO();
				liandong.setAmount(vo.getRefundAmount());
				liandong.setRecv_account(bankInfo.getBankAccount());
				liandong.setRecv_user_name(bankInfo.getAccountName());
				liandong.setOrder_id(serialNum);
				String currentTime = DateUtils.getCurrentTime(DateUtils.STYLE_3);
				liandong.setMer_date(currentTime);
				logger.info("退款处理-联动请求参数："+JSONObject.toJSONString(liandong));
				com.nyd.zeus.model.common.CommonResponse<LiandongChargeResp> result = liandongPayPaymentService.pay(liandong);
				logger.info("退款处理-联动返回参数："+JSONObject.toJSONString(result));
				LiandongChargeResp liandongResp = result.getData();
				//0001 处理中   0000请求成功（不代表交易成功）
				String resultCode = liandongResp.getRet_code();
				String resultMsg = liandongResp.getRet_msg();
				//1-支付中3-失败4-成功
				String tradeState = liandongResp.getTrade_state();
				entity.setReal_refund_date(currentTime);
				if("0001".equals(resultCode)){
					entity.setStatus("1");
				}else if("0000".equals(resultCode)){
					if("4".equals(tradeState)){
						entity.setStatus("2");
					}else if("3".equals(tradeState)){
						entity.setStatus("3");
					}else{
						entity.setStatus("1");
					}
				}else{
					entity.setStatus("3");
				}
				
			}else{
				
			}
			entity.setId(vo.getRefundId());
			entity.setReal_refund_amount(vo.getRefundAmount());
			entity.setRemarks(vo.getRemark());
			entity.setHandle_user_id(vo.getSysUserId());
			entity.setHandle_user_name(vo.getSysUserName());
			entity.setSerialNum(serialNum);
			refundApplyMapper.updateInfo(entity);
			common.setCode("1");
			common.setMsg("操作成功");
			common.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("退款出现异常,请求参数:{}",JSONObject.toJSONString(vo));
			logger.error(e.getMessage());
			common.setCode("0");
			common.setMsg("操作失败");
			common.setSuccess(false);
		}
		return common;
	}
	
	/**
	 * 跑批查询待放款记录
	 * @author 
	 * @return
	 */
	@Override
	public List<TOrderLoanRecord> getOrderLoanRecords() {
		TOrderLoanRecordSearchForm form = new TOrderLoanRecordSearchForm();
		form.setPage(1);
		form.setRows(100);
		form.setStatus(0);
		List<TOrderLoanRecord> list = new ArrayList<TOrderLoanRecord>();
		try {
			list = tOrderLoanRecordMapper.pageData(form);
//			 a = tOrderLoanRecordMapper.sss();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	
	
	
	/**
	 * 进行放款
	 * @author 
	 * VO  提现流水
	 * json 银行卡信息
	 * @return
	 */
	@Override
	public CommonResponse<JSONObject> executeOrderLoan(TOrderLoanRecord vo,JSONObject bankInfo) {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		
		 if (redisTemplate.hasKey(ORDER_LOAN_KEY + vo.getOrderNo())) {
			 logger.info("放款redis有值" + vo.getOrderNo());
			 common.setMsg("放款失败,对同一订单重复放款");
			 return common;
         } else {
             redisTemplate.opsForValue().set(ORDER_LOAN_KEY + vo.getOrderNo(), "1", 70, TimeUnit.SECONDS);
         }
		
		
		String loanRedisStatus = (String) redisTemplate.opsForValue().get("nyd-orderLoanSuccess"+vo.getOrderNo());
		if(!ChkUtil.isEmpty(loanRedisStatus)) {
			logger.error("放款失败,对同一订单重复放款,请求参数:{}",JSONObject.toJSONString(vo));
			common.setMsg("放款失败,对同一订单重复放款");
			return common;
		}
		try {
			//查询订单信息
			List<OrderInfo> orderInfos = orderDao.getObjectsByOrderNo(vo.getOrderNo());
			if(ChkUtil.isEmpty(orderInfos) || orderInfos.size()<=0){
				common.setMsg("订单信息不存在");
				common.setCode("0");
				common.setSuccess(false);
				return common;
			}
			OrderInfo orderInfo = orderInfos.get(0);
			
			if(OrderStatus.WAIT_LOAN.getCode() != orderInfo.getOrderStatus()) {
				logger.error("放款失败,订单不处于放款中,请求参数:{}",JSONObject.toJSONString(vo));
				common.setMsg("放款失败,订单不处于放款中");
				return common;
			}
			double loanAmt = orderInfo.getRealLoanAmount().doubleValue();

			// 插入放款流水记录表
			TExecuteLoanRecord executeLoanRecord = new TExecuteLoanRecord();
			executeLoanRecord.setId(UUID.randomUUID().toString().replace("-", "")+Uuid.getUuid24());
			executeLoanRecord.setOrderId(vo.getOrderNo());
			executeLoanRecord.setCreateTime(new Date());
			
			//提现记录表 修改时间
			vo.setUpdateTime(new Date());
			
			// 调用第三方支付代付
			JSONObject payResult = requestOrderLoanPayService(orderInfo,loanAmt,bankInfo);
			String executeStatus = payResult.getString("executeStatus");
			String retSeriNo = payResult.getString("seriNo");
			String helibaoUserid = payResult.getString("helibaoUserid");
			String helibaoOrderid = payResult.getString("helibaoOrderid");
			String resultMsg = payResult.getString("resultMsg");
			if("W".equals(executeStatus)){
				logger.info("放款失败,合利宝注册或认证失败,等待下次跑批,请求参数:"+JSONObject.toJSONString(vo));
				common.setCode("0");
				common.setMsg("合利宝没有资质认证");
				common.setSuccess(false);
				return common;
			}else{
				if("E".equals(executeStatus)) {
					// 修改放款记录表 放款失败
					vo.setStatus(vo.STATUS_FAILD);
					vo.setRemark(resultMsg);
					// 放款失败修改订单状态为放款失败 
					orderInfo.setOrderStatus(OrderStatus.LOAN_FAIL.getCode());
					orderInfo.setFailType(1);
					orderInfo.setLoanFailTime(new Date());
//					orderInfo.setPayFailReason(payFailReason);
					orderDao.update(orderInfo);

					common.setCode("0");
					common.setMsg("放款失败");
					common.setSuccess(false);
				}else {
					if("S".equals(executeStatus)) {
						vo.setStatus(vo.STATUS_WAITING);
						vo.setRemark(resultMsg);
						tOrderLoanRecordMapper.updateBySelective(vo);
						// 调用生成明细方法
						generateCrmPayControl(vo.getOrderNo(),bankInfo);
						executeLoanRecord.setStatus(vo.STATUS_SUCCESS);
						// 放款成功 把订单数据加入到缓存 
						long liveTime = 60 * 24 * 5;
						redisTemplate.opsForValue().set("nyd-orderLoanSuccess"+vo.getOrderNo(), "1",liveTime);
					}else if("F".equals(executeStatus)){
						executeLoanRecord.setStatus(2);
						vo.setStatus(vo.STATUS_FAILD);
						vo.setRemark(resultMsg);
						// 放款失败修改订单状态为放款失败 
						orderInfo.setOrderStatus(OrderStatus.LOAN_FAIL.getCode());
						orderInfo.setFailType(1);
						orderInfo.setLoanFailTime(new Date());
//						orderInfo.setPayFailReason(payFailReason);
						orderDao.update(orderInfo);
					}else {
						vo.setRemark(resultMsg);
						executeLoanRecord.setStatus(0);
						vo.setStatus(TOrderLoanRecord.STATUS_WAITING);
					}
					// 修改放款流水记录表
					executeLoanRecord.setOutTradeNo(retSeriNo);
					executeLoanRecord.setHelibaoOrderid(helibaoOrderid);
					executeLoanRecord.setHelibaoUserid(helibaoUserid);
					executeLoanRecord.setUserNo(orderInfo.getUserId());
					tExecuteLoanRecordMapper.insert(executeLoanRecord);

					common.setCode("1");
					common.setMsg("放款成功");
					common.setSuccess(true);
				}
				tOrderLoanRecordMapper.updateBySelective(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("放款失败 ：请求参数{}", JSONObject.toJSONString(vo));
			common.setCode("0");
			common.setMsg("放款失败");
			common.setSuccess(false);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return common;
	}
	
	
	private JSONObject requestOrderLoanPayService(OrderInfo orderInfo,double loanAmt,JSONObject bankInfo) throws Exception{
		JSONObject ret = new JSONObject();
		// 交易状态
		String executeStatus = "";
		// 交易流水号,第三方返回,后期查询使用
		String retSeriNo = "";
		//
		String helibaoUserid = "";
		String helibaoOrderid = "";
		String resultMsg = "";
		// 合利宝
		//查询是否合利宝注册
		THelibaoRecord helibaoCustRecord = tHelibaoRecordMapper.findByUserId(orderInfo.getUserId());
		if(ChkUtil.isEmpty(helibaoCustRecord) || 5 != helibaoCustRecord.getStatus()) {
			// 
			executeStatus = "W";
			logger.error("用户进入放款时未完成用户资质注册，用户ID：{}，订单ID：{}",new Object[]{orderInfo.getUserId(),orderInfo.getOrderNo()});
		}else {
			// 借款信息
			LoanConInfo loanConInfo = new LoanConInfo();
			// 调用代付
			OrderVo orderVo = new OrderVo();
			orderVo.setP4_userId(helibaoCustRecord.getUserNumber());
			orderVo.setP9_bankAccountName(bankInfo.getString("accountName"));
			orderVo.setP10_bankAccountNo(bankInfo.getString("cardNumber"));
			orderVo.setP11_legalPersonID(bankInfo.getString("accountIc"));
			orderVo.setP12_mobile(bankInfo.getString("accountMobile"));
			orderVo.setP17_bankCode(bankInfo.getString("bankCode"));
			orderVo.setP20_purpose("消费");
			
			loanConInfo.setLoanTime(String.valueOf(orderInfo.getBorrowTime()));
			loanConInfo.setLoanTimeUnit("D");
			loanConInfo.setLoanInterestRate(ArithUtil.formatDouble(ArithUtil.mul(ArithUtil.mul(orderInfo.getAnnualizedRate().doubleValue(),100),365)));//利率
			loanConInfo.setPeriodization("1");//分期数
			loanConInfo.setPeriodizationDays(String.valueOf(orderInfo.getBorrowTime()));  //期数
			// （借款金额+借款金额×借款年利率÷365×借款天数）÷分期数
			double fee = 0d;
			orderVo.setP7_amount(ArithUtil.formatDouble(loanAmt));
//			// 
			fee = ArithUtil.add(loanAmt,ArithUtil.mul(ArithUtil.mul(orderInfo.getAnnualizedRate().doubleValue()
					,orderInfo.getBorrowTime()),Double.parseDouble(orderVo.getP7_amount())));
			
			loanConInfo.setPeriodizationFee(ArithUtil.formatDouble(fee));
			orderVo.setP21_loanConInfo(JSONObject.toJSONString(loanConInfo, SerializerFeature.WriteMapNullValue,
                      SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty));
			logger.info("请求合利宝代付接口,请求参数:"+JSONObject.toJSONString(orderVo));
			OrderResVo resultVo = helibaoEntrustedLoanService.createOrder(orderVo);
			logger.info("合利宝代付接口返回,返回参数:"+JSONObject.toJSONString(resultVo));
			if("0000".equals(resultVo.getRt2_retCode())){
				if("SUCCESS".equals(resultVo.getRt8_orderStatus())){
					executeStatus="S";
					retSeriNo = resultVo.getRt7_serialNumber();
					helibaoUserid = resultVo.getRt6_userId();
					helibaoOrderid = resultVo.getRt5_orderId();
				}else if("FAIL".equals(resultVo.getRt8_orderStatus())){
					executeStatus = "F";
					resultMsg = resultVo.getRt3_retMsg();
					logger.error("调用合利宝代付失败（放款操作）,请求参数{}", JSONObject.toJSONString(orderVo));
				}else{
					executeStatus = "I";
					retSeriNo = resultVo.getRt7_serialNumber();
					helibaoUserid = resultVo.getRt6_userId();
					helibaoOrderid = resultVo.getRt5_orderId();
				}
			}else{
				resultMsg = resultVo.getRt3_retMsg();
				executeStatus = "E";
				logger.error("调用合利宝代付失败（放款操作）,请求参数{}", JSONObject.toJSONString(orderVo));
			}
		}
		ret.put("executeStatus", executeStatus);
		ret.put("seriNo", retSeriNo);
		ret.put("helibaoUserid", helibaoUserid);
		ret.put("helibaoOrderid", helibaoOrderid);
		ret.put("resultMsg", resultMsg);
		return ret;
	}

	
	/*	 
 	 * 放款成功生成还款明细
	 * @param orderId
	 * @return
	 */
	@Override
	public CommonResponse<JSONObject> generateCrmPayControl(String orderNo,JSONObject bankInfo) throws Exception{
		logger.info("执行放款生成明细,请求参数:{}"+orderNo);
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try{
			//查询订单信息
			List<OrderInfo> orderInfos = orderDao.getObjectsByOrderNo(orderNo);
			if(ChkUtil.isEmpty(orderInfos) || orderInfos.size()<=0){
				common.setMsg("订单信息不存在");
				common.setCode("0");
				common.setSuccess(false);
				return common;
			}
			OrderInfo orderInfo = orderInfos.get(0);		
			if(OrderStatus.WAIT_LOAN.getCode() != orderInfo.getOrderStatus()) {
				logger.error("放款成功生成还款明细,订单不处于放款中,请求参数,orderNo:"+orderNo);
				common.setMsg("放款失败,订单不处于放款中");
				common.setCode("0");
				return common;
			}
			
			// 放款流水 
			TOrderLoanRecordSearchForm orderLoanRecordSearchForm = new TOrderLoanRecordSearchForm();
			orderLoanRecordSearchForm.setOrderNo(orderNo);
			orderLoanRecordSearchForm.setStatus(3);
			List<TOrderLoanRecord> listOrderLoanRecord = tOrderLoanRecordMapper.pageData(orderLoanRecordSearchForm);
			TOrderLoanRecord loanRecord = listOrderLoanRecord.get(0);
			
			// 修改放款记录表放款成功
			
			loanRecord.setStatus(1);
			tOrderLoanRecordMapper.updateBySelective(loanRecord);
			
			// 放款流水
			TExecuteLoanRecordSearchForm executeLoanRecordSearchForm = new TExecuteLoanRecordSearchForm();
			executeLoanRecordSearchForm.setOrderId(orderNo);
			executeLoanRecordSearchForm.setStatus(0);
			List<TExecuteLoanRecord> orderExecuteLoanRecordList = tExecuteLoanRecordMapper.pageData(executeLoanRecordSearchForm);
			
			if(orderExecuteLoanRecordList.size()>0){
				TExecuteLoanRecord executeLoanRecord = new TExecuteLoanRecord();
				executeLoanRecord.setId(orderExecuteLoanRecordList.get(0).getId());
				executeLoanRecord.setStatus(1);
				executeLoanRecord.setUpdateTime(new Date());
				tExecuteLoanRecordMapper.updateBySelective(executeLoanRecord);
			}
			
			//还款计划bean
			BillInfo billInfo = getbillInfo(orderInfo.getUserId(),orderInfo.getOrderNo(),orderInfo.getRealLoanAmount());
			
			// 修改订单状态为还款中
			orderInfo.setBankAccount(bankInfo.getString("cardNumber"));		//'打款银行卡号
			orderInfo.setOrderStatus(OrderStatus.LOAN_SUCCESS.getCode());
			orderInfo.setPayTime(new Date()); 			//放款时间
			orderInfo.setPromiseRepaymentTime(billInfo.getPromiseRepaymentDate());
//			orderInfo.setRealLoanAmount(billInfo.getCurRepayAmount());
			orderInfo.setManagerFee(billInfo.getManagerFee());
			orderInfo.setLoanNumber(orderInfo.getLoanNumber()+1);
			orderDao.update(orderInfo);
			 
			zeusForZQServise.saveBill(billInfo);
			
			BillExtendInfoVo vo = new BillExtendInfoVo();
			vo.setUserName(bankInfo.getString("accountName"));
			vo.setUserMobile(bankInfo.getString("accountMobile"));
			vo.setUserIc(bankInfo.getString("accountIc"));
			vo.setOrderNo(orderNo);
			vo.setLoanNum(orderInfo.getLoanNumber());
			vo.setCreditTrialUserId(orderInfo.getReviewedId());
			vo.setCreditTrialUserName(orderInfo.getReviewedName());
			vo.setCreateTime(new Date());
			vo.setApplyTime(orderInfo.getLoanTime());
			vo.setLoanTime(new Date());
			//查询source
			String orderDetailSql = "select source from t_order_detail where order_no = '"+orderInfo.getOrderNo()+"'";
			JSONObject orderDetailJson = orderSqlService.queryOne(orderDetailSql);
			String source = ChkUtil.isEmpty(orderDetailJson)?"":orderDetailJson.getString("source");
			vo.setSource(source);
			zeusForOrderPayBackServise.saveBillExtendInfo(vo);
			
//			Double rate = ArithUtil.mul(ArithUtil.mul(orderInfo.getAnnualizedRate().doubleValue(),100),365);
			
			Double rate = ArithUtil.mul(orderInfo.getAnnualizedRate().doubleValue(),100);
			
			
			JSONObject userDetailJson = userForZQServise.findUserDetailByUserId(orderInfo.getUserId());
			
			// 生成合同 
			CenerateContractVo contractVo = new CenerateContractVo();
			contractVo.setBankAccount(bankInfo.getString("cardNumber"));
			contractVo.setAddress(userDetailJson.getString("id_address"));
			contractVo.setBankCode(bankInfo.getString("bankCode"));
			contractVo.setLoanInterst(String.valueOf(rate));  //日利息
			contractVo.setLoanRate(billInfo.getCurPeriod().toString());		//期限
			contractVo.setLoanDay(DateUtil.format(new Date(), "yyyy-MM-dd"));
			contractVo.setLoanMoney(orderInfo.getRealLoanAmount().toString());		//本金
			contractVo.setUserName(bankInfo.getString("accountName"));
			contractVo.setMobile(bankInfo.getString("accountMobile"));
			contractVo.setIdNumber(bankInfo.getString("accountIc"));
			contractVo.setUserId(bankInfo.getString("userId"));
			contractVo.setOrderId(orderNo);
			contractVo.setLoanUse(orderInfo.getLoanPurpose());  //借款用途
			contractVo.setLoanRate2(ArithUtil.formatDouble(ArithUtil.mul(billInfo.getPenaltyRate().doubleValue(),100)));  //逾期罚息
			contractVo.setFee1(billInfo.getManagerFee().toString());//服务费
			contractVo.setFee2(AmountUtil.wordsAmount(billInfo.getManagerFee().toString()));
			//查询批核服务费
			String payFeeSql = "select * from xxd_user.t_pay_fee limit 1";
			List<PayFee> payFees = orderSqlService.queryT(payFeeSql,PayFee.class);
			if(payFees.size()>0){
				contractVo.setFee(payFees.get(0).getRealFee().toString());
			}else{
				contractVo.setFee("198");
			}
			logger.info("放款生成合同,请求参数:{}",JSONObject.toJSONString(contractVo));
			generateContract(contractVo);
			
			common.setCode("1");
			common.setMsg("放款成功");
			common.setSuccess(true);
			
			logger.info("结束放款生成明细,请求参数:{}",orderNo);
			return common;
	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("(放款)生成还款明细失败 ,请求参数{}", orderNo);
			common.setCode("0");
			common.setMsg("失败");
			common.setSuccess(false);
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new RuntimeException();
		}
	} 
	
	/**
	 * 封装还款流水表
	 * @return
	 * @throws Exception 
	 */
	private BillInfo getbillInfo(String userId,String orderNo,BigDecimal realLoanAmount) throws Exception{
		com.nyd.zeus.model.common.CommonResponse<JSONObject> billProduct = zeusForZQServise.getBillProduct(orderNo);
		JSONObject billProJson = billProduct.getData();
		 
		 Integer periods = billProJson.getInteger("period");	//期数
//		 double repayPrinciple = billProJson.getDoubleValue("money");  //本金
		 double repayPrinciple = realLoanAmount.doubleValue();
		 double repayInterest =billProJson.getDoubleValue("repay_interest");	//利息
		 double managerFee = billProJson.getDoubleValue("manager_fee");	//平台服务费
		 double lateFee = billProJson.getDoubleValue("late_fee");	//滞纳金
		 double penaltyRate = billProJson.getDoubleValue("penalty_rate");	//罚息率
		 
		 //利息  本金*利率*期限
		 double shouldInterest =ArithUtil.getInteg(ArithUtil.mul( ArithUtil.mul(repayPrinciple, repayInterest),periods));
		 
//		 本金 +本金*利息*期限+平台服务费（后期）+ 本金*罚息*当前逾期天数
//		 double curRepayAmount =ArithUtil.mul(Double.valueOf(periods), ArithUtil.mul(repayPrinciple, repayInterest));
//		 double aaa = ArithUtil.mul(ArithUtil.mul(repayPrinciple, penaltyRate),0);
//		 double curRepayAmount = ArithUtil.add(ArithUtil.add(ArithUtil.add(repayPrinciple,managerFee),
//				 ArithUtil.mul(Double.valueOf(periods), ArithUtil.mul(repayPrinciple, repayInterest))),
//				 ArithUtil.mul(ArithUtil.mul(repayPrinciple, penaltyRate),0));
		 
		 double curRepayAmount = ArithUtil.add(ArithUtil.add(repayPrinciple, shouldInterest),managerFee);
		 
		 //bill编号
		 BillInfo billInfo = new BillInfo();
		 billInfo.setUserId(userId);
		 billInfo.setOrderNo(orderNo);
		 Date payTime = DateUtil.addDay(billProJson.getInteger("period") - 1);
		 billInfo.setPromiseRepaymentDate(payTime);//还款日
		 billInfo.setPeriods(1);		//	期数
		 billInfo.setCurPeriod(periods);
		 billInfo.setCurRepayAmount(new BigDecimal(curRepayAmount));	//应还总金额
		 billInfo.setRepayPrinciple(new BigDecimal(repayPrinciple));  //应还本金
		 billInfo.setRepayInterest(new BigDecimal(billProJson.getString("repay_interest"))); 	//应还利息
		 billInfo.setReceivableAmount(new BigDecimal(curRepayAmount)); 		//应实收金额
		 billInfo.setWaitRepayPrinciple(new BigDecimal(repayPrinciple)); 	//剩余还款本金
		 billInfo.setWaitRepayAmount(new BigDecimal(curRepayAmount));		//剩余应还总金额
		 billInfo.setAlreadyRepayAmount(new BigDecimal("0.00"));		//已还金额
		 billInfo.setSyntheticalFee(new BigDecimal("0.00"));			//综合费用
		 billInfo.setCouponDerateAmount(new BigDecimal("0.00"));		//优惠券减免金额
		 billInfo.setFastAuditFee(new BigDecimal("0.00"));				//快速信审费
		 billInfo.setAccountManageFee(new BigDecimal("0.00"));			//账户管理费
		 billInfo.setIdentityVerifyFee(new BigDecimal("0.00"));			//身份验证费
		 billInfo.setMobileVerifyFee(new BigDecimal("0.00"));			//手机验证费
		 billInfo.setBankVerifyFee(new BigDecimal("0.00"));				//银行卡验证费
		 billInfo.setCreditServiceFee(new BigDecimal("0.00"));			//征信验证费
		 billInfo.setInformationPushFee(new BigDecimal("0.00"));		//信息发布费
		 billInfo.setSlidingFee(new BigDecimal("0.00"));				//浮动服务费
		 billInfo.setTestStatus(1);
		 billInfo.setManagerFee(new BigDecimal(managerFee));		//平台管理费
		 billInfo.setPenaltyRate(new BigDecimal(penaltyRate)); 			//罚息率
		 billInfo.setLateFee(new BigDecimal("0.00"));				//滞纳金
		 billInfo.setOverdueDays(0); 								//逾期天数
		 billInfo.setPenaltyFee(new BigDecimal("0.00")); 			//罚息费
		 billInfo.setShouldInterest(new BigDecimal(shouldInterest));	//应还利息
		 return billInfo;
	}
	
	@Override
	public CommonResponse<Object> generateContract(CenerateContractVo vo){
		CommonResponse<Object> common = new CommonResponse<Object>();
		// 生成合同 
	    try {
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("userId",vo.getUserId());
	        jsonObject.put("orderId",vo.getOrderId());
	        jsonObject.put("idNumber",vo.getIdNumber());
	        jsonObject.put("orderNo",vo.getOrderId());
	        jsonObject.put("mobile",vo.getMobile());
	        jsonObject.put("userName",vo.getUserName());
	        jsonObject.put("loanMoney",vo.getLoanMoney());
	        jsonObject.put("loanDay",vo.getLoanDay());
	        jsonObject.put("loanRate",vo.getLoanRate());
	        jsonObject.put("loanInterst",vo.getLoanInterst());
	        jsonObject.put("bankName",vo.getBankCode());
	        jsonObject.put("bankAccount",vo.getBankAccount());
            jsonObject.put("loanUse",vo.getLoanUse());
	        jsonObject.put("loanRate2",vo.getLoanRate2());
	        jsonObject.put("fee1",vo.getFee1());
	        jsonObject.put("fee2",vo.getFee2());
	        jsonObject.put("fee", vo.getFee());
	        jsonObject.put("address", vo.getAddress());
	        agreeMentContract.signConductLoan(jsonObject);
	    } catch (Exception e) {
	        logger.error(" ",e);
	    }
	    return common;
	}
	
	
	/**
	 * 查询放款流水表中的放款处理中的订单
	 * @author 
	 */
	@Override
	public List<TExecuteLoanRecord> getOrderExecuteLoanRecord() {
		TExecuteLoanRecordSearchForm form = new TExecuteLoanRecordSearchForm();
		form.setPage(1);
		form.setRows(100);
		form.setStatus(0);
		List<TExecuteLoanRecord> list;
		try {
			list = tExecuteLoanRecordMapper.pageData(form);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	
	/**
	 * 
	* 查询放款最终结果
	* @param oderExecuteLoanRecordVO
	* @return
	* @throws Exception 异常
	 */
	@Override
	public CommonResponse<JSONObject> queryTransactionResult(TExecuteLoanRecord vo,JSONObject bankInfo) throws Exception{
		logger.info("开始执行放款跑批查询交易状态,请求参数:{}",JSONObject.toJSONString(vo));
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try {
			//查询订单信息
			List<OrderInfo> orderInfos = orderDao.getObjectsByOrderNo(vo.getOrderId());
			if(ChkUtil.isEmpty(orderInfos) || orderInfos.size()<=0){
				common.setMsg("订单信息不存在");
				common.setCode("0");
				common.setSuccess(false);
				return common;
			}
			OrderInfo orderInfo = orderInfos.get(0);
			JSONObject returnJson = queryPayResult(vo,orderInfo);
			String executeStatus = returnJson.getString("executeStatus");
//			executeStatus = "I";
			// 根据第三方返回查询状态来执行
			if("S".equals(executeStatus)){
				generateCrmPayControl(vo.getOrderId(),bankInfo);
			}else if("F".equals(executeStatus)){
				orderInfo.setOrderStatus(OrderStatus.LOAN_FAIL.getCode());
				orderDao.update(orderInfo);
				
				//修改放款记录表放款失败
				TOrderLoanRecordSearchForm loanRecordSearchForm = new TOrderLoanRecordSearchForm();
				loanRecordSearchForm.setOrderNo(vo.getOrderId());
				List<TOrderLoanRecord> orderLoanRecords = tOrderLoanRecordMapper.pageData(loanRecordSearchForm);
				TOrderLoanRecord loanRecord = new TOrderLoanRecord();
				loanRecord.setId(orderLoanRecords.get(0).getId());
				loanRecord.setStatus(2);
				loanRecord.setUpdateTime(new Date());
				tOrderLoanRecordMapper.updateBySelective(loanRecord);
				
				//修改还款记录流水表
				TExecuteLoanRecordSearchForm executeLoanRecordSearchForm = new TExecuteLoanRecordSearchForm();
				executeLoanRecordSearchForm.setOrderId(vo.getOrderId());
				executeLoanRecordSearchForm.setStatus(0);
				List<TExecuteLoanRecord> executeLoanRecords = tExecuteLoanRecordMapper.pageData(executeLoanRecordSearchForm);
				if(executeLoanRecords != null && executeLoanRecords.size() > 0 && executeLoanRecords.get(0) != null) {
					TExecuteLoanRecord executeLoanRecord =executeLoanRecords.get(0);
					executeLoanRecord.setStatus(2);
					executeLoanRecord.setUpdateTime(new Date());
					tExecuteLoanRecordMapper.updateBySelective(executeLoanRecord);
				}
				logger.error("放款失败，请求参数{}",new Object[] {vo });

				common.setCode("1");
				common.setMsg("放款失败");
				common.setSuccess(false);
				return common;
			}
		} catch (Exception e) {
			logger.error("查询交易流水失败,请求参数:{}",new Object[] {vo });
			logger.error(e.getMessage(), e);
			common.setCode("1");
			common.setMsg("放款失败");
			common.setSuccess(false);
			throw new RuntimeException();
			
		}
		return common;
	}
	
	
	private JSONObject queryPayResult(TExecuteLoanRecord vo,OrderInfo orderInfo) {
		JSONObject ret = new JSONObject();
		String executeStatus = "";

		OrderQueryVo orderVo = new OrderQueryVo();
		orderVo.setP3_orderId(vo.getHelibaoOrderid());
		orderVo.setP4_userId(vo.getHelibaoUserid());
		logger.info("放款跑批查询交易状态,请求参数:{}",JSONObject.toJSONString(orderVo));
		OrderQueryResVo resultVo = helibaoEntrustedLoanService.orderQuery(orderVo);
		logger.info("放款跑批查询交易状态,返回参数:{}",JSONObject.toJSONString(resultVo));
		if("SUCCESS".equals(resultVo.getRt8_orderStatus())){
			executeStatus="S";
		}else if("FAIL".equals(resultVo.getRt8_orderStatus())){
			executeStatus = "F";
		}else{
			executeStatus = "I";
			logger.info("放款跑批查询交易状态，流水号[{}]交易处理中。",vo.getOutTradeNo());
		}
			
		ret.put("executeStatus", executeStatus);
		return ret;
	}
	
	
	
}
