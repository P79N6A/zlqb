package com.zhiwang.zfm.service.api.sys;

import java.util.List;

import com.zhiwang.zfm.common.request.sys.SysPlatformReqVO;
import com.zhiwang.zfm.common.response.bean.sys.SysPlatformVO;

public interface SysPlatformService<T> {

	/**
	 * 查询平台
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SysPlatformVO> pageData(SysPlatformReqVO vo) throws Exception;

	/**
	 * 查询数量
	 * 
	 * @param vo
	 * @return
	 * @throws Exception 
	 */
	long pageTotalRecord(SysPlatformReqVO vo) throws Exception;
}
