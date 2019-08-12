package com.zhiwang.zfm.dao.sys;

import com.zhiwang.zfm.common.page.BaseMapper;
import com.zhiwang.zfm.entity.sys.SysParameter;
/**
 * SysParameter Mapper
 *
 */
public interface SysParameterMapper<T> extends BaseMapper<T> {
	
	SysParameter queryNew();
}
