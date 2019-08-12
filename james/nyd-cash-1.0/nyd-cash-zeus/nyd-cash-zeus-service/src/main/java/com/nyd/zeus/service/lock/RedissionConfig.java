//package com.nyd.zeus.service.lock;
//
//import org.redisson.Redisson;
//import org.redisson.config.Config;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class RedissionConfig {
//	
//	Logger logger = LoggerFactory.getLogger(RedissionConfig.class);
//	@Value("${redis.hostname}")
//	private String redisHostName;
//	
//	@Value("${redis.port}")
//	private String redisPort;
//	
//	@Value("${redis.password}")
//	private String redisPassword;
//	
//	@Bean
//    public Redisson redisson(){
//    	 Config config = new Config();
//    	 String address = redisHostName+":"+redisPort;
//    	 logger.info(address+"======"+redisPassword);
//    	  config.useSingleServer().setAddress("redis://"+address)
//    	  .setPassword(redisPassword)
//    	  .setDatabase(0);
//    	 Redisson redisson = (Redisson) Redisson.create(config);
//    	  return redisson;
//     }
//
//}
