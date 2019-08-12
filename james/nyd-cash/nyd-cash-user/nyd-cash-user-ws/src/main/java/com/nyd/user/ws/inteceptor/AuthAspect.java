package com.nyd.user.ws.inteceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.nyd.user.model.LoginInfo;
import com.nyd.user.model.dto.IpIntercepterDto;
import com.nyd.user.service.AccountInfoService;
import com.nyd.user.service.UserLoginService;
import com.nyd.user.ws.exception.AuthException;
import com.tasfe.framework.redis.RedisService;

@Component
@Aspect
public class AuthAspect {
    protected Logger logger = LoggerFactory.getLogger(AuthAspect.class);
    
    @Autowired
    UserLoginService userLoginService;
    @Autowired
    RedisService redisService;
    @Autowired
    AccountInfoService accountInfoService;
    
    /**
     * 参考：https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#aop-pointcuts
     * 定义校验切入点
     */
    @Pointcut("execution(* com.nyd.user.ws.controller..*(..))")
    public void verify() {
    }


    /**
     * 切入点之前执行
     * @param joinPoint
     */
   /* @Before("verify()")
    public void before(JoinPoint joinPoint) throws Exception{
        if (getMthodDescription(joinPoint)){
            Object[] args = joinPoint.getArgs();
            Map<String,String> params = new HashMap<>();
            for(Object arg:args){
                params.putAll(BeanUtils.describe(arg));
            }
            // 验证成功返回true。失败按异常处理
            if(doVerify(params)){

            } else {
                throw new AuthException("Login timeout,please login again!");
            }
        }
    }*/


    /**
     * 做一些校验
     * @param params
     * @return
     */
    private boolean doVerify(Map<String,String> params){
        String deviceId = params.get("deviceId");
        String  accountNumber = params.get("accountNumber");
        String token = params.get("userToken");
        String userId = params.get("userId");
        LoginInfo login = new LoginInfo();
        login.setAccountNumber(accountNumber);
        login.setDeviceId(deviceId);
        login.setUserToken(token);
        login.setUserId(userId);
        if(token != null && token != "" && accountNumber != null) {
        	return judgeTimeoutByMd5(login);
        }else {
        	if(deviceId != null && accountNumber != null){
        		return userLoginService.judgeTimeout(accountNumber,deviceId);
        	} else {
        		return false;
        	}
        }
    }

    /**
     * 异常处理
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "verify()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) throws Exception {
        throw (AuthException)e;

    }

    /**
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    public boolean getMthodDescription(JoinPoint joinPoint) throws Exception {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String[] val = requestMapping.value();
        for(String v:val){
            if(v.contains("auth")){
                return true;
            }
        }
        return false;
    }
    
    private boolean judgeTimeoutByMd5(LoginInfo check) {
		if (StringUtils.isBlank(check.getUserToken())) {
			return false;
		} else {
			try {
				String pfx = accountInfoService.getLoginPrefix(check.getAccountNumber());
				LoginInfo source = (LoginInfo)JSON.parseObject(redisService.getString(pfx+ check.getUserToken()), LoginInfo.class);
				return checkToken(check,source);
			} catch (Exception e) {
				logger.error("redis get error! key is " + check.getAccountNumber());
			}
		}
		return false;
	}


	private boolean checkToken(LoginInfo checkToken, LoginInfo source) {
		if(checkToken == null || source == null) {
			return false;
		}
		if(!StringUtils.isEmpty(checkToken.getAccountNumber()) && !checkToken.getAccountNumber().equals(source.getAccountNumber())) {
			return false;
		}
		if(!StringUtils.isEmpty(checkToken.getDeviceId()) && !checkToken.getDeviceId().equals(source.getDeviceId())) {
			return false;
		}
		if(!StringUtils.isEmpty(checkToken.getUserId()) && !StringUtils.isEmpty(source.getUserId()) && !checkToken.getUserId().equals(source.getUserId())) {
			return false;
		}
		return true;
	}

}