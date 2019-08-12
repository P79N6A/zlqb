//package com.nyd.settlement.ws.controller;
//
//import com.tasfe.framework.support.model.ResponseData;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//
///**
// * 异常拦截
// * Cong Yuxiang
// * 2018/2/1
// **/
//@ControllerAdvice(annotations = Controller.class)
//public class ExceptionController {
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public ResponseData handleException(Exception ex) {
//
//       return ResponseData.error(ex.getMessage());
//
//    }
//    @ExceptionHandler(NullPointerException.class)
//    @ResponseBody
//    public ResponseData handleNullPointerException(NullPointerException ex) {
//        return ResponseData.error(ex.getMessage());
//
//    }
//}
