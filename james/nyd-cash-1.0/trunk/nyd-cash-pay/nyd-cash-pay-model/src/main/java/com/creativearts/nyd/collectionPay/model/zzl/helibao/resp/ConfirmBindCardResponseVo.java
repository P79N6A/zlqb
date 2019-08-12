package com.creativearts.nyd.collectionPay.model.zzl.helibao.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.nyd.pay.api.annotation.zzl.SignExclude;


/**
 * Created by heli50 on 2017/4/15.
 */
@ApiModel(description="鉴权绑卡返回报文")
public class ConfirmBindCardResponseVo {
	
	@ApiModelProperty(value="交易类型")
    private String rt1_bizType;
	
	@ApiModelProperty(value="返回码")
    private String rt2_retCode;
	
	@ApiModelProperty(value="返回信息")
    private String rt3_retMsg;
	
	@ApiModelProperty(value="商户编号")
    private String rt4_customerNumber;
	
	@ApiModelProperty(value="用户ID")
    private String rt5_userId;
	
	@ApiModelProperty(value="商户订单号")
    private String rt6_orderId;
	
	@ApiModelProperty(value="绑定状态")
    private String rt7_bindStatus;
	
	@ApiModelProperty(value="银行编码")
    private String rt8_bankId;
	
	@ApiModelProperty(value="银行卡后四位")
    private String rt9_cardAfterFour;
	
	
	@ApiModelProperty(value="绑定号")
    private String rt10_bindId;
	
	@ApiModelProperty(value="平台流水号")
    private String rt11_serialNumber;
	
	@ApiModelProperty(value="签名")
    @SignExclude
    private String sign;

    public String getRt1_bizType() {
        return rt1_bizType;
    }

    public void setRt1_bizType(String rt1_bizType) {
        this.rt1_bizType = rt1_bizType;
    }

    public String getRt2_retCode() {
        return rt2_retCode;
    }

    public void setRt2_retCode(String rt2_retCode) {
        this.rt2_retCode = rt2_retCode;
    }

    public String getRt3_retMsg() {
        return rt3_retMsg;
    }

    public void setRt3_retMsg(String rt3_retMsg) {
        this.rt3_retMsg = rt3_retMsg;
    }

    public String getRt4_customerNumber() {
        return rt4_customerNumber;
    }

    public void setRt4_customerNumber(String rt4_customerNumber) {
        this.rt4_customerNumber = rt4_customerNumber;
    }

    public String getRt5_userId() {
        return rt5_userId;
    }

    public void setRt5_userId(String rt5_userId) {
        this.rt5_userId = rt5_userId;
    }

    public String getRt6_orderId() {
        return rt6_orderId;
    }

    public void setRt6_orderId(String rt6_orderId) {
        this.rt6_orderId = rt6_orderId;
    }

    public String getRt7_bindStatus() {
        return rt7_bindStatus;
    }

    public void setRt7_bindStatus(String rt7_bindStatus) {
        this.rt7_bindStatus = rt7_bindStatus;
    }

    public String getRt8_bankId() {
        return rt8_bankId;
    }

    public void setRt8_bankId(String rt8_bankId) {
        this.rt8_bankId = rt8_bankId;
    }

    public String getRt9_cardAfterFour() {
        return rt9_cardAfterFour;
    }

    public void setRt9_cardAfterFour(String rt9_cardAfterFour) {
        this.rt9_cardAfterFour = rt9_cardAfterFour;
    }

    public String getRt10_bindId() {
        return rt10_bindId;
    }

    public void setRt10_bindId(String rt10_bindId) {
        this.rt10_bindId = rt10_bindId;
    }

    public String getRt11_serialNumber() {
        return rt11_serialNumber;
    }

    public void setRt11_serialNumber(String rt11_serialNumber) {
        this.rt11_serialNumber = rt11_serialNumber;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
