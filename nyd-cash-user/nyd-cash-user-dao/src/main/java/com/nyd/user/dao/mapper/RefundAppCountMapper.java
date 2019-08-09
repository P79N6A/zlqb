package com.nyd.user.dao.mapper;

import java.util.List;
import java.util.Map;

import com.nyd.user.model.RefundAppCountInfo;


public interface RefundAppCountMapper {
	void save(RefundAppCountInfo info);
	void update(RefundAppCountInfo info);
	void updateClickCount(RefundAppCountInfo info);
	void updateRegisterCount(RefundAppCountInfo info);
	List<RefundAppCountInfo> queryRefundAppCount(Map<String, Object> param);
	Integer judgeRefundAppCount(Map<String, Object> param);
	Integer queryRefundAppCountTotal(Map<String, Object> param);
}
