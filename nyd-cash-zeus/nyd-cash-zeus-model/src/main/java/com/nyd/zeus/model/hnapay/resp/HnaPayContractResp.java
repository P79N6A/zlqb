package com.nyd.zeus.model.hnapay.resp;

import java.io.Serializable;

import lombok.Data;

@Data
public class HnaPayContractResp implements Serializable {

	private static final long serialVersionUID = 1L;

	// 商户请求流水号
	private String merOrderId;

	// 三方请求流水号
	private String hnapayOrderId;

}
