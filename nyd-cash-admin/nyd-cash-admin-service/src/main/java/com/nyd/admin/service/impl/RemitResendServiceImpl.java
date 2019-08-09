package com.nyd.admin.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.model.Info.OrderExceptionRequst;
import com.nyd.admin.service.RemitResendService;
import com.nyd.capital.api.service.CapitalApi;
import com.nyd.order.api.OrderExceptionContract;
import com.nyd.order.model.OrderExceptionInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
@Service
public class RemitResendServiceImpl implements RemitResendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemitResendServiceImpl.class);
    @Autowired
    private OrderExceptionContract orderExceptionContract;
    @Autowired
    private CapitalApi capitalApi;
	@Override
	public ResponseData audit(OrderExceptionRequst req) {
		if(req.getOrderNos() == null || req.getOrderNos().size() == 0 || req.getAuditStatus() == null) {
			return ResponseData.error("请求参数异常！");
		}
		if(req != null && req.getOrderNos() != null) {
			for(String orderNo : req.getOrderNos()) { 
				try {
					orderExceptionContract.audit(orderNo,req.getAuditStatus(),req.getFundCode());
					if(new Integer(1).equals(req.getAuditStatus())) {
						capitalApi.reSendCapitalAfterRiskByOrderNo(orderNo);
					}
				}catch(Exception e) {
					LOGGER.error("订单审批失败：" + orderNo + e.getMessage());
				}
			}
		}
		return ResponseData.success();
	}
	@Override
	public ResponseData query(OrderExceptionRequst req) throws ParseException{
		ResponseData respData = ResponseData.success();
		Map<String,Object> map = new HashMap<String,Object>();
		if(!StringUtils.isBlank(req.getStartDate())) {
			map.put("startDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(req.getStartDate()));
		}
		if(!StringUtils.isBlank(req.getEndDate())) {
			map.put("endDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(req.getEndDate()));
		}
		map.put("pageNum", req.getPageNum());
		map.put("pageSize", req.getPageSize());
		if(!StringUtils.isBlank(req.getAuditStatu())) {
			map.put("auditStatus", Integer.parseInt(req.getAuditStatu()));
		}
		ResponseData<PageInfo> resp = orderExceptionContract.queryOrderExceptionList(map);
		if(resp != null) {
			return respData.setData(resp.getData());
		}
		return respData.error();
	}
	@Override
	public ResponseData export(Map<String,Object> req) {
		ResponseData respData = ResponseData.success();
		/*Map<String,Object> map = new HashMap<String,Object>();
		map.put("startDate", req.getStartDate());
		map.put("endDate", req.getEndDate());
		map.put("pageNum", req.getPageNum());
		map.put("pageSize", req.getPageSize());
		if(req.getAuditStatu() != null && req.getAuditStatu() != "") {
			map.put("auditStatus", Integer.parseInt(req.getAuditStatu()));
		}*/
		ResponseData<List<OrderExceptionInfo>> resp = orderExceptionContract.queryOrderException(req);
		if(resp != null) {
			return respData.setData(resp.getData());
		}
		return respData.error();
	}

}
