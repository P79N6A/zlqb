package com.nyd.zeus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.nyd.zeus.entity.PaymentRiskExcludeInfo;
import com.nyd.zeus.model.PaymentRiskExcludeInfoRequest;
import com.nyd.zeus.model.PaymentRiskExcludeInfoResult;

public interface PaymentRiskExcludeInfoDao {
    public void save(PaymentRiskExcludeInfo info) throws Exception;
    public List<PaymentRiskExcludeInfoResult> queryList(PaymentRiskExcludeInfoRequest request);
    public PaymentRiskExcludeInfoResult findByOrderNo(String orderNo);
	public Integer queryListCount(PaymentRiskExcludeInfoRequest request);
	void deleteById(Long id);
}
