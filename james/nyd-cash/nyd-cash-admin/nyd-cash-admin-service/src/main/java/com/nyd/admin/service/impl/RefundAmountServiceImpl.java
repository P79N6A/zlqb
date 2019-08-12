package com.nyd.admin.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.AdminRefundAmountMapper;
import com.nyd.admin.model.AdminRefundAmountInfo;
import com.nyd.admin.model.AdminRefundInfo;
import com.nyd.admin.service.RefundAmountService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.user.api.RefundAmountContract;
import com.nyd.user.model.RefundAmountInfo;
import com.tasfe.framework.support.model.ResponseData;

@Service
public class RefundAmountServiceImpl implements RefundAmountService{
    private static Logger logger = LoggerFactory.getLogger(RefundAmountServiceImpl.class);
	
	@Autowired
	RefundAmountContract refundAmountContract;
	
	@Autowired
	AdminRefundAmountMapper adminRefundAmountMapper;
	
	@Override
	public ResponseData save(RefundAmountInfo req) {
		ResponseData res = ResponseData.success();
		if(req.getMaxAmount() == null || req.getMinAmount() == null || req.getRegisterCount() == null || req.getRegisterCount().compareTo(0) == 0) {
			return res.error("请求参数异常");
		}
		if(!this.queryAndCheckAmount(null,req.getMinAmount(),req.getMaxAmount())) {
			return res.error("金额校验未通过");
		}
		try {
			refundAmountContract.save(req);
		}catch(Exception e) {
			logger.info("保存退款金额信息失败：" + e.getMessage());
			return res.error("保存失败！");
		}
		return res;
	}
	@Override
	public ResponseData update(RefundAmountInfo req) {
		ResponseData res = ResponseData.success();
		if(StringUtils.isBlank(req.getAmountCode())) {
			return res.error("请求参数异常");
		}
		if(req.getMaxAmount() != null && !this.queryAndCheckAmount(req.getAmountCode(),req.getMinAmount(),req.getMaxAmount())) {
			return res.error("金额校验未通过");
		}
		try {
			refundAmountContract.update(req);
		}catch(Exception e) {
			logger.info("更新退款金额信息失败：" + e.getMessage());
			return res.error("更新失败！");
		}
		return res;
		
	}
	/*@Override
	public ResponseData query(RefundAmountInfo req) {
		ResponseData res = ResponseData.success();
		try {
			return refundAmountContract.queryRefundAmount(req);
		}catch(Exception e) {
			logger.info("查询退款金额信息失败：" + e.getMessage());
			return res.error("查询失败！");
		}
	}*/
	
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
	@Override
	public ResponseData query(AdminRefundAmountInfo req) {
		ResponseData responseData = ResponseData.success();
		Integer pageNum = req.getPageNum();
        Integer pageSize = req.getPageSize();
        //设置默认分页条件
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 20;
        }
        Integer total = 0;
        try {
			total = adminRefundAmountMapper.queryRefundAmountTotal(req);
		} catch (Exception e) {
			logger.error("查询退款列表个数异常：" + e.getMessage());
		}
        logger.info("查询总个数 total is " + total);
        if (total == null) {
            total = 0;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<AdminRefundAmountInfo> list = null;
        try {
            list = adminRefundAmountMapper.queryRefundAmount(req);
            if(list != null && list.size() > 0) {
            	for(AdminRefundAmountInfo info:list) {
            		info.setAmount(info.getMinAmount() + "-" + info.getMaxAmount());
            	}
            }
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
    
    /**
     * 判断是否已存在金额区间
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public boolean queryAndCheckAmount(String amountCode,BigDecimal minAmount,BigDecimal maxAmount) {
    	if(minAmount.compareTo(maxAmount) > 0 ) {
    		return false;
    	}
    	AdminRefundAmountInfo req = new AdminRefundAmountInfo();
    	//req.setAmountStatus(0);
    	List<AdminRefundAmountInfo> list = adminRefundAmountMapper.queryRefundAmount(req);
    	if(list == null || list.size() == 0) {
    		return true;
    	}
    	for (AdminRefundAmountInfo info: list) {
    		if(!StringUtils.isBlank(amountCode)) {
    			if(amountCode.equals(info.getAmountCode())) {
    				continue;
    			}
    		}
    		if(info.getMaxAmount().compareTo(info.getMinAmount()) == 0) {
    			if(info.getMaxAmount().compareTo(minAmount) >= 0 && info.getMinAmount().compareTo(maxAmount) <= 0) {
    				return false;
    			}
    		}
			if(minAmount.compareTo(info.getMinAmount()) >= 0 && minAmount.compareTo(info.getMaxAmount()) <= 0) {
				return false;
			}
			if(maxAmount.compareTo(info.getMinAmount()) >= 0 && maxAmount.compareTo(info.getMaxAmount()) <= 0) {
				return false;
			}
    		
    	}
    	return true;
    }
}
