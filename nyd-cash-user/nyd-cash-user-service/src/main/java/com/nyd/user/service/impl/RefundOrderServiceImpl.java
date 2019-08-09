package com.nyd.user.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.nyd.member.api.MemberLogContract;
import com.nyd.member.model.MemberLogModel;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.WithholdOrderInfo;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.mapper.RefundMapper;
import com.nyd.user.dao.mapper.RefundOrderMapper;
import com.nyd.user.dao.mapper.RefundUserMapper;
import com.nyd.user.model.RefundInfo;
import com.nyd.user.model.RefundOrderInfo;
import com.nyd.user.model.dto.WithholdResultDto;
import com.nyd.user.service.RefundOrderService;
import com.nyd.user.service.consts.UserConsts;
import com.nyd.user.service.util.RestTemplateApi;
import com.nyd.user.service.util.UserProperties;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
@Service
public class RefundOrderServiceImpl implements RefundOrderService{
    private static Logger logger = LoggerFactory.getLogger(RefundOrderServiceImpl.class);
	
	@Autowired
	RefundOrderMapper refundOrderMapper;
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private OrderContract orderContract;
	
	@Autowired
	private UserProperties  userProperties;
	
	@Autowired
	private RestTemplateApi restTemplateApi;
	
	@Autowired
	private RefundMapper refundMapper;
	
	@Autowired
	private MemberLogContract memberLogContract;
	
	@Autowired
	private RedisTemplate redisTemplate;
		
	
	public static String KEY ="nyd:refunduser";
	
	@Override
    public ResponseData save(RefundOrderInfo refund) throws Exception{
		ResponseData response = ResponseData.success();
		String userId = refund.getUserId();		
		if(StringUtils.isBlank(userId)) {
			logger.info("uerId为空");
			return response.error("用户信息为空");	
		}
		ResponseData<MemberLogModel>  modelData = memberLogContract.getMemberLogByUserId(userId);
		if("1".equals(modelData.getStatus())) {
			logger.info("调用 ordercontract error");
			return response.error(UserConsts.DB_ERROR_MSG);
		}
		String  memberId = null;
		if("0".equals(modelData.getStatus()) && modelData.getData() != null) {
			  memberId = modelData.getData().getMemberId();	
			  if(StringUtils.isBlank(memberId)) {
				  logger.info("没有代扣记录"+refund.getAccountNumber());
			  }
		}			
	     String payOrderNo = null;			
	     ResponseData<WithholdOrderInfo>  withholdData = orderContract.findWithholdOrderByMemberIdDesc(memberId);
		 if("0".equals(withholdData.getStatus()) && withholdData.getData() != null) {
			 payOrderNo  = withholdData.getData().getPayOrderNo();
		 }else {	
			 return response.error("该用户无缴费记录");
		 }				 
		Map<String, Object> param = new HashMap<String, Object>();
		int [] state = {3};//代扣成功
		param.put("payOrderNo", payOrderNo);
		param.put("state", state);
		String url = userProperties.getQueryAllWithholdOrder();
		redisTemplate.opsForValue().set(KEY+refund.getAccountNumber(), payOrderNo,14*24*60,TimeUnit.MINUTES);
		logger.info("查询代扣评估费记录流水传入参数{},url{}",JSON.toJSONString(param),url);			
		ResponseData resp = restTemplateApi.postForObject(url, param,ResponseData.class);
		logger.info("响应信息:" + JSON.toJSONString(resp));
		List<WithholdResultDto> withholdList = new ArrayList<WithholdResultDto>();
		if(resp != null &&  "0".equals(resp.getStatus())) {
			String json = JSON.toJSONString(resp.getData());
			if(StringUtils.isBlank(json)) {
				return response.error("该用户无缴费记录");
			}
			withholdList = JSON.parseArray(json, WithholdResultDto.class);
		}else {
			return response.error(UserConsts.DB_ERROR_MSG);
		}
		BigDecimal refundAmonut = BigDecimal.ZERO;
		RefundOrderInfo info = new RefundOrderInfo();
		BeanUtils.copyProperties(refund, info);
		String orderNo = "";
		for(WithholdResultDto  withhold :withholdList) {
			if("评估报告".equals(withhold.getBusinessOrderType())) {
				info.setBankName(withhold.getBankName());
				info.setBankAccount(withhold.getBankcardNo());
				info.setBusinessOrderType(withhold.getBusinessOrderType());
				info.setMerchantCode(withhold.getMerchantCode());
				orderNo += withhold.getOrderNo()+ ";"; 
				info.setPayChannelOrder(orderNo);
			}
		}
		info.setRefundStatus(1);
		refundOrderMapper.save(info);
		return resp;
    }

	@Override
	public void sumbitRefundList(RefundInfo refund) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		RefundOrderInfo info  = new RefundOrderInfo();
		info.setRefundNo(refund.getRefundNo());
		List<RefundOrderInfo> list = new ArrayList<RefundOrderInfo>();
		try {
			list =refundOrderMapper.queryRefundOrder(info);
		}catch(Exception e) {		
			logger.error("select refund order error",e);
			return;
		}
		String accountNumber = null;
		if(list != null && list.size() > 0) {
			accountNumber = list.get(0).getAccountNumber();
		}		
		String payOrderNo =(String)redisTemplate.opsForValue().get(KEY+accountNumber);
		List<String> payOrderNoList = new ArrayList<String>();
		payOrderNoList.add(payOrderNo);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("operator", refund.getUpdateBy());
		param.put("operatorTime", sdf.format(new Date()));
		param.put("payOrderNos", payOrderNoList);
		param.put("refundMsg", "审核通过");
		param.put("source", "nyd");
		if(param.get("operator") == null  || payOrderNo == null) {
			logger.info("必传参数为空");
			return;
		}
		String url ="http://"+userProperties.getCommonRefundSubmitIp()+":"+userProperties.getCommonRefundSubmitPort()+"/fmis/pushRefundInfo";
		logger.info("提交退款名单参数{},url{}",JSON.toJSONString(param),url);		
	    try {			    	
	    	ResponseData resp =restTemplateApi.postForObject(url, param,ResponseData.class);
	    	logger.info("公共服务提交退款名单返回结果{}",JSON.toJSON(resp));
	    }catch(Exception e) {
	    	logger.info("调用公共服务提交退款名单服异常",e);
	    }
	    redisTemplate.delete(KEY+accountNumber);
	    
	}
}
