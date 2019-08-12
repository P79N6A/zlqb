package com.tasfe.zh.base.model;


import lombok.Data;

import java.io.Serializable;

/**
 * Created by liangwei on 2017/6/29.
 */
@Data
public abstract class BaseDto implements Serializable {

    private Long userId;
    private String sessionCode;


    /*private CustomerSetting customerSetting;
    private AuthContract authContract;

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public ValidResult validateSessionCode(){
        ValidResult res = null;
        try {
            res = authContract.validUserCode(this.sessionCode);
        } catch (BizException e) {
            return new ValidResult(0, ErrorCode.RESULT_TOKEN_EXPIRE.getCode().toString(), ErrorCode.RESULT_TOKEN_EXPIRE.getMessage());
        }
        this.userId = res.getUserId();
        return res;
    }

    public void setAuthContract(AuthContract authContract) {
        this.authContract = authContract;
    }

    public CustomerSetting getCustomerSetting() {
        return customerSetting;
    }

    public void setCustomerSetting(CustomerSetting customerSetting) {
        this.customerSetting = customerSetting;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }*/
}
