package com.nyd.settlement.ws.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.nyd.settlement.service.SettlementRedisService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by Dengw on 2018/1/4
 */
@Component
public class LoginInteceptor implements HandlerInterceptor{
    @Autowired
    SettlementRedisService settlementRedisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accountNo = request.getHeader("accountNo");
        String token = request.getHeader("token");
        boolean judgeFlag = settlementRedisService.judgeTimeout(accountNo,token);
        if(!judgeFlag){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ResponseData responseData = ResponseData.error("登录超时，请重新登录！");
            responseData.setStatus("2");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSONObject.toJSONString(responseData));
            printWriter.flush();
            printWriter.close();
            return false;
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
