package com.nyd.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.capital.api.service.CapitalApi;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderExceptionContract;
import com.nyd.order.dao.mapper.OrderExceptionMapper;
import com.nyd.order.model.OrderExceptionInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.service.NewOrderInfoService;
import com.nyd.order.service.OrderExceptionService;
import com.nyd.user.api.UserBankContract;
import com.nyd.user.model.BankInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
@Service(value = "orderExceptionContract")
public class OrderExceptionImpl implements OrderExceptionService,OrderExceptionContract {
    private static Logger LOGGER = LoggerFactory.getLogger(OrderExceptionImpl.class);
    @Autowired
    private OrderExceptionMapper orderExceptionMapper;
    
    @Autowired
    private UserBankContract userBankContract;
    
    @Autowired
    private OrderContract orderContract;
    @Autowired(required=false)
    private CapitalApi capitalApi;


	@Override
	public ResponseData save(OrderExceptionInfo info) {
		try {
			LOGGER.info("异常订单信息入库：" + JSON.toJSONString(info));
			orderExceptionMapper.save(info);
		}catch(Exception e) {
			LOGGER.error("异常订单信息入库异常：" + e.getMessage());
			return ResponseData.error(e.getMessage());
		}
		return ResponseData.success();
	}

	@Override
	public ResponseData update(OrderExceptionInfo info) {
		try {
			LOGGER.info("异常订单信息更新：" + JSON.toJSONString(info));
			orderExceptionMapper.update(info);
		}catch(Exception e) {
			LOGGER.error("异常订单信息更新异常：" + e.getMessage());
			return ResponseData.error(e.getMessage());
		}
		return ResponseData.success();
	}

	@Override
	public ResponseData queryOrderException(Map<String, Object> param) {
        ResponseData responseData = ResponseData.success();
        try {
            List<OrderExceptionInfo> list = orderExceptionMapper.queryOrderException(param);
            responseData.setData(list);
        } catch (Exception e) {
        	LOGGER.error("查询异常订单信息异常：" + e.getMessage());
            responseData = ResponseData.error(e.getMessage());
        }
        return responseData;
	}
	@Override
	public List<OrderExceptionInfo> getOrderExceptionByOrderNo(OrderInfo info) {
		List<OrderExceptionInfo> list = null;
		try {
			list = orderExceptionMapper.getOrderExceptionByOrderNo(info);
		} catch (Exception e) {
			LOGGER.error("查询异常订单信息异常：" + e.getMessage());
		}
		return list;
	}
	@Override
	public ResponseData queryOrderExceptionList(Map<String, Object> param) {
		ResponseData response = ResponseData.success();
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
			total = orderExceptionMapper.queryOrderExceptionCount(param);
		} catch (Exception e) {
			LOGGER.error("查询异常订单信息异常：" + e.getMessage());
		}
        LOGGER.info("查询总个数 total is " + total);
        if (total == null) {
            total = 0;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<OrderExceptionInfo> list = null;
        List<OrderExceptionInfo> listReturn = new ArrayList<OrderExceptionInfo>();
		try {
			list = orderExceptionMapper.queryOrderException(param);
			for(OrderExceptionInfo info : list) {
				info.setFailTimeStr(DateUtils.formatDate(info.getFailTime(), "yyyy-MM-dd HH:mm:ss"));
				listReturn.add(info);
			}
		} catch (Exception e) {
			LOGGER.error("查询异常订单信息异常：" + e.getMessage());
		}
		PageInfo pageInfo = new PageInfo(listReturn);
        pageInfo.setTotal(total);
        response.setData(pageInfo);
		return response;
	}

	@Override
	public ResponseData saveByOrderInfo(OrderInfo info) {
		OrderExceptionInfo exInfo = new OrderExceptionInfo();
		BeanUtils.copyProperties(info, exInfo);
		ResponseData<List<BankInfo>> list = userBankContract.getBankInfosByBankAccout(info.getBankAccount());
		if(list != null && list.getData() != null) {
			BankInfo bank = list.getData().get(0);
			exInfo.setAccountNumber(bank.getReservedPhone());
			exInfo.setRealName(bank.getAccountName());
		}
		//异常订单状态初始化
		exInfo.setOrderStatus(null);
		exInfo.setAuditStatus(null);
		exInfo.setFailTime(new Date());
		List<OrderExceptionInfo> orderRep = getOrderExceptionByOrderNo(info);
		if(orderRep != null && orderRep.size() > 0) {
			update(exInfo);
		}else{
			save(exInfo);
		}
		return ResponseData.success();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseData audit(String orderNo, Integer auditStatu, String fundCode) throws Exception{
		OrderExceptionInfo info = new OrderExceptionInfo();
		info.setOrderNo(orderNo);
		info.setAuditStatus(auditStatu);
		//info.setFundCode(fundCode);
		update(info);
		return ResponseData.success();
	}
}
