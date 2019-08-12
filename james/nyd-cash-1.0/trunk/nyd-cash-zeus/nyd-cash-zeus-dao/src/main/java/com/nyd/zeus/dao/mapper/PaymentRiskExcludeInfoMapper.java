package com.nyd.zeus.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.nyd.zeus.entity.PaymentRiskExcludeInfo;
import com.nyd.zeus.model.PaymentRiskExcludeInfoRequest;
import com.nyd.zeus.model.PaymentRiskExcludeInfoResult;

@Mapper
public interface PaymentRiskExcludeInfoMapper {
	void save(PaymentRiskExcludeInfo paymentRiskExcludeInfo);
	List<PaymentRiskExcludeInfoResult> queryList(PaymentRiskExcludeInfoRequest request);
	PaymentRiskExcludeInfoResult findByOrderNo(String orderNo);
	Integer queryListCount(PaymentRiskExcludeInfoRequest request);
	void deleteById(Long id);
}
