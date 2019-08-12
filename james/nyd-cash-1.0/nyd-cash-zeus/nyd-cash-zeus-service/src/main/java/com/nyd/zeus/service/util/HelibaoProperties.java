package com.nyd.zeus.service.util;

import lombok.Data;
import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @author admin
 *
 */
@Component
@Getter
@Data
public class HelibaoProperties {
  
    @Value("${helibao.customerNumber}")
	private String customerNumber;			//商户编号
    
    @Value("${helibao.certPath}")
	private String certPath;			//公钥路径
    
    @Value("${helibao.pfxPath}")
	private String pfxPath;			//私钥路径
    
    @Value("${helibao.pfxPwd}")
	private String pfxPwd;			//
    
    @Value("${helibao.payIp}")
	private String payIp;			//服务ip
    
    @Value("${helibao.payUrl}")
	private String payUrl;			//标准快捷url
    
    @Value("${helibao.payCallbackUrl}")
	private String payCallbackUrl;			//支付回调地址
    
    @Value("${helibao.entrustedLoanUrl}")
	private String entrustedLoanUrl;			//委托代付url
    
    @Value("${helibao.entrustedUploanUrl}")
	private String entrustedUploanUrl;			//委托代付上传url
    
    @Value("${helibao.entrustedCallbackUrl}")
	private String entrustedCallbackUrl;			//代付回调地址
    
    @Value("${helibao.signkey}")
	private String signkey;			//MD5签名密钥
    
    @Value("${helibao.signkeyPrivate}")
	private String signkeyPrivate;			//RSA私钥
    
    @Value("${helibao.deskeyKey}")
	private String deskeyKey;			//3DES加密密钥
    
    @Value("${helibao.goodsName}")
	private String goodsName;
    
    
}
