package com.nyd.order.ws.inteceptor;

import com.alibaba.fastjson.JSONObject;
import com.nyd.order.ws.exception.AuthException;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExpInterceptor implements HandlerExceptionResolver {
    private static Logger LOGGER = LoggerFactory.getLogger(GlobalExpInterceptor.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv = new ModelAndView();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin","*");
        //如果是自定义异常
        if(ex instanceof AuthException) {
            try {
                ResponseData responseData = ResponseData.error("请重新登录！");
                responseData.setStatus("2");
                PrintWriter printWriter = response.getWriter();
                printWriter.write(JSONObject.toJSONString(responseData));
                printWriter.flush();
                printWriter.close();
            } catch (IOException e) {
                LOGGER.error("GlobalExpInterceptor has except!",e);
            }
        } else {
            try {
                ResponseData responseData = ResponseData.error("请求异常！");
                responseData.setStatus("3");
                PrintWriter printWriter = response.getWriter();
                printWriter.write(JSONObject.toJSONString(responseData));
                printWriter.flush();
                printWriter.close();
            } catch (IOException e) {
                LOGGER.error("GlobalExpInterceptor has except!",e);
            }
        }
        return mv;
    }
}
