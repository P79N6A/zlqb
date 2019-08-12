package com.nyd.admin.ws.controller;

import com.alibaba.fastjson.JSON;
import com.nyd.admin.model.dto.SalesPlatformDto;
import com.nyd.admin.service.SalesPlatformService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hwei on 2018/12/3.
 */
@RestController
@RequestMapping("/admin")
public class SalesPlatformController {
    private static Logger logger = LoggerFactory.getLogger(SalesPlatformController.class);

    @Autowired
    private SalesPlatformService salesPlatformService;
    /**
     * 注册未填写资料
     * @param salesPlatformDto
     * @return
     */
    @RequestMapping(value = "/find/register/unfilled",method = RequestMethod.POST,produces = "application/json")
    public ResponseData findRegisterUnfilledData(@RequestBody SalesPlatformDto salesPlatformDto) {
        logger.info("注册未填写资料 start param is " + JSON.toJSONString(salesPlatformDto));
        if (salesPlatformDto == null) {
            return ResponseData.error("参数错误！");
        }
        ResponseData responseData = salesPlatformService.findRegisterUnfilledData(salesPlatformDto);
        logger.info("注册未填写资料 result is " + JSON.toJSONString(responseData.getData()));
        return responseData;
    }


    /**
     * 资料填写不完整
     * @param salesPlatformDto
     * @return
     */
    @RequestMapping(value = "/find/data/incomplete",method = RequestMethod.POST,produces = "application/json")
    public ResponseData findDataIncomplete(@RequestBody SalesPlatformDto salesPlatformDto) {
        logger.info("资料填写不完整 start param is " + JSON.toJSONString(salesPlatformDto));
        if (salesPlatformDto == null) {
            return ResponseData.error("参数错误！");
        }
        ResponseData responseData = salesPlatformService.findDataIncomplete(salesPlatformDto);
        logger.info("资料填写不完整 result is " + JSON.toJSONString(responseData.getData()));
        return responseData;

    }

    /**
     * 借款成功
     * @param salesPlatformDto
     * @return
     */
    @RequestMapping(value = "/find/load/success",method = RequestMethod.POST,produces = "application/json")
    public ResponseData findLoadSuccess(@RequestBody SalesPlatformDto salesPlatformDto) {
        logger.info("借款成功 start param is " + JSON.toJSONString(salesPlatformDto));
        if (salesPlatformDto == null) {
            return ResponseData.error("参数错误！");
        }
        ResponseData responseData = salesPlatformService.findLoadSuccess(salesPlatformDto);
        logger.info("借款成功 result is " + JSON.toJSONString(responseData.getData()));
        return responseData;
    }

    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload/checkUserMobile", method = RequestMethod.POST, produces = "application/json")
    public Boolean checkUser(HttpServletRequest request){
        String mobile = request.getParameter("mobile");
        return salesPlatformService.checkUser(mobile);
    }

}
