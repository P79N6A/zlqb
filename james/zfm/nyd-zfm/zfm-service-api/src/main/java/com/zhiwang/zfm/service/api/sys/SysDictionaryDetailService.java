package com.zhiwang.zfm.service.api.sys;

import java.util.List;
import java.util.Map;

import com.zhiwang.zfm.common.request.sys.SysDictionaryDetailVO;
import com.zhiwang.zfm.common.response.AppCommonResponse;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;

public interface SysDictionaryDetailService<T> {

	/**
	 * 
	 * @desc 查询数据字典明细列表 
	 * @date 2018年5月29日
	 * @auth zhenggang.Huang
	 * @param sysDictionaryDetailRequest
	 * @return
	 */
	PagedResponse<List<SysDictionaryDetailVO>> queryDictionaryDetailList(SysDictionaryDetailVO sysDictionaryDetailRequest) ;
	
	
	CommonResponse<SysDictionaryDetailVO> save(SysDictionaryDetailVO loanSysDictionaryDetailEntityRequest) ;
	
	CommonResponse<SysDictionaryDetailVO> update(SysDictionaryDetailVO loanSysDictionaryDetailEntityRequest) ;
	
	
	/**
	 * 
	 * @desc 根据不同的编码查询不同的明细 多个以","隔开
	 * @date 2018年6月7日
	 * @auth zhenggang.Huang
	 * @param detailCode
	 * @return
	 */
	CommonResponse<List<SysDictionaryDetailVO>> queryDetailOrder(String detailCode);


	
}
