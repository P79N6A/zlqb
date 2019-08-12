package com.nyd.user.service;

import com.nyd.user.model.GeinihuaResponseData;
import com.nyd.user.model.dto.GeiNiHuaDTO;

/**
 * 
 * @author zhangch
 *
 */
public interface RegisterGeiNiHuaService {

	 /**
	   * 
	   * @author zhangch
	   * @Description: 给你花联合注册撞库接口
	   * @param @param geiNiHuaDTO
	   * @param @return
	   * @return GeinihuaResponseData
	   * @throws
	    */
	   GeinihuaResponseData checkGNHAccountPhone(GeiNiHuaDTO geiNiHuaDTO);
	   
	   /**
	    * 
	   * @author zhangch
	   * @Description: 给你花注册返回接口
	   * @param @param geiNiHuaDTO
	   * @param @return
	   * @return GeinihuaResponseData
	   * @throws
	    */
	   GeinihuaResponseData registerGeiNiHua(GeiNiHuaDTO geiNiHuaDTO);
}
