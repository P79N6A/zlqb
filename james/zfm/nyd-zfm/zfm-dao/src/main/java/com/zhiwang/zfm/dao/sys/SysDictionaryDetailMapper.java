package com.zhiwang.zfm.dao.sys;

import java.util.List;
import java.util.Map;

import com.zhiwang.zfm.common.page.BaseMapper;
import com.zhiwang.zfm.entity.sys.SysDictionaryDetail;
/**
 * SysDictionaryDetail Mapper
 *
 */
public interface SysDictionaryDetailMapper<T> extends BaseMapper<T> {
	
	List<SysDictionaryDetail> queryDetailOrder(Map<String,Object> params);
	
	SysDictionaryDetail queryDectionaryDetailByCode(String detailCode);
	
}
