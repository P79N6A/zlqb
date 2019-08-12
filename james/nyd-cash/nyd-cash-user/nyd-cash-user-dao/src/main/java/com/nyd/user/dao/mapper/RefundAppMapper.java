package com.nyd.user.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nyd.user.model.RefundAppInfo;

@Mapper
public interface RefundAppMapper {
	void save(RefundAppInfo info);
	void update(RefundAppInfo info);
	void updateRecomNum(RefundAppInfo info);
	void resetRealRecomNum();
	List<RefundAppInfo> queryRefundApp(RefundAppInfo info);
	RefundAppInfo getRefundAppByAppCode(@Param("appCode") String appCode);
	Integer queryRefundAppTotal(RefundAppInfo info);
	List<RefundAppInfo> getRefundAppListByCount(RefundAppInfo info);
}
