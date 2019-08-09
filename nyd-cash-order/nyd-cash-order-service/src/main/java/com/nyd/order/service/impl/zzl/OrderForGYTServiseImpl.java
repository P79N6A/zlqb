package com.nyd.order.service.impl.zzl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.nyd.zeus.api.zzl.xunlian.XunlianPayService;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieQueryMerchantVO;
import com.nyd.zeus.model.helibao.vo.pay.resp.chanpay.ChangJieDFResp;
import com.nyd.zeus.model.xunlian.req.XunlianChargeVO;
import com.nyd.zeus.model.xunlian.req.XunlianQueryChargeVO;
import com.nyd.zeus.model.xunlian.resp.XunlianChargeResp;
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
			BankInfo bankInfo = userForZQServise.getAccountNo(entity.getUser_id());
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
			list = refundApplyMapper.queryProcessing();
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
	
	

}
