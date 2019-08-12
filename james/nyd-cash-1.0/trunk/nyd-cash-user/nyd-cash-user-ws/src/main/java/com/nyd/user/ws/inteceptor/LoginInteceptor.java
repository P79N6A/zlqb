package com.nyd.user.ws.inteceptor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.nyd.user.model.dto.IpIntercepterDto;
import com.nyd.user.ws.util.ControllerUntil;
import com.tasfe.framework.redis.RedisService;

/**
 * 
 * @author zhangdk
 *
 */
@Component
public class LoginInteceptor implements HandlerInterceptor{
	private static Logger LOGGER = LoggerFactory.getLogger(LoginInteceptor.class);
	
	private static final String FORBIDEN_IP_PRFIX="iplist";
	//时间内登录次数限制
	private static final int COUNT = 20;
	//限制登录时间  单位分钟
	private static final int FORBIDEN_MIN = 10;
	//多久之内登录多少次才拦截的时间 单位分钟
	private static final int QUALIFICATION_TIME = 1;
	//单位秒 redis 有效时间
	private static final int USE_LONG = 60*15;
	
    @Autowired
    private RedisTemplate redisTemplate;
	/**
	 * 登录拦截处理恶意IP频繁请求问题
	 */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	/*
    	String ip = ControllerUntil.getIPAddr(request);
    	String[] ips = ip.split(",");
    	List<String> ipl = null;
    	if(ips != null && ips.length>1) {
    		ipl = Arrays.asList(ips);
    		ip = ipl.get(0);
    	}
    	LOGGER.info("打印请求Ip：" + ip);
    	IpIntercepterDto redis =(IpIntercepterDto)JSON.parseObject((String)redisTemplate.opsForValue().get(FORBIDEN_IP_PRFIX+":" + ip), IpIntercepterDto.class);
    	//判断是否请求过。没有则创建
    	if(redis == null) {
    		IpIntercepterDto dto = new IpIntercepterDto();
        	dto.setCount(1);
        	dto.setForbidenFlag(false);
        	dto.setStartTime(new Date());
        	dto.setEndTime(new Date());
        	redisTemplate.opsForValue().set(FORBIDEN_IP_PRFIX+":" + ip, JSON.toJSONString(dto),USE_LONG,TimeUnit.SECONDS);
        	LOGGER.info("不拦截" + dto.getCount());
        	return true;
    	}else {
    		//不需要拦截  拦截标识为false
    		if(!redis.isForbidenFlag()) {
    			redis.setCount(redis.getCount()+1);
    			//如果小于设置数值，则直接+1返回 。否则直接设置拦截标识为ture,
    			if(redis.getCount()<=COUNT) {
    				if(new Date().compareTo(DateUtils.addMinutes(redis.getStartTime(), QUALIFICATION_TIME) ) >= 0) {
    					redisTemplate.delete(FORBIDEN_IP_PRFIX+":" + ip);
    					LOGGER.info("不拦截并重置");
    					return true;
    				}
    				redis.setEndTime(new Date());
    				redisTemplate.opsForValue().set(FORBIDEN_IP_PRFIX+":" + ip, JSON.toJSONString(redis),USE_LONG,TimeUnit.SECONDS);
    				LOGGER.info("不拦截计数" + redis.getCount());
        			return true;
    			}else {
    				if(new Date().compareTo(DateUtils.addMinutes(redis.getStartTime(), QUALIFICATION_TIME)) <= 0) {
    					redis.setForbidenFlag(true);
        				redis.setEndTime(new Date());
        				redis.setForbidenTime(DateUtils.addMinutes(new Date(), FORBIDEN_MIN));
        				redisTemplate.opsForValue().set(FORBIDEN_IP_PRFIX+":" + ip, JSON.toJSONString(redis),USE_LONG,TimeUnit.SECONDS);
        				LOGGER.info("拦截" + redis.getCount());
            			return false;
    				}else {
    					redisTemplate.delete(FORBIDEN_IP_PRFIX+":" + ip);
    					LOGGER.info("不拦截并重置");
        				return true;
    				}
    			}
    		}
    		//需要拦截的情况，拦截标识为true
    		if(redis.isForbidenFlag()) {
    			//如果禁止截至时间大于当前时间，直接返回fasle。否则remove key
    			if(redis.getForbidenTime().compareTo(new Date()) > 0) {
    				redis.setCount(redis.getCount()+1);
    				redisTemplate.opsForValue().set(FORBIDEN_IP_PRFIX+":" + ip, JSON.toJSONString(redis),USE_LONG,TimeUnit.SECONDS);
    				LOGGER.info("拦截" + redis.getCount());
        			return false;
    			}else {
    				redis.setCount(1);
    	        	redis.setForbidenFlag(false);
    	        	redis.setStartTime(new Date());
    	        	redis.setEndTime(new Date());
    	        	redisTemplate.opsForValue().set(FORBIDEN_IP_PRFIX+":" + ip, JSON.toJSONString(redis),USE_LONG,TimeUnit.SECONDS);
    	        	LOGGER.info("不拦截" + redis.getCount());
    	        	return true;
    			}
    		}
    		
    	}*/
    	return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }

}
