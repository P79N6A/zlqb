package com.nyd.admin.ws.controller;

import com.alibaba.fastjson.JSON;
import com.nyd.admin.model.dto.CustomerRefundDto;
import com.nyd.admin.model.dto.RefundApplyDto;
import com.nyd.admin.service.CustomerRefundService;
import com.nyd.user.model.RefundApplyInfo;
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
 * 退费
 * @Author: wucx
 * @Date: 2018/11/2 17:43
 */
@RestController
@RequestMapping(value = "/admin")
public class CustomerRefundController {

    private static Logger logger = LoggerFactory.getLogger(CustomerRefundController.class);

    @Autowired
    private CustomerRefundService customerRefundService;

    /**
     * 审核操作接口
     * @return
     */
    @RequestMapping(value = "/operational/auth",method = RequestMethod.POST,produces = "application/json")
    public ResponseData operational(@RequestBody CustomerRefundDto customerRefundDto, HttpServletRequest request) {
        String operatorPerson = request.getHeader("accountNo");
        customerRefundDto.setOperatorPerson(operatorPerson);
        logger.info("审核操作 入参：" + JSON.toJSONString(customerRefundDto));
        ResponseData responseData = customerRefundService.customerOperational(customerRefundDto);
        logger.info("审核操作 出参：" + JSON.toJSONString(responseData.getData()));
        return responseData;
    }
    /**
     * 查询退款列表
     * @return
     */
    @RequestMapping(value = "/refund/list/auth",method = RequestMethod.POST,produces = "application/json")
    public ResponseData queryRefundList(@RequestBody RefundApplyDto apply, HttpServletRequest request) {
    	String operatorPerson = request.getHeader("accountNo");
    	apply.setUpdateBy(operatorPerson);
    	logger.info("查询 入参：" + JSON.toJSONString(apply));
    	ResponseData responseData = customerRefundService.getRefundList(apply);
    	logger.info("查询完成");
    	return responseData;
    }

    /**
     * 财务查询
     * @param apply
     * @return
     */
    @RequestMapping(value = "/finance/refund/list/auth",method = RequestMethod.POST,produces = "application/json")
    public ResponseData financeQueryRefundList(@RequestBody RefundApplyDto apply){
        logger.info("财务查询 入参：" + JSON.toJSONString(apply));
        ResponseData responseData = customerRefundService.financeQueryRefundList(apply);
        logger.info("财务查询 出参：" + JSON.toJSONString(responseData.getData()));
        return responseData;
    }
    /**
     * 查询详情
     * @return
     */
    @RequestMapping(value = "/refund/detail/auth",method = RequestMethod.POST,produces = "application/json")
    public ResponseData queryRefundDetail(@RequestBody RefundApplyDto apply, HttpServletRequest request) {
    	String operatorPerson = request.getHeader("accountNo");
    	apply.setUpdateBy(operatorPerson);
    	logger.info("查询 入参：" + JSON.toJSONString(apply));
    	ResponseData responseData = customerRefundService.getRefundDetail(apply);
    	logger.info("查询完成");
    	return responseData;
    }
}
