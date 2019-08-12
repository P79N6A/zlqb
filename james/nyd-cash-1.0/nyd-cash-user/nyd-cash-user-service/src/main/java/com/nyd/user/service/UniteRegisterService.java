package com.nyd.user.service;

import com.nyd.user.model.dto.NydUniteRegisterDto;
import com.nyd.user.model.dto.UniteRegisterDto;
import com.nyd.user.model.uniteregister.UniteRegisterResponseData;

public interface UniteRegisterService {
	
	/**
	 * 联合注册处理service
	 * @param dto
	 * @return
	 */
	UniteRegisterResponseData dealUniteRegisterReq(UniteRegisterDto dto);
	
	/**
	 * 联合注册处理service
	 */
	UniteRegisterResponseData dealNydUniteRegisterReq(NydUniteRegisterDto dto);
}
