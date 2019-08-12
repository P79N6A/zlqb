package com.creativearts.nyd.collectionPay.model.zzl.helibao.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.nyd.pay.api.annotation.zzl.SignExclude;


/**
 * Created by heli50 on 2017/4/14.
 */
@ApiModel(description="首次支付返回报文")
public class QuickPayCreateOrderResponseVo {

	
	@ApiModelProperty(value="交易类型")
    private String rt1_bizType;

	@ApiModelProperty(value="返回码")
    private String rt2_retCode;

	@ApiModelProperty(value="返回信息")
    private String rt3_retMsg;

    @ApiModelProperty(value="商户编号")
    private String rt4_customerNumber;

    @ApiModelProperty(value="商户订单号")
    private String rt5_orderId;
    
    @ApiModelProperty(value="短信发送状态")
    @SignExclude
    private String smsStatus;
    
    @ApiModelProperty(value="短信发送状态描述")
    @SignExclude
    private String smsMsg;
    
    @ApiModelProperty(value="短信确认")
    @SignExclude
    private String smsConfirm;
    
    

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

    public String getRt5_orderId() {
        return rt5_orderId;
    }

    public void setRt5_orderId(String rt5_orderId) {
        this.rt5_orderId = rt5_orderId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

	public String getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(String smsStatus) {
		this.smsStatus = smsStatus;
	}

	public String getSmsMsg() {
		return smsMsg;
	}

	public void setSmsMsg(String smsMsg) {
		this.smsMsg = smsMsg;
	}

	public String getSmsConfirm() {
		return smsConfirm;
	}

	public void setSmsConfirm(String smsConfirm) {
		this.smsConfirm = smsConfirm;
	}

    
}
