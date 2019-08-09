package com.creativearts.nyd.collectionPay.model.zzl.helibao.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by heli50 on 2017/4/14.
 */

@ApiModel(description="商户用户资质上传返回报文")
public class MerchantUserUploadResVo {
	
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
	
	@ApiModelProperty(value="用户编号")
    private String rt6_userId;
	
	@ApiModelProperty(value="用户状态")
    private String rt7_credentialStatus;
	
	@ApiModelProperty(value="返回信息")
    private String rt8_desc;
	
	@ApiModelProperty(value="签名")
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

    public String getRt6_userId() {
        return rt6_userId;
    }

    public void setRt6_userId(String rt6_userId) {
        this.rt6_userId = rt6_userId;
    }

    public String getRt7_credentialStatus() {
        return rt7_credentialStatus;
    }

    public void setRt7_credentialStatus(String rt7_credentialStatus) {
        this.rt7_credentialStatus = rt7_credentialStatus;
    }

    public String getRt8_desc() {
        return rt8_desc;
    }

    public void setRt8_desc(String rt8_desc) {
        this.rt8_desc = rt8_desc;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
