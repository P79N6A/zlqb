package com.nyd.user.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.user.api.RefundAmountContract;
import com.nyd.user.dao.mapper.RefundAmountMapper;
import com.nyd.user.model.RefundAmountInfo;
import com.nyd.user.service.RefundAmountService;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
@Service("refundAmountContract")
public class RefundAmountServiceImpl implements RefundAmountService,RefundAmountContract {
    private static Logger logger = LoggerFactory.getLogger(RefundAmountServiceImpl.class);
	
	@Autowired
	RefundAmountMapper refundAmountMapper;
	
	@Override
    public void save(RefundAmountInfo refund) throws Exception{
		refund.setAmountCode(generaterCode());
		refundAmountMapper.save(refund);
    }
	@Override
    public void update(RefundAmountInfo refund) throws Exception{
		refundAmountMapper.update(refund);
	}
	@Override
    public ResponseData<List<RefundAmountInfo>> queryRefundAmount(RefundAmountInfo refund) throws Exception{
		ResponseData responseData = ResponseData.success();
        try {
            List<RefundAmountInfo> list = refundAmountMapper.queryRefundAmount(refund);
            if (list!=null&&list.size()>0) {
                responseData.setData(list);
            }
        } catch (Exception e) {
        	logger.error("查询退款金额列表失败：" + e.getMessage());
            responseData = ResponseData.error("查询退款金额列表失败：" + e.getMessage());
            return responseData;
        }
        return responseData;
	}
	@Override
	public ResponseData<RefundAmountInfo> getCountByAmount(BigDecimal amount) throws Exception{
		ResponseData responseData = ResponseData.success();
		try {
			RefundAmountInfo list = refundAmountMapper.getCountByAmount(amount);
			if (list!=null) {
				responseData.setData(list);
			}
		} catch (Exception e) {
			logger.error("根据退款金额查询任务数失败：" + e.getMessage());
			responseData = ResponseData.error("根据退款金额查询任务数失败");
			return responseData;
		}
		return responseData;
	}
	@Override
    public ResponseData<RefundAmountInfo> getRefundAmountByCode(RefundAmountInfo info) throws Exception{
		ResponseData responseData = ResponseData.success();
        try {
            RefundAmountInfo refund = refundAmountMapper.getRefundAmountByCode(info);
            if (refund!=null) {
                responseData.setData(refund);
            }
        } catch (Exception e) {
        	logger.error("查询退款金额失败：" + e.getMessage());
            responseData = ResponseData.error("查询退款金额失败：" + e.getMessage());
            return responseData;
        }
        return responseData;
	}
	
	private String generaterCode() {
		String prx = "AM";
		int i = (int)((Math.random()*9+1)*1000);
		return prx+i;
	}
}
