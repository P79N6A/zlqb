package com.creativearts.nyd.pay.service.zzl.helibao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;


@Component
public class ConfigCacheManager {
	
	private static final Logger log = LoggerFactory.getLogger(ConfigCacheManager.class);
	
	public static final String HLB_NAMES_DEFAULT = "helibao";

	/**
	 * 清除掉全部缓存
	 * 
	 * , beforeInvocation = true
	 */
	@CacheEvict(value = HLB_NAMES_DEFAULT, allEntries = true)
	public   void  clearCache() {
		log.info("清除全部的缓存");
	}

}
