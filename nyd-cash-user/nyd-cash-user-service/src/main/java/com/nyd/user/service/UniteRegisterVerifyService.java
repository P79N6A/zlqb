package com.nyd.user.service;

import com.nyd.user.model.dto.NydUniteRegisterDto;
import com.nyd.user.model.uniteregister.UniteRegisterResponseData;

public interface UniteRegisterVerifyService {
	
	/**		
	 * 联合注册处理service
	 */
	UniteRegisterResponseData dealNydUniteRegisterReq(NydUniteRegisterDto dto);

}
