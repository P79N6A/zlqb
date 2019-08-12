package com.creativearts.nyd.web.interceptors;

import com.creativearts.nyd.pay.model.code.ResultCode;
import com.creativearts.nyd.pay.model.exceptions.ErrorInfo;
import com.creativearts.nyd.pay.model.exceptions.ValidateException;
import com.creativearts.nyd.pay.service.weixinpay.oauth.ApiConfigKit;
import com.creativearts.nyd.web.controller.weixinpay.WxOauthApiController;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cong Yuxiang
 * 2017/11/15
 **/
public class WeixinOuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod h = (HandlerMethod) handler;
            Object bean = h.getBean();
            if(bean instanceof WxOauthApiController){
                try {
                    String appId = request.getParameter("appId");
                    ApiConfigKit.setThreadLocalAppId(appId);

                } finally {
                    ApiConfigKit.removeThreadLocalAppId();
                }
            }else {
                ValidateException exception = new ValidateException();
                ErrorInfo errorInfo = new ErrorInfo(ResultCode.PAY_TYPE_FAIL.getCode(),"微信"+ResultCode.PAY_TYPE_FAIL.getMessage());
                exception.setErrorInfo(errorInfo);
                throw exception;
            }
        }else {
            ValidateException exception = new ValidateException();
            ErrorInfo errorInfo = new ErrorInfo(ResultCode.PAY_TYPE_FAIL.getCode(),"微信"+ResultCode.PAY_TYPE_FAIL.getMessage());
            exception.setErrorInfo(errorInfo);
            throw exception;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
