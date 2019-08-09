package com.nyd.msg.service;


import com.alibaba.fastjson.JSONArray;
import com.nyd.msg.exception.ValidateException;
import com.nyd.msg.model.CommonResponse;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.model.SmsRequestBatch;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 短信发送服务
 */
public interface ISendSmsService {
    /**
     * 根据sms类型 发送短信魔板
     * @param vo
     * @return
     * @throws ValidateException
     */
    ResponseData sendSingleSms(SmsRequest vo);

    /**
     *  批量发送通知短信
     * @param batchVo
     * @return
     * @throws Exception
     */
    ResponseData sendBatchSms(SmsRequestBatch batchVo);

    /**
     * 获取验证码
     * @param vo
     * @return
     * @throws ValidateException
     */
    ResponseData getVerifyCode(SmsRequest vo);

    /**
     * 根据位数生成几位验证码
     * @param digit
     * @return
     */
    String generateVerifyCode(int digit);
    
    /**
     * 根据sms类型 发送短信魔板
     * @param vo
     * @return
     * @throws ValidateException
     */
    CommonResponse<JSONArray> smsReport();
}
