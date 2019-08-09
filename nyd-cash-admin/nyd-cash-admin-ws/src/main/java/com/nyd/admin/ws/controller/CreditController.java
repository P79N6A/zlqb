package com.nyd.admin.ws.controller;

import com.alibaba.fastjson.JSON;
import com.nyd.admin.model.dto.CreditDto;
import com.nyd.admin.model.dto.CreditRemarkDto;
import com.nyd.admin.service.CreditService;
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
 * @Author: wucx
 * @Date: 2018/10/16 21:06
 */
@RestController
@RequestMapping(value = "/admin")
public class CreditController {
    private static Logger logger = LoggerFactory.getLogger(CreditController.class);

    @Autowired
    private CreditService creditService;

    /**
     * 授信查询
     * @return
     */
    @RequestMapping(value = "/findCredit/auth",method = RequestMethod.POST,produces = "application/json")
    public ResponseData findCreditDetails(@RequestBody CreditDto creditDto){
        logger.info("授信查询 入参：" + JSON.toJSONString(creditDto));
        ResponseData responseData = creditService.findCreditDetails(creditDto);
        logger.info("授信查询返回结果：" + responseData.getData());
        return responseData;
    }

    /**
     * 授信操作
     * @param creditRemarkDto
     * @return
     */
    @RequestMapping(value = "/updateRemark/auth",method = RequestMethod.POST,produces = "application/json")
    public ResponseData updateRemark(@RequestBody CreditRemarkDto creditRemarkDto, HttpServletRequest request){
        String operatorPerson = request.getHeader("accountNo");
        creditRemarkDto.setOperatorPerson(operatorPerson);
        logger.info("授信操作 入参：" + JSON.toJSONString(creditRemarkDto));
        ResponseData responseData = creditService.updateCreditRemark(creditRemarkDto);
        logger.info("授信操作 出参：" + responseData.getData());
        return responseData;
    }
}
