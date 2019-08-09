package com.nyd.admin.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.admin.dao.mapper.DldLoanDataMapper;
import com.nyd.admin.entity.WithholdOrder;
import com.nyd.admin.model.dto.DldCreateBillDto;
import com.nyd.admin.model.dto.DldDeductMoneyDto;
import com.nyd.admin.model.dto.RequestData;
import com.nyd.admin.service.UrgentDeductMoneyService;
import com.nyd.admin.service.utils.AdminProperties;
import com.nyd.admin.service.utils.HttpUtil;
import com.tasfe.framework.support.model.ResponseData;
@Service
public class UrgentDeductMoneyServiceImpl  implements UrgentDeductMoneyService{
	
	private static Logger logger = LoggerFactory.getLogger(UrgentDeductMoneyService.class);

	@Autowired
	private DldLoanDataMapper dldLoanDataMapper;
	
	@Autowired
	private AdminProperties adminProperties;
	
	@Override
	public ResponseData dldDeductMoney() {
	 //得到多来点二次放款的数据
		List<DldCreateBillDto> createList = dldLoanDataMapper.findCollectionData();
		logger.info("查询需要扣款记录数： " + createList.size());
		if(createList != null && createList.size()>0) {
			for(DldCreateBillDto twoLoanData :createList) {
				/*String s = "101540873412816001,101540871307428001,101540869898575001,101540870008407001,101540869888634001,101540864885203001,101540862015408001,101540857873340001,101540833534036001,101540829453928001,101540828408408001,101540828351835001,101540828152657001";
				if(s.indexOf(twoLoanData.getBusinessOrderNo()) >= 0) {
					continue;
				}*/
				twoLoanData.setBusinessOrderType("放款本金");
				/*twoLoanData.setMerchantCode("xqj1");
				twoLoanData.setPayChannelCode("dld");*/
				twoLoanData.setPayType("1");
				RequestData requestData = new RequestData();
	            requestData.setData(twoLoanData);
	            requestData.setRequestAppId("nyd");
	            requestData.setRequestId(UUID.randomUUID().toString());
	            requestData.setRequestTime(JSON.toJSONString(new Date()));
	            String json = JSON.toJSONString(requestData);
	            String url = "http://" + adminProperties.getCommonPayIp() + ":" + adminProperties.getCommonPayPort() + "/common/pay/createOrder";           
	            WithholdOrder withholdOrder = new WithholdOrder();
	            withholdOrder.setMemberId(twoLoanData.getMemberId());
	            withholdOrder.setPayAmount(new BigDecimal(twoLoanData.getPayAmount()));
	            withholdOrder.setUserId(twoLoanData.getUserId());
	            withholdOrder.setAppName("nyd");
	            String payOrderNo = null;
	            try {
	            	 logger.info("调用代扣创建订单接口请求参数"+json);
	            	 String sendPost = HttpUtil.sendPost(url, json);
	            	 JSONObject jsonObject = JSONObject.parseObject(sendPost);
	            	 logger.info("代扣订单创建接口返回结果"+sendPost);
	                 JSONObject data = jsonObject.getJSONObject("data");
	                 payOrderNo = data.getString("payOrderNo");
	            }catch(Exception e) {
	            	  logger.error("调用生成代扣订单接口发生异常,billNo:"+twoLoanData.getBusinessOrderNo());
	            	  withholdOrder.setOrderStatus(1);
	            	  /*try {
		 	            	if(withholdOrder.getMemberId() == null) {
		 	            		withholdOrder.setMemberId(twoLoanData.getBusinessOrderNo());
		 	            	}
		 	            	//dldLoanDataMapper.saveWithholdOrder(withholdOrder);
		 	            }catch(Exception ex) {
		 	            	logger.error("---------代扣信息入库异常------------------");
		 	            	continue;
		 	            }      */              
	            }
			    String url2 = "http://" + adminProperties.getCommonPayIp() + ":" + adminProperties.getCommonPayPort() + "/common/pay/submitWithhold";           
			    DldDeductMoneyDto dldDeductMoneyDto = new DldDeductMoneyDto();
			    dldDeductMoneyDto.setPayOrderNo(payOrderNo);
			    dldDeductMoneyDto.setWithholdAmount(Double.valueOf(twoLoanData.getPayAmount()));
	            requestData.setData(dldDeductMoneyDto);
	            requestData.setRequestAppId("nyd");
	            requestData.setRequestId(UUID.randomUUID().toString());
	            requestData.setRequestTime(JSON.toJSONString(new Date()));
	            String json2 = JSON.toJSONString(requestData);
	            String withholdOrderNo = null;
	            try {
	            	 String sendPost = HttpUtil.sendPost(url2, json2);
	            	 logger.info("调用发起代扣请求参数"+json);
	                 JSONObject jsonObject = JSONObject.parseObject(sendPost);
	                 JSONObject data = jsonObject.getJSONObject("data");
	                 logger.info("发起代扣返回结果"+sendPost);
	                 withholdOrderNo = data.getString("withholdOrderNo");
	                 withholdOrder.setWithholdOrderNo(withholdOrderNo);	            	
	            }catch(Exception e) {
	            	 logger.error("调用发起代扣接口发生异常,billNo:"+twoLoanData.getBusinessOrderNo());
	                 withholdOrder.setOrderStatus(1);
	                 try {
		 	            	if(withholdOrder.getMemberId() == null) {
		 	            		withholdOrder.setMemberId(twoLoanData.getBusinessOrderNo());
		 	            	}
		 	            	//dldLoanDataMapper.saveWithholdOrder(withholdOrder);
		 	            }catch(Exception ex) {
		 	            	logger.error("---------代扣信息入库异常------------------");
		 	            	continue;
		 	            }     
	            }
	            withholdOrder.setOrderStatus(1);
	            logger.info("代扣信息入库：" + JSON.toJSONString(withholdOrder));
	            try {
 	            	if(withholdOrder.getMemberId() == null) {
 	            		withholdOrder.setMemberId(twoLoanData.getBusinessOrderNo());
 	            	}
 	            	//dldLoanDataMapper.saveWithholdOrder(withholdOrder);
 	            }catch(Exception ex) {
 	            	logger.error("---------代扣信息入库异常------------------");
 	            	continue;
 	            }     
			}
		}
		return ResponseData.success();
		
	}

}
