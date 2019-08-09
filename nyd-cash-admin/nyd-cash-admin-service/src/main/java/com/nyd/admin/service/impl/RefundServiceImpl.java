package com.nyd.admin.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.AdminRefundMapper;
import com.nyd.admin.model.AdminRefundInfo;
import com.nyd.admin.service.RefundService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.user.api.RefundContract;
import com.nyd.user.model.RefundInfo;
import com.tasfe.framework.support.model.ResponseData;
/**
 * 
 * @author zhangdk
 *
 */
@Service
public class RefundServiceImpl implements RefundService {
	private static Logger logger = LoggerFactory.getLogger(RefundServiceImpl.class);

	@Autowired
	RefundContract refundContract;
	
	@Autowired
	AdminRefundMapper adminRefundMapper;


	@Override
	public ResponseData audit(RefundInfo req) {
		ResponseData res = ResponseData.success();
		if(StringUtils.isBlank(req.getRefundNo()) || req.getRequestStatus() == null) {
			return res.error("请求参数异常");
		}
		try {
			refundContract.update(req);
		} catch (Exception e) {
			logger.info("更新退款请求失败：" + e.getMessage());
			return res.error("更新失败！");
		}
		return res;

	}

	/*@Override
	public ResponseData query(RefundInfo param) {
		ResponseData re = ResponseData.success();
		try {
			return refundContract.queryRefund(param);
		} catch (Exception e) {
			logger.error("查询退款请求列表失败： " + e.getMessage());
			return re.error("查询列表失败");
		}
	}*/
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
	@Override
	public ResponseData query(AdminRefundInfo param) {
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
			total = adminRefundMapper.queryRefundTotal(param);
		} catch (Exception e) {
			logger.error("查询退款列表个数异常：" + e.getMessage());
		}
        logger.info("查询总个数 total is " + total);
        if (total == null) {
            total = 0;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<AdminRefundInfo> list = null;
        try {
            list = adminRefundMapper.queryRefund(param);
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
	@Override
	public ResponseData getRefundDetail(RefundInfo param) {
		ResponseData re = ResponseData.success();
		try {
			return refundContract.getRefundAppListByRefundNo(param.getRefundNo());
		} catch (Exception e) {
			logger.error("查询退款请求列表失败： " + e.getMessage());
			return re.error("查询列表失败");
		}
	}
}
