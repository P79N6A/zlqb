package com.nyd.admin.ws.controller;

import com.alibaba.fastjson.JSON;
import com.nyd.admin.model.dto.*;
import com.nyd.admin.service.CustomerHandleQueryService;
import com.nyd.admin.service.CustomerServiceLogService;
import com.nyd.admin.service.DownloadAgreementLogService;
import com.nyd.admin.service.ReturnPremiumService;
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
 * @author cm
 */
@RestController
@RequestMapping(value = "/admin")
public class CustomerHandleQueryController {
    private static Logger logger = LoggerFactory.getLogger(CustomerHandleQueryController.class);

    @Autowired
    private CustomerHandleQueryService customerHandleQueryService;

    @Autowired
    private ReturnPremiumService returnPremiumService;

    @Autowired
    private CustomerServiceLogService customerServiceLogService;

    @Autowired
    private DownloadAgreementLogService downloadAgreementLogService;

    /**
     * 客户处理查询
     * @param customerQueryDto
     * @return
     */
    @RequestMapping(value = "/customerquery",method = RequestMethod.POST,produces = "application/json")
    public ResponseData customerQuery(@RequestBody CustomerQueryDto customerQueryDto){
        logger.info("客户处理查询入参:"+ JSON.toJSON(customerQueryDto));
        ResponseData responseData = customerHandleQueryService.findAllCustomer(customerQueryDto);
        logger.info("back to web object of find customer:"+JSON.toJSON(responseData));
        return responseData;

    }


    /**
     * 退费标记
     * @param returnPremiumLabelDto
     * @return
     */
    @RequestMapping(value = "/returnpremiumlabel",method = RequestMethod.POST,produces = "application/json")
    public ResponseData returnPremiumLabel(@RequestBody ReturnPremiumLabelDto returnPremiumLabelDto,HttpServletRequest request){
        logger.info("退费标记入参:"+JSON.toJSON(returnPremiumLabelDto));
        String OperationPerson = request.getHeader("accountNo");
        returnPremiumLabelDto.setOperatePerson(OperationPerson);
        ResponseData responseData = returnPremiumService.saveReturnPremiumLabel(returnPremiumLabelDto);
        logger.info("back to web object of return premium label:"+JSON.toJSON(responseData));
        return  responseData;
    }


    /**
     * 保存客服记录
     * @param customerServiceLogDto
     * @param request
     * @return
     */
    @RequestMapping(value = "/customerservice",method = RequestMethod.POST,produces = "application/json")
    public ResponseData customerService (@RequestBody CustomerServiceLogDto customerServiceLogDto,HttpServletRequest request){
        String OperationPerson = request.getHeader("accountNo");
        logger.info("操作人员:"+JSON.toJSON(OperationPerson));
        customerServiceLogDto.setOperationPerson(OperationPerson);
        ResponseData responseData = customerServiceLogService.saveCustomerServiceLog(customerServiceLogDto);
        return responseData;
    }

    /**
     * 客服记录查询
     * @param customerServiceLogDto
     * @return
     */
    @RequestMapping(value = "/customerservice/query",method = RequestMethod.POST,produces = "application/json")
    public ResponseData queryCustomerService (@RequestBody CustomerServiceLogDto customerServiceLogDto){
        logger.info("查询客服记录请求参数:"+customerServiceLogDto.getUserId());
        ResponseData responseData = customerServiceLogService.findCustomerServiceList(customerServiceLogDto.getUserId());
        logger.info("back to web web of customer service:"+JSON.toJSON(responseData));
        return responseData;

    }

    /**
     * 充值付费记录
     * @return
     */
    @RequestMapping(value = "/rechargePaymentRecords",method = RequestMethod.POST,produces = "application/json")
    public ResponseData rechargePaymentRecords(@RequestBody RechargePaymentRecordDto rechargePaymentRecordDto){
        logger.info("查询用户充值付费记录入参:" + JSON.toJSONString(rechargePaymentRecordDto));
        ResponseData responseData = customerHandleQueryService.findrechargePaymentRecordsByUserId(rechargePaymentRecordDto);
        logger.info("查询用户充值付费记录结果:" + responseData.getData());
        return responseData;
    }

    /**
     * 风险速查报告
     * @param rechargePaymentRecordDto
     * @return
     */
    @RequestMapping(value = "/riskInspectionReport", method = RequestMethod.POST, produces = "application/json")
    public ResponseData riskInspectionReport(@RequestBody RechargePaymentRecordDto rechargePaymentRecordDto) {
        logger.info("查询用户风险速查报告入参:" + JSON.toJSONString(rechargePaymentRecordDto));
        ResponseData responseData = customerHandleQueryService.findRiskInspectionReport(rechargePaymentRecordDto);
        logger.info("查询用户风险速查报告结果:" + responseData.getData());
        return responseData;
    }

    /**
     * 订单记录
     * @param rechargePaymentRecordDto
     * @return
     */
    @RequestMapping(value = "/orderdetails", method = RequestMethod.POST, produces = "application/json")
    public ResponseData orderdetails(@RequestBody RechargePaymentRecordDto rechargePaymentRecordDto){
        logger.info("查询订单详情入参:" +  JSON.toJSONString(rechargePaymentRecordDto));
        ResponseData responseData = customerHandleQueryService.findorderdetailsByUserId(rechargePaymentRecordDto);
        logger.info("查询订单详情结果:" + responseData.getData());
        return responseData;
    }

    /**
     * 综合券管理（侬要贷）查询
     * @param integratedVolumeDto
     * @return
     */
    @RequestMapping(value = "/findIntegratedVolumeDetails", method = RequestMethod.POST, produces = "application/json")
    public ResponseData findIntegratedVolumeDetails(@RequestBody IntegratedVolumeDto integratedVolumeDto){
        logger.info("综合卷管理（侬要贷）入参:" +  JSON.toJSONString(integratedVolumeDto));
        ResponseData responseData = returnPremiumService.findIntegratedVolumeDetails(integratedVolumeDto);
        logger.info("综合卷管理（侬要贷）结果:" + responseData.getData());
        return responseData;
    }

    /**
     * 批量发券
     * @return
     */
    @RequestMapping(value = "/batchCoupons", method = RequestMethod.POST, produces = "application/json")
    public ResponseData batchCoupons(@RequestBody BatchCouponDto batchCouponDto, HttpServletRequest request){
        String updateBy = request.getHeader("accountNo");
        batchCouponDto.setUpdateBy(updateBy);
        logger.info("批量发券 入参：" + JSON.toJSONString(batchCouponDto));
        ResponseData responseData = returnPremiumService.batchCoupons(batchCouponDto);
        logger.info("批量发券 结果：" + JSON.toJSONString(responseData));
        return responseData;
    }

    /**
     * 修改备注
     * @return
     */
    @RequestMapping(value = "/updateRemark", method = RequestMethod.POST, produces = "application/json")
    public ResponseData updateRemark(@RequestBody RemarkDto remarkDto){
        logger.info("修改备注 入参：" + JSON.toJSONString(remarkDto));
        ResponseData responseData = returnPremiumService.updateRemark(remarkDto);
        logger.info("修改备注 结果：" + JSON.toJSONString(responseData));
        return responseData;
    }


    /**
     * 代扣协议查询
     * @param withHoldAgreementDto
     * @return
     */
    @RequestMapping(value = "/withHoldAgreement", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryWithHoldAgreement(@RequestBody WithHoldAgreementDto withHoldAgreementDto){
        logger.info("侬要贷代扣协议查询请求参数:"+withHoldAgreementDto.getUserId());
        ResponseData responseData = customerHandleQueryService.queryWithHoldAgreement(withHoldAgreementDto.getUserId());
        return responseData;
    }


    /**
     * 代扣协议下载记录保存
     * @param downloadAgreementLogDto
     * @param request
     * @return
     */
    @RequestMapping(value = "/withHoldAgreement/download", method = RequestMethod.POST, produces = "application/json")
    public ResponseData downLoadWithHoldAgreement(@RequestBody DownloadAgreementLogDto downloadAgreementLogDto,HttpServletRequest request){
        logger.info("侬要贷代扣协议下载请求参数:"+downloadAgreementLogDto.getAgreementId());
        String updateBy = request.getHeader("accountNo");
        logger.info("download person :"+updateBy);
        downloadAgreementLogDto.setDownloadPerson(updateBy);
        ResponseData responseData = downloadAgreementLogService.saveDownloadAgreementLog(downloadAgreementLogDto);
        return responseData;
    }

}
