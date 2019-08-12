package com.nyd.application.api;

import com.alibaba.fastjson.JSONObject;
import com.nyd.user.model.BankInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by hwei on 2017/11/30.
 */
public interface AgreeMentContract {

    //签署自动还款协议
    ResponseData signAutoRepay(BankInfo bankInfo);

    //签署另外协议
    ResponseData signOtherAgreeMent(JSONObject jsonObject);

    //签署会员协议
    ResponseData signMemberAgreeMent(String userId);

    //签署充值协议
    ResponseData signRechargeAgreeMent(JSONObject jsonObject);


    //还款签署协议--tds
    ResponseData signConductLoan(JSONObject jsonObject);

    //签署注册及隐私协议
    ResponseData signRegisterAgreeMent(JSONObject jsonObject);

    //签署代扣协议
    ResponseData signDaiKouAgreeMent(JSONObject jsonObject);
    
    //信用批核服务协议
    ResponseData signAddressListAgreeMent(BankInfo bankInfo);

}
