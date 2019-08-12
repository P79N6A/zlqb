package com.nyd.admin.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.admin.model.Info.RefundAppCountRequest;
import com.nyd.admin.service.RefundAppCountService;
import com.nyd.user.api.RefundAppCountContract;
import com.tasfe.framework.support.model.ResponseData;

@Service
public class RefundAppCountServiceImpl implements RefundAppCountService{
    private static Logger logger = LoggerFactory.getLogger(RefundAppCountServiceImpl.class);
	
    @Autowired
    RefundAppCountContract refundAppCountContract;
	
    @Override
	public ResponseData query(RefundAppCountRequest param) {
    	ResponseData re = ResponseData.success();
    	Map<String,Object> map = new HashMap<String,Object>();
		map.put("startDate", param.getStartDate());
		map.put("endDate", param.getEndDate());
		map.put("pageNum", param.getPageNum());
		map.put("pageSize", param.getPageSize());
		map.put("appCode", param.getAppCode());
		try {
			return refundAppCountContract.queryRefundAppCount(map);
		} catch (Exception e) {
			logger.error("查询统计信息失败： " + e.getMessage());
			return re.error("查询详情失败");
		}
	}
}
