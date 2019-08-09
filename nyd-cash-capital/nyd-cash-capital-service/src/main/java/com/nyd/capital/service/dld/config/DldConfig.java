package com.nyd.capital.service.dld.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class DldConfig {
	
	 	@Value("${dld.shop.id}")
	    private  String shopID = "200008";
	    @Value("${dld.shop.key}")
	    private  String shopKEY = "FYTCP99Y21MY2ZPUKGBHRFI6SDXH23KN";
	    @Value("${dld.shop.rsa}")
	    private  String shopRSA = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLfxzs5vB2Zg4r1U+XOQSilsfQ22PhpTcgu7hsmfC505TobUbDHWnpy+WZ2+wAxPDIyv6rjsJ/Zc9hQYCScNRFR4eVidisHuZ1BLGFNd7aGxkY3kWIrB7haljDIN76OUOyB9kZQ/sjGIYkcSRA5oyCV/5idaRK6DC+xGXI3DxPuQIDAQAB";
	    @Value("${dld.shop.ptpmer.id}")
	    private  String pTpMerId = "929010041211053";
	    @Value("${dld.shop.url}")
	    private  String testUrl = "";
	    @Value("${dld.loan.callback_url}")
	    private  String merCallBackUrl = "127.0.0.1:8080";
	    @Value("${dld.temp.file.path}")
	    private  String tempFilePath = "";
	    @Value("${nyd.dd.talk}")
	    private  String nydDdTalk;
	    @Value("${dld.time.on}")
	    private  String dldTimeOn;
	    @Value("${dld.warn.balance}")
	    private  BigDecimal warnBalance;
	    @Value("${common.pay.url}")
	    private  String commonPayUrl;
}
