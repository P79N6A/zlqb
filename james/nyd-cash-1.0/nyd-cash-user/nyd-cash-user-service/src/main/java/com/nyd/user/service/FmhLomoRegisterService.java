package com.nyd.user.service;

import com.nyd.user.model.dto.UniteRegisterDto;
import com.nyd.user.model.uniteregister.UniteRegisterResponseData;

public interface FmhLomoRegisterService {
	
	/**
	 * 分秒花lomo联合注册service
	 * @param dto
	 * @return
	 */
	UniteRegisterResponseData fmhLomoRegisterReq(UniteRegisterDto dto);

}
