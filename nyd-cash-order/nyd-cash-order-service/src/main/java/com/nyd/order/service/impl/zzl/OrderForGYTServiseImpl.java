package com.nyd.order.service.impl.zzl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.order.api.zzl.OrderForGYTServise;
import com.nyd.order.api.zzl.OrderSqlService;
import com.nyd.order.dao.mapper.RefundApplyMapper;
import com.nyd.order.entity.refund.RefundApplyEntity;
import com.nyd.order.model.common.ChkUtil;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.order.model.common.DateUtils;
import com.nyd.order.model.common.PagedResponse;
import com.nyd.order.model.enums.OrderStatus;
import com.nyd.order.model.refund.request.RefundListRequest;
import com.nyd.order.model.refund.vo.RefundFlowVo;
import com.nyd.order.model.refund.vo.RefundListVo;
import com.nyd.order.model.refund.vo.RefundRatioVo;
import com.nyd.order.model.refund.vo.SumAmountVo;
import com.nyd.order.service.util.DateUtil;
import com.nyd.user.api.zzl.UserForZQServise;
import com.nyd.user.model.BankInfo;
import com.nyd.zeus.api.payment.PaymentRiskRecordService;
import com.nyd.zeus.api.zzl.chanpay.ChangJiePaymentService;
import com.nyd.zeus.api.zzl.hnapay.HnaPayPaymentService;
import com.nyd.zeus.api.zzl.liandong.LiandongPayPaymentService;
import com.nyd.zeus.api.zzl.xunlian.XunlianPayService;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieQueryMerchantVO;
import com.nyd.zeus.model.helibao.vo.pay.resp.chanpay.ChangJieDFResp;
import com.nyd.zeus.model.hnapay.req.HnaPayQueryPayReq;
import com.nyd.zeus.model.hnapay.resp.HnaPayQueryPayResp;
import com.nyd.zeus.model.liandong.vo.LiandongQueryChargeVO;
import com.nyd.zeus.model.liandong.vo.resp.LiandongChargeResp;
import com.nyd.zeus.model.xunlian.req.XunlianQueryChargeVO;
import com.nyd.zeus.model.xunlian.resp.XunlianQueryChargeResp;

@Service("orderForGYTServise")
public class OrderForGYTServiseImpl implements OrderForGYTServise{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderForGYTServiseImpl.class);

	@Autowired
    private RefundApplyMapper refundApplyMapper;
	
	@Autowired
    private OrderSqlService orderSqlService;
	
	@Autowired
    private ChangJiePaymentService changJiePaymentService;
	
	@Autowired
	private PaymentRiskRecordService paymentRiskRecordService;
	
	@Autowired
	private UserForZQServise userForZQServise;
    
    @Autowired
	private XunlianPayService xunlianPayService;
    
    @Autowired
	private HnaPayPaymentService hnaPayPaymentService;
    
    @Autowired
    private LiandongPayPaymentService liandongPayPaymentService;
	
	/**
	 * 退款处理列表查询
	 */
	@Override
	public PagedResponse<List<RefundListVo>> refundList(RefundListRequest request) {
		LOGGER.info("退款处理--列表查询开始入参：{}",JSON.toJSONString(request));
		PagedResponse<List<RefundListVo>> response = new PagedResponse<List<RefundListVo>>();
		try {
			List<RefundListVo> list = refundApplyMapper.queryRefundList(request);
			if(!ChkUtil.isEmpty(list)){
				for(RefundListVo vo : list){
					vo.setOrderStatus(!ChkUtil.isEmpty(vo.getOrderStatus())?OrderStatus.getDescription(Integer.parseInt(vo.getOrderStatus())):null);
				}
			}
			Long count = refundApplyMapper.queryRefundListCount(request);
			response.setData(list);
			response.setTotal(count);
			response.setPageNo(request.getPageNo());
			response.setPageSize(request.getPageSize());
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("退款处理--列表查询系统错误：{}",e,e.getMessage());
		}
		return response;
	}
	
	/**
	 * 退款比例查询
	 */
	@Override
	public CommonResponse<RefundRatioVo> refundRatio() {
 		CommonResponse<RefundRatioVo> response = new CommonResponse<>();
		try {
			RefundRatioVo vo = new RefundRatioVo();
			//今日平台收入的批核费总金额，保留小数点后两位小数，单位：元
			String totalIncome = "0.00";
			com.nyd.zeus.model.common.CommonResponse<JSONObject> common= paymentRiskRecordService.allCostMoney();
			JSONObject obj = common.getData();
			if(!ChkUtil.isEmpty(obj) && obj.containsKey("money") && !ChkUtil.isEmpty(obj.getString("money"))){
				totalIncome = obj.getString("money");
			}
			String refunded = refundApplyMapper.queryRefunded();
			vo.setRefundRatio("--");
			if(!ChkUtil.isEmpty(totalIncome) && !ChkUtil.isEmpty(refunded)){
				BigDecimal totalNum = new BigDecimal(totalIncome);
				int i=totalNum.compareTo(BigDecimal.ZERO);
				//今日已退的批核费总金额，保留小数点后两位小数，单位：元
				BigDecimal refundedNum = new BigDecimal(refunded);
				if(i==1){
					BigDecimal refundRatioNum = refundedNum.divide(totalNum,2,BigDecimal.ROUND_HALF_UP);
					vo.setRefundRatio(String.valueOf(refundRatioNum.multiply(new BigDecimal("100"))));
				}
			}
			vo.setTotalIncome(totalIncome);
			vo.setRefunded(refunded);
			response.setData(vo);
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("退款处理--退款比例查询系统错误：{}",e,e.getMessage());
		}
		return response;
	}

	/**
	 * 退款处理记录
	 */
	@Override
	public CommonResponse<List<RefundFlowVo>> queryRefundFlow(String orderNo) {
		LOGGER.info("退款处理记录查询开始入参：{}",JSON.toJSONString(orderNo));
		CommonResponse<List<RefundFlowVo>> response = new CommonResponse<List<RefundFlowVo>>();
		try {
			List<RefundFlowVo> list = refundApplyMapper.queryRefundFlow(orderNo);
			response.setData(list);
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("退款处理记录系统错误：{}",e.getMessage());
		}
		return response;
	}

	/**
	 * 跑批处理退款状态为处理中订单
	 */
	@Override
	public Boolean updateRefundStatus(RefundApplyEntity entity) {
		LOGGER.info("退款跑批--跑批处理退款状态为处理中订单开始.入参：{}",JSON.toJSONString(entity));
		Boolean status = false;
		try {
			BankInfo bankInfo = userForZQServise.getAccountNo(entity.getCust_id());
			if(ChkUtil.isEmpty(bankInfo)){
				LOGGER.error("退款处理--跑批处理退款状态为处理中订单查询绑卡信息为空.入参：{}",JSON.toJSONString(entity));
				return false;
			}
			if(bankInfo.getSoure()==1 && "changjie".equals(bankInfo.getChannelCode())){
				//畅捷
				//1、调用畅捷退款接口
				ChangJieQueryMerchantVO vo = new ChangJieQueryMerchantVO();
				vo.setOrderno(entity.getSerialNum());
				com.nyd.zeus.model.common.CommonResponse<ChangJieDFResp> result = changJiePaymentService
						 .queryPayForAnother(vo);
				LOGGER.info("退款跑批--请求畅捷返回结果：{}",JSON.toJSONString(result));
				if(!ChkUtil.isEmpty(result) && result.isSuccess()){
					ChangJieDFResp cJieDFResp = result.getData();
					if("0000".equals(cJieDFResp.getFinalCode())){
						entity.setStatus("2");
					}else if("0001".equals(cJieDFResp.getFinalCode())){//处理中
						entity.setStatus("1");
					}else{//失败
						entity.setStatus("3");
					}
				}
			}else if(bankInfo.getSoure()==3 && "xunlian".equals(bankInfo.getChannelCode())){
				//迅联
				XunlianQueryChargeVO xunlianVo = new XunlianQueryChargeVO();
				xunlianVo.setOrderId(entity.getSerialNum());//流水号
				com.nyd.zeus.model.common.CommonResponse<XunlianQueryChargeResp> result = xunlianPayService.queryCharge(xunlianVo);
				LOGGER.info("退款跑批--请求迅联返回结果：{}",JSON.toJSONString(result));
				if(!ChkUtil.isEmpty(result) && result.isSuccess()){
					XunlianQueryChargeResp xunlianResp = result.getData();
					if("T".equals(xunlianResp.getQueryResult())){
						if("T".equals(xunlianResp.getRetFlag())){//成功
							entity.setReal_refund_date(DateUtil.dateToString(new Date()));
							entity.setStatus("2");
						}else if("F".equals(xunlianResp.getRetFlag())){//失败
							entity.setStatus("3");
						}else{//处理中
							entity.setStatus("1");
						}
					}
				}
			}else if(bankInfo.getSoure()==4 && "xinsheng".equals(bankInfo.getChannelCode())){
				String submitTime = entity.getSubmitTime();
				if(StringUtils.isBlank(submitTime)){
					return status;
				}
				LOGGER.info("新生提交时间："+submitTime);
				Boolean difFlag = difCurrentTime(submitTime);
				LOGGER.info("新生时间差返boolean："+difFlag);
			   if(difFlag){
				   return status;
			   }
				//新生代付
				HnaPayQueryPayReq payReq = new HnaPayQueryPayReq();
				payReq.setMerOrderId(entity.getSerialNum());
				//原商户订单请求时间 格式：YYYYMMDD
				payReq.setSubmitTime(!ChkUtil.isEmpty(entity.getSubmitTime()) ? entity.getSubmitTime().substring(0,8) : null);
				LOGGER.info("新生代付查询请求参数："+JSONObject.toJSONString(payReq));
				com.nyd.zeus.model.common.CommonResponse<HnaPayQueryPayResp> result = hnaPayPaymentService.queryPay(payReq);
				LOGGER.info("新生代付查询返回参数："+JSONObject.toJSONString(result));
				if(result.isSuccess()){
					HnaPayQueryPayResp payResp = result.getData();
					if("0000".equals(payResp.getResultCode()) && "1".equals(payResp.getOrderStatus())){//成功
							entity.setReal_refund_date(DateUtil.dateToString(new Date()));
							entity.setStatus("2");
					}else if("9999".equals(payResp.getResultCode()) 
							|| ("0000".equals(payResp.getResultCode()) && "0".equals(payResp.getOrderStatus()))){//处理中
						entity.setStatus("1");
					}else{//失败
						entity.setStatus("3");
					}
				}else{//失败
					entity.setStatus("3");
				}
			}else if(bankInfo.getSoure()==5 && "liandong".equals(bankInfo.getChannelCode())){
				//联动退款查询
				LiandongQueryChargeVO liandongQueryChargeVO = new LiandongQueryChargeVO();
				liandongQueryChargeVO.setOrder_id(entity.getSerialNum());
				liandongQueryChargeVO.setMer_date(entity.getReal_refund_date());
				LOGGER.info("联动退款查询请求参数:"+JSONObject.toJSONString(liandongQueryChargeVO));
				com.nyd.zeus.model.common.CommonResponse<LiandongChargeResp> result =liandongPayPaymentService.queryPay(liandongQueryChargeVO);
				LOGGER.info("联动退款查询返回参数:"+JSONObject.toJSONString(result));
				LiandongChargeResp liandongResp = result.getData();
				//0001 处理中   0000请求成功（不代表交易成功）
				String resultCode = liandongResp.getRet_code();
				String resultMsg = liandongResp.getRet_msg();
				//1-支付中3-失败4-成功
				String tradeState = liandongResp.getTrade_state();
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
			refundApplyMapper.updateInfo(entity);
			status = true;
		} catch (Exception e) {
			status = false;
			LOGGER.error("退款处理--跑批处理退款状态为处理中订单系统错误：{}",e,e.getMessage());
			LOGGER.error("退款处理--跑批处理退款状态为处理中订单系统错误.入参：{}",JSON.toJSONString(entity));
		}
		LOGGER.info("退款跑批--跑批处理退款状态为处理中订单结束.入参：{}，处理结果status：{}",JSON.toJSONString(entity),status);
		return status;
	}

	/**
	 * 查询所有处理中退款订单
	 */
	@Override
	public List<RefundApplyEntity> queryProcessing() {
		List<RefundApplyEntity> list = new ArrayList<>();
		try {
			String sql = "select * from t_refund_apply where status = '1'";
			list = orderSqlService.queryT(sql, RefundApplyEntity.class);
			//list = refundApplyMapper.queryProcessing();
		} catch (Exception e) {
			LOGGER.error("退款处理--查询所有处理中退款订单系统错误：{}",e.getMessage());
		}
		return list;
	}

	@Override
	public CommonResponse<SumAmountVo> sumAmount(String orderNo) {
		LOGGER.info("退款处理记录金额合计查询开始入参：{}",orderNo);
		CommonResponse<SumAmountVo> response = new CommonResponse<>();
		try {
			SumAmountVo vo = refundApplyMapper.sumAmount(orderNo);
			response.setData(vo);
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("退款处理记录系统错误：{}",e.getMessage());
		}
		return response;
	}
	
	private  Boolean difCurrentTime(String submitTime){
		submitTime = submitTime.substring(0,14);
		System.out.println(submitTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		try {
			Date submitDate = sdf.parse(submitTime);
			Calendar c1 = Calendar.getInstance();
	        c1.setTime(submitDate);
	        c1.add(Calendar.MINUTE, 60);
	        System.out.println("60分钟之后时间："+sdf.format(c1.getTime()));
	        Date d2 = new Date();
	        Calendar c2 = Calendar.getInstance();
			c2.setTime(d2);
			int dif = c1.compareTo(c2);
			if(dif<0){
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	
	
}
