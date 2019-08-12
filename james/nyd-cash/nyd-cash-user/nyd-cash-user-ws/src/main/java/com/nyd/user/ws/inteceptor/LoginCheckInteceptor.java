package com.nyd.user.ws.inteceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tasfe.framework.redis.RedisService;

/**
 * 
 * @author zhangdk
 *
 */
@Component
public class LoginCheckInteceptor implements HandlerInterceptor{
	private static Logger LOGGER = LoggerFactory.getLogger(LoginCheckInteceptor.class);
    @Autowired
    private RedisService redisService;
	/**
	 * 登录校验
	 */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	/*String accountNumber = request.getHeader("accountNumber");
        String token = request.getHeader("userToken");
        String deviceId = request.getHeader("deviceId");
        response.setHeader("Access-Control-Allow-Origin","*");
        if(token == null || token == "") {
        	token = deviceId;
        }
        return judgeTimeout(accountNumber, token);*/

    	return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
    
    private boolean judgeTimeout(String accountNumber, String token) {
       /* if (StringUtils.isBlank(accountNumber) || StringUtils.isBlank(token)) {
            return false;
        }
        try {
            String tokenInRedis = redisService.getString(accountNumber);
            if (StringUtils.isBlank(tokenInRedis)) {
                return false;
            } else {
                if (token.equals(tokenInRedis)) {
                    return true;
                }
            }
        } catch (Exception e) {
        	LOGGER.error("redis get error! key is " + accountNumber);
        }*/
        return true;
    }

}
