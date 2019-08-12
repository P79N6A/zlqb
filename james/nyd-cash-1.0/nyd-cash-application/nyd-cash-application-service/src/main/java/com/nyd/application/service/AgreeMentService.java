package com.nyd.application.service;

import com.alibaba.fastjson.JSONObject;
import com.nyd.application.model.request.AgreementTemplateModel;
import com.nyd.user.model.BankInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by hwei on 2017/11/25.
 * 协议相关操作
 */
public interface AgreeMentService {
    //上传协议模板
    ResponseData uploadAgreemengt(AgreementTemplateModel agreementTemplateModel);
    
    //签署自动还款协议
    ResponseData signAutoRepay(BankInfo bankInfo);

    //签署自动还款协议
    ResponseData signConductLoan(JSONObject jsonObject);

    //查看签章的合同
    ResponseData getSignAgreement(String userId,String orderId);
    
    //签署注册及隐私协议
	ResponseData signRegisterAgreeMent(JSONObject json);

	//信用批核服务协议
    ResponseData signAddressListAgreeMent(BankInfo bankInfo);
	
}
