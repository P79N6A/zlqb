package com.nyd.zeus.model.xunlian.resp;

import java.io.Serializable;

import lombok.Data;

@Data
public class IdentifyResp implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		//商户请求流水号
		private String merOrderId;
		
		//商户请求流水号
		private String respDate;
		
		//商户请求流水号 8029 发短信成功 
		private String resultCode;
		
		//T-成功，F-失败，P-未明
		private String resultMsg;
		
		//T-成功，F-失败，P-未明
		private String retFlag;
		
		//签约协议号
		private String protocolId;
		
		//短信发送编号
		private String smsSendNo;
		
		
		
		
		
		
}
