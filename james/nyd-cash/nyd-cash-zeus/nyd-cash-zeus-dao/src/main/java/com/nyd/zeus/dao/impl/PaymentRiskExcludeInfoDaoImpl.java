package com.nyd.zeus.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.nyd.zeus.dao.PaymentRiskExcludeInfoDao;
import com.nyd.zeus.dao.mapper.PaymentRiskExcludeInfoMapper;
import com.nyd.zeus.entity.PaymentRiskExcludeInfo;
import com.nyd.zeus.model.PaymentRiskExcludeInfoRequest;
import com.nyd.zeus.model.PaymentRiskExcludeInfoResult;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class PaymentRiskExcludeInfoDaoImpl implements PaymentRiskExcludeInfoDao {
	@Autowired
	PaymentRiskExcludeInfoMapper paymentRiskExcludeInfoMapper;
	@Override
    public void save(PaymentRiskExcludeInfo info) throws Exception {
    	paymentRiskExcludeInfoMapper.save(info);
    }
	@Override
	public List<PaymentRiskExcludeInfoResult> queryList(PaymentRiskExcludeInfoRequest request) {
		return paymentRiskExcludeInfoMapper.queryList(request);
	}
	@Override
	public PaymentRiskExcludeInfoResult findByOrderNo(String orderNo) {
		return findByOrderNo(orderNo);
	}
	@Override
	public Integer queryListCount(PaymentRiskExcludeInfoRequest request) {
		return paymentRiskExcludeInfoMapper.queryListCount(request);
	}
	@Override
	public void deleteById(Long id) {
		paymentRiskExcludeInfoMapper.deleteById(id);
	}
}
