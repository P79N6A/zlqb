package com.nyd.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.user.api.RefundAppCountContract;
import com.nyd.user.dao.mapper.RefundAppCountMapper;
import com.nyd.user.model.RefundAppCountInfo;
import com.nyd.user.service.RefundAppCountService;
import com.nyd.user.service.util.DateUtil;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
@Service("refundAppCountContract")
public class RefundAppCountServiceImpl implements RefundAppCountService,RefundAppCountContract {
    private static Logger logger = LoggerFactory.getLogger(RefundAppCountServiceImpl.class);
	
	@Autowired
	RefundAppCountMapper refundAppCountMapper;
	
	@Override
    public void save(RefundAppCountInfo refund) throws Exception{
		refundAppCountMapper.save(refund);
    }
	@Override
    public void update(RefundAppCountInfo refund) throws Exception{
		refundAppCountMapper.update(refund);
	}
	@Override
	public ResponseData updateCount(RefundAppCountInfo refund) throws Exception{
		if(StringUtils.isBlank(refund.getAppCode())) {
			return ResponseData.error("请求参数异常！");
		}
		//判断记录是否存在
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("appCode", refund.getAppCode());
		param.put("date", refund.getCountDate());
		Integer count = refundAppCountMapper.judgeRefundAppCount(param);
		if(count != null && count.equals(0)) {
			RefundAppCountInfo info = new RefundAppCountInfo();
			info.setAppCode(refund.getAppCode());
			info.setClickCount(0);
			info.setRegisterCount(0);
			info.setCountDate(refund.getCountDate());
			refundAppCountMapper.save(info);
		}
		if(refund.getUpdateClikCount() == 1) {
			refundAppCountMapper.updateClickCount(refund);
		}else if(refund.getUpdateRegisterCount() == 1) {
			refundAppCountMapper.updateRegisterCount(refund);
		}
		return ResponseData.success();
	}
	@Override
    public ResponseData<List<RefundAppCountInfo>> queryRefundAppCount(Map<String, Object> param) throws Exception{
		ResponseData responseData = ResponseData.success();
		Integer pageNum = (Integer)param.get("pageNum");
        Integer pageSize = (Integer)param.get("pageSize");
        //设置默认分页条件
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 20;
        }
        Integer total = 0;
        try {
			total = refundAppCountMapper.queryRefundAppCountTotal(param);
		} catch (Exception e) {
			logger.error("查询统计异常：" + e.getMessage());
		}
        logger.info("查询总个数 total is " + total);
        if (total == null) {
            total = 0;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<RefundAppCountInfo> list = null;
        try {
            list = refundAppCountMapper.queryRefundAppCount(param);
            for(RefundAppCountInfo info : list) {
            	info.setCountDateStr(DateUtil.dateToString(info.getCountDate()));
            }
        } catch (Exception e) {
        	logger.error("查询退款金额列表失败：" + e.getMessage());
            responseData = ResponseData.error("查询退款金额列表失败：" + e.getMessage());
            return responseData;
        }
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setTotal(total);
        responseData.setData(pageInfo);
        return responseData;
	}
}
