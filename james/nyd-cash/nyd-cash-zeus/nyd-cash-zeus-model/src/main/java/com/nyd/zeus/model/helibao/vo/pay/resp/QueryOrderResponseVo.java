package com.nyd.zeus.model.helibao.vo.pay.resp;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.nyd.zeus.model.helibao.vo.pay.annotation.SignExclude;

/**
 * Created by heli50 on 2017/4/15.
 */
@ApiModel(description="进入订单查询接口返回报文")
public class QueryOrderResponseVo implements Serializable{
	
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
	
	@ApiModelProperty(value="交易金额")
    private String rt6_orderAmount;
	
	@ApiModelProperty(value="订单创建时间")
    private String rt7_createDate;
	
	@ApiModelProperty(value="订单完成时间")
    private String rt8_completeDate;
	
	@ApiModelProperty(value="订单状态")
    private String rt9_orderStatus;
	
	@ApiModelProperty(value="合利宝交易流水号")
    private String rt10_serialNumber;
	
	@ApiModelProperty(value="银行编码")
    private String rt11_bankId;
	
	@ApiModelProperty(value="银行卡类型")
    private String rt12_onlineCardType;
	
	@ApiModelProperty(value="银行卡后四位")
    private String rt13_cardAfterFour;
	
	@ApiModelProperty(value="绑定id")
    private String rt14_bindId;
	
	@ApiModelProperty(value="用户标识")
    private String rt15_userId;
	
	@ApiModelProperty(value="订单成功或失败的原因")
    @SignExclude
    private String retReason;
	
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

    public String getRt6_orderAmount() {
        return rt6_orderAmount;
    }

    public void setRt6_orderAmount(String rt6_orderAmount) {
        this.rt6_orderAmount = rt6_orderAmount;
    }

    public String getRt7_createDate() {
        return rt7_createDate;
    }

    public void setRt7_createDate(String rt7_createDate) {
        this.rt7_createDate = rt7_createDate;
    }

    public String getRt8_completeDate() {
        return rt8_completeDate;
    }

    public void setRt8_completeDate(String rt8_completeDate) {
        this.rt8_completeDate = rt8_completeDate;
    }

    public String getRt9_orderStatus() {
        return rt9_orderStatus;
    }

    public void setRt9_orderStatus(String rt9_orderStatus) {
        this.rt9_orderStatus = rt9_orderStatus;
    }

    public String getRt10_serialNumber() {
        return rt10_serialNumber;
    }

    public void setRt10_serialNumber(String rt10_serialNumber) {
        this.rt10_serialNumber = rt10_serialNumber;
    }

    public String getRt11_bankId() {
        return rt11_bankId;
    }

    public void setRt11_bankId(String rt11_bankId) {
        this.rt11_bankId = rt11_bankId;
    }

    public String getRt12_onlineCardType() {
        return rt12_onlineCardType;
    }

    public void setRt12_onlineCardType(String rt12_onlineCardType) {
        this.rt12_onlineCardType = rt12_onlineCardType;
    }

    public String getRt13_cardAfterFour() {
        return rt13_cardAfterFour;
    }

    public void setRt13_cardAfterFour(String rt13_cardAfterFour) {
        this.rt13_cardAfterFour = rt13_cardAfterFour;
    }

    public String getRt14_bindId() {
        return rt14_bindId;
    }

    public void setRt14_bindId(String rt14_bindId) {
        this.rt14_bindId = rt14_bindId;
    }

    public String getRt15_userId() {
        return rt15_userId;
    }

    public void setRt15_userId(String rt15_userId) {
        this.rt15_userId = rt15_userId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

	public String getRetReason() {
		return retReason;
	}

	public void setRetReason(String retReason) {
		this.retReason = retReason;
	}
    
    

}
