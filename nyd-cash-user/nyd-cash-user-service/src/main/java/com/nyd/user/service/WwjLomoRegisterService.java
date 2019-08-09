package com.nyd.user.service;

import com.nyd.user.model.dto.UniteRegisterDto;
import com.nyd.user.model.uniteregister.UniteRegisterResponseData;

public interface WwjLomoRegisterService {
	
	/**
	 *万万借乐摩联合登录
	 * @param dto
	 * @return
	 */
	UniteRegisterResponseData lomoWwjRegisterReq(UniteRegisterDto dto);

}
