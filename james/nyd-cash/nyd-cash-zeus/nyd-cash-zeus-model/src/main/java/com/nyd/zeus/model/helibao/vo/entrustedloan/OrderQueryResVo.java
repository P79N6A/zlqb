package com.nyd.zeus.model.helibao.vo.entrustedloan;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by heli50 on 2017/4/14.
 */

@ApiModel(description="委托代付订单查询返回报文")
public class OrderQueryResVo implements Serializable{
	
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
	
	@ApiModelProperty(value="用户编号")
    private String rt6_userId;
	
	@ApiModelProperty(value="平台流水号")
    private String rt7_serialNumber;
	
	@ApiModelProperty(value="订单状态")
    private String rt8_orderStatus;
	
	@ApiModelProperty(value="订单描述")
    private String rt9_desc;
	
	@ApiModelProperty(value="时间戳")
    private String rt10_timestamp;
	
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

    public String getRt7_serialNumber() {
        return rt7_serialNumber;
    }

    public void setRt7_serialNumber(String rt7_serialNumber) {
        this.rt7_serialNumber = rt7_serialNumber;
    }

    public String getRt8_orderStatus() {
        return rt8_orderStatus;
    }

    public void setRt8_orderStatus(String rt8_orderStatus) {
        this.rt8_orderStatus = rt8_orderStatus;
    }

    public String getRt9_desc() {
        return rt9_desc;
    }

    public void setRt9_desc(String rt9_desc) {
        this.rt9_desc = rt9_desc;
    }

    public String getRt10_timestamp() {
        return rt10_timestamp;
    }

    public void setRt10_timestamp(String rt10_timestamp) {
        this.rt10_timestamp = rt10_timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
