package com.nyd.user.model.uniteregister;

import java.io.Serializable;

import com.nyd.user.model.enums.UniteRegisterRespCode;

import lombok.Data;

@Data
public class UniteRegisterResponseData implements Serializable{
		
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1977337902423023773L;

	Integer code;

	 String message;
	 
	 public static UniteRegisterResponseData success() { 
		 UniteRegisterResponseData  responseData  = new UniteRegisterResponseData();
		 responseData.setCode(UniteRegisterRespCode.REGISTER_NEW_SUCCESS.getCode());
		 responseData.setMessage(UniteRegisterRespCode.REGISTER_NEW_SUCCESS.getMessage());
		 return responseData;
	 }
	 
	 public static UniteRegisterResponseData error() { 
		 UniteRegisterResponseData  responseData  = new UniteRegisterResponseData();
		 responseData.setCode(UniteRegisterRespCode.REGISTER_FAIL.getCode());
		 responseData.setMessage(UniteRegisterRespCode.REGISTER_FAIL.getMessage());
		 return responseData;
	 }
	 
	 public static UniteRegisterResponseData error(UniteRegisterRespCode code) {
		 UniteRegisterResponseData responseData = new UniteRegisterResponseData();
	        responseData.setCode(code.getCode());
	        responseData.setMessage(code.getMessage());
	        return responseData;
	    }
	    public static UniteRegisterResponseData error(UniteRegisterRespCode code , String msg) {
	    	UniteRegisterResponseData responseData = new UniteRegisterResponseData();
	        responseData.setCode(code.getCode());
	        responseData.setMessage(msg);
	        return responseData;
	    }
}
