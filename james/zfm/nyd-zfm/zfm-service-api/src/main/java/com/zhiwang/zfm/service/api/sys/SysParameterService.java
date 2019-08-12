package com.zhiwang.zfm.service.api.sys;

import com.alibaba.fastjson.JSONObject;
import com.zhiwang.zfm.common.request.sys.SysParameterVO;
import com.zhiwang.zfm.common.response.CommonResponse;

public interface SysParameterService<T> {

	/**
	 * 查询最新一条数据
	 * @return
	 */
	CommonResponse<SysParameterVO> queryNew();

	/**
	 * 修改数据
	 * @param vo
	 * @return
	 */
	CommonResponse<JSONObject> update(SysParameterVO vo);

	/**
	 * 添加数据
	 * @param vo
	 */
	CommonResponse<JSONObject> save(SysParameterVO vo);

}
