package com.nyd.admin.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.RefundOrderMapper;
import com.nyd.admin.model.RefundOrderInfo;
import com.nyd.admin.service.RefundOrderService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.tasfe.framework.support.model.ResponseData;
/**
 * 
 * @author zhangdk
 *
 */
@Service
public class RefundOrderServiceImpl implements RefundOrderService {
	private static Logger logger = LoggerFactory.getLogger(RefundOrderServiceImpl.class);

	@Autowired
	RefundOrderMapper refundOrderMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
	@Override
	public ResponseData query(RefundOrderInfo param) {
		ResponseData responseData = ResponseData.success();
		Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        //设置默认分页条件
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 20;
        }
        Integer total = 0;
        try {
			total = refundOrderMapper.queryRefundOrderTotal(param);
		} catch (Exception e) {
			logger.error("查询退款列表个数异常：" + e.getMessage());
		}
        logger.info("查询总个数 total is " + total);
        if (total == null) {
            total = 0;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<RefundOrderInfo> list = null;
        try {
            list = refundOrderMapper.queryRefundOrder(param);
        } catch (Exception e) {
        	logger.error("查询退款列表失败：" + e.getMessage());
            responseData = ResponseData.error("查询退款列表失败：" + e.getMessage());
            return responseData;
        }
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setTotal(total);
        responseData.setData(pageInfo);
        return responseData;
	}
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public ResponseData queryExport(RefundOrderInfo param) {
    	ResponseData responseData = ResponseData.success();
    	List<RefundOrderInfo> list = null;
    	try {
    		list = refundOrderMapper.queryRefundOrder(param);
    	} catch (Exception e) {
    		logger.error("查询退款列表失败：" + e.getMessage());
    		responseData = ResponseData.error("查询退款列表失败：" + e.getMessage());
    		return responseData;
    	}
    	responseData.setData(list);
    	return responseData;
    }



	@Override
	public ResponseData save(RefundOrderInfo req) {
		ResponseData responseData = ResponseData.success();
		logger.info("保存退款订单信息：" + JSON.toJSONString(req));
		try {
			refundOrderMapper.save(req);
		} catch (Exception e) {
			logger.error("保存退款订单信息异常：" + e.getMessage());;
		}
		return null;
	}
}
