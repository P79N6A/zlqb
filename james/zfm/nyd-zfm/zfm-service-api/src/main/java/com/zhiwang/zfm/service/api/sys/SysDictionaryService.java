package com.zhiwang.zfm.service.api.sys;

import java.util.List;

import com.zhiwang.zfm.common.request.sys.SysDictionaryDetailVO;
import com.zhiwang.zfm.common.request.sys.SysDictionaryVO;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;


public interface SysDictionaryService<T> {

	PagedResponse<List<SysDictionaryDetailVO>> queryDictionaryList(SysDictionaryVO sysDictionaryRequest);
	
	CommonResponse save(SysDictionaryVO sysDictionaryRequest) ;
	
	CommonResponse update(SysDictionaryVO sysDictionaryRequest);
	
}
