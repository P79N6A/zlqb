package com.zhiwang.zfm.service.api.sys;

import java.util.List;

import com.zhiwang.zfm.common.request.sys.SysChannelReqVO;
import com.zhiwang.zfm.common.response.bean.sys.SysChannelVO;

public interface SysChannelService<T> {
	/**
	 * 查询数据
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<SysChannelVO> pageData(SysChannelReqVO vo) throws Exception;

	/**
	 * 查询数量
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	long pageTotalRecord(SysChannelReqVO vo) throws Exception;
}
