package com.nyd.zeus.model.helibao.vo.pay.resp;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.nyd.zeus.model.helibao.vo.pay.annotation.SignExclude;

/**
 * Created by mask on 2019/3/19
 */
@ApiModel(description="绑卡支付预下单返回报文")
public class QuickPayBindPayPreOrderResponseVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	
	@ApiModelProperty(value="合利宝交易流水号")
    private String rt6_serialNumber;
	
	@ApiModelProperty(value="订单金额")
    private String rt7_orderAmount;
	
	@ApiModelProperty(value="合利宝绑定号")
    private String rt8_bindId;
	
	@ApiModelProperty(value="用户标识")
    private String rt9_userId;

	@ApiModelProperty(value="短信发送状态")
    @SignExclude
    private String smsStatus;

	@ApiModelProperty(value="短信发送状态描述")
    @SignExclude
    private String smsMsg;

	@ApiModelProperty(value="短信确")
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

    public String getRt6_serialNumber() {
        return rt6_serialNumber;
    }

    public void setRt6_serialNumber(String rt6_serialNumber) {
        this.rt6_serialNumber = rt6_serialNumber;
    }

    public String getRt7_orderAmount() {
        return rt7_orderAmount;
    }

    public void setRt7_orderAmount(String rt7_orderAmount) {
        this.rt7_orderAmount = rt7_orderAmount;
    }

    public String getRt8_bindId() {
        return rt8_bindId;
    }

    public void setRt8_bindId(String rt8_bindId) {
        this.rt8_bindId = rt8_bindId;
    }

    public String getRt9_userId() {
        return rt9_userId;
    }

    public void setRt9_userId(String rt9_userId) {
        this.rt9_userId = rt9_userId;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
