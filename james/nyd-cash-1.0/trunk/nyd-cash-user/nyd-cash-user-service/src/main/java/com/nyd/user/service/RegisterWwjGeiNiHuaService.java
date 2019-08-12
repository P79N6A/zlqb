package com.nyd.user.service;

import com.nyd.user.model.GeinihuaResponseData;
import com.nyd.user.model.dto.GeiNiHuaDTO;

public interface RegisterWwjGeiNiHuaService {
	
	 /**
	  * wwj51gnh撞库接口
	  * @param geiNiHuaDTO
	  * @return
	  */
	 GeinihuaResponseData checkWwjGNHAccountPhone(GeiNiHuaDTO geiNiHuaDTO);
	 
	 /**
	  * wwj51gnh注册接口
	  * @param geiNiHuaDTO
	  * @return
	  */
	 GeinihuaResponseData registerWwjGeiNiHua(GeiNiHuaDTO geiNiHuaDTO);
	 
	 

}
