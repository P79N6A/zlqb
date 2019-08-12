package com.nyd.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.RefundUserMapper;
import com.nyd.admin.model.AdminRefundUserDto;
import com.nyd.admin.model.RefundUserInfo;
import com.nyd.admin.service.RefundUserService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.tasfe.framework.support.model.ResponseData;
@Service
public class RefundUserServiceImpl implements RefundUserService{
	
	private static Logger logger = LoggerFactory.getLogger(RefundUserServiceImpl.class);
	
	@Autowired
	RefundUserMapper refundUserMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
	@Override
	public ResponseData selectRefundUser(AdminRefundUserDto dto) {
		logger.info("用户列表查询前端传入参数"+JSON.toJSONString(dto));
		ResponseData response = ResponseData.success();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("accountNumber", dto.getAccountNumber());
		map.put("userName", dto.getUserName());		         
        //设置默认分页条件
		Integer  pageNum = dto.getPageNum();
		Integer  pageSize = dto.getPageSize();
        if (pageNum == null || pageNum== 0) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 20;
        }
        Integer total = 0;
        try {
        	total =refundUserMapper.queryRefundUserInfoCount(map);
		} catch (Exception e) {
			logger.info("select refunduser error",e);
		}
        if (total == null) {
            total = 0;
        }
        PageHelper.startPage(pageNum, pageSize);
        logger.info("查询总个数 total is " + total);
        List<RefundUserInfo> refundList = new ArrayList<RefundUserInfo>();
        try {
        	refundList = refundUserMapper.queryRefundUserInfo(map);
		} catch (Exception e) {
			logger.info("select refunduser error",e);
		}
        PageInfo pageInfo = new PageInfo(refundList);
        pageInfo.setTotal(total);
        response.setData(pageInfo);
        logger.info(JSON.toJSONString(response));
		return response;
	}
	

}
