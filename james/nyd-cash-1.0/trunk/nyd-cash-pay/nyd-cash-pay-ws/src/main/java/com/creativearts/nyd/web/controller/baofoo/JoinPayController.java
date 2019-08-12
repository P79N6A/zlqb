package com.creativearts.nyd.web.controller.baofoo;


import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.baofoo.*;
import com.creativearts.nyd.pay.service.baofoo.JoinPayWithholdService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author liuqiu
 */
@RestController
@RequestMapping("/pay/baofoo")
public class JoinPayController {

    private Logger log = LoggerFactory.getLogger(JoinPayController.class);

    @Autowired
    private JoinPayWithholdService withholdService;

    /**
     * @param dto
     * @return
     */
    @RequestMapping(value = "/selectBankCard")
    public ResponseData selectBankCard(@RequestBody SelectBankCardDto dto) {
        if (dto == null  || StringUtils.isBlank(dto.getUserId())) {
            return ResponseData.error("参数错误");
        }
        if (StringUtils.isBlank(dto.getAppName())){
            dto.setAppName("nyd");
        }
        log.info("查询汇聚还款代扣是否需要绑卡请求参数:{}", dto.toString());
        ResponseData<SelectBankCardResult> data = withholdService.selectBankCard(dto);
        log.info("查询汇聚还款代扣是否需要绑卡响应结果:{}", JSON.toJSONString(data));
        return data;
    }

    /**
     * @param dto
     * @return
     */
    @RequestMapping(value = "/withHold")
    public ResponseData withHold(@RequestBody BaoFooWithHoldDto dto) {
        if (dto == null || StringUtils.isBlank(dto.getAmount())  || StringUtils.isBlank(dto.getBillNo()) || StringUtils.isBlank(dto.getCardNo()) || StringUtils.isBlank(dto.getPhone()) || StringUtils.isBlank(dto.getBankName()) || StringUtils.isBlank(dto.getUserId())) {
            return ResponseData.error("参数错误");
        }
        if (StringUtils.isBlank(dto.getAppName())){
            dto.setAppName("nyd");
        }
        log.info("发起代扣请求参数:{}", dto.toString());
        ResponseData data = withholdService.withHold(dto);
        log.info("代扣响应结果:{}", JSON.toJSONString(data));
        return data;
    }

    /**
     * 支付回调
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/payCallback/withhold", method = RequestMethod.POST, produces = "application/json")
    public ResponseData payCallback(@RequestBody PayCallbackDTO dto) {
        log.info("common service : pay : payCallback, params is {}", JSON.toJSONString(dto));
        if (StringUtils.isBlank(dto.getPayOrderNo())) {
            return ResponseData.error("参数错误");
        }
        ResponseData responseData = withholdService.payCallback(dto);
        log.info("common service : pay : payCallback, result is {}", ToStringBuilder.reflectionToString(responseData));
        return responseData;
    }

    /**
     * @param dto
     * @return
     */
    @RequestMapping(value = "/withHold/result")
    public ResponseData withHoldResult(@RequestBody BaoFooWithHoldResultDto dto) {
        if (dto == null || StringUtils.isBlank(dto.getPayOrderNo())) {
            return ResponseData.error("参数错误");
        }
        log.info("查询汇聚还款代扣结果请求参数:{}", dto.toString());
        ResponseData data = withholdService.withHoldResult(dto);
        log.info("查询汇聚还款代扣结果响应结果:{}", JSON.toJSONString(data));
        return data;
    }
}
