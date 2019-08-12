package com.creativearts.nyd.web.interceptors;

import com.creativearts.nyd.pay.model.code.ResultCode;
import com.creativearts.nyd.pay.model.exceptions.ErrorInfo;
import com.creativearts.nyd.pay.model.exceptions.ValidateException;
import com.creativearts.nyd.pay.service.alipay.AliPayApiConfigKit;
import com.creativearts.nyd.web.controller.alipay.AliPayApiController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * cong yuxiang
 */
public class AliPayApiInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod h = (HandlerMethod) handler;
            Object bean = h.getBean();
            if(bean instanceof AliPayApiController){
                AliPayApiConfigKit.setThreadLocalAliPayApiConfig(((AliPayApiController)bean).getApiConfig());
            }else {
                ValidateException exception = new ValidateException();
                ErrorInfo errorInfo = new ErrorInfo(ResultCode.PAY_TYPE_FAIL.getCode(),"支付宝"+ResultCode.PAY_TYPE_FAIL.getMessage());
                exception.setErrorInfo(errorInfo);
                throw exception;
            }
        }else {
            ValidateException exception = new ValidateException();
            ErrorInfo errorInfo = new ErrorInfo(ResultCode.PAY_TYPE_FAIL.getCode(),"支付宝"+ResultCode.PAY_TYPE_FAIL.getMessage());
            exception.setErrorInfo(errorInfo);
            throw exception;
        }

        return true;
    }
//    private String getParamString(Map<String, String[]> map) {
//        StringBuilder sb = new StringBuilder();
//        for(Map.Entry<String,String[]> e:map.entrySet()){
//            sb.append(e.getKey()).append("=");
//            String[] value = e.getValue();
//            if(value != null && value.length == 1){
//                sb.append(value[0]).append("\t");
//            }else{
//                sb.append(Arrays.toString(value)).append("\t");
//            }
//        }
//        return sb.toString();
//    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {
//        long startTime = (Long) httpServletRequest.getAttribute("startTime");
//        long endTime = System.currentTimeMillis();
//        long executeTime = endTime - startTime;
//        if (handler instanceof HandlerMethod) {
//            StringBuilder sb = new StringBuilder(1000);
//            sb.append("CostTime  : ").append(executeTime).append("ms").append("\n");
//            sb.append("-------------------------------------------------------------------------------");
//            System.out.println(sb.toString());
//        }
//        httpServletRequest.setAttribute("startTime", startTime);
//        if (handler instanceof HandlerMethod) {
//            StringBuilder sb = new StringBuilder(1000);
//
//
//            HandlerMethod h = (HandlerMethod) handler;
//            sb.append("Controller: ").append(h.getBean().getClass().getName()).append("\n");
//            sb.append("Method    : ").append(h.getMethod().getName()).append("\n");
//            sb.append("Params    : ").append(getParamString(httpServletRequest.getParameterMap())).append("\n");
//            sb.append("URI       : ").append(httpServletRequest.getRequestURI()).append("\n");
//            System.out.println(sb.toString());
//        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//        System.out.println("aftercompletion");
    }
}
