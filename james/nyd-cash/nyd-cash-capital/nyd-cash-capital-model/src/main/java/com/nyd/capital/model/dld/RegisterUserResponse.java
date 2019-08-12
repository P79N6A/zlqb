package com.nyd.capital.model.dld;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserResponse implements Serializable{

	private String code;
	private String msg;
	private boolean success;
	private FirstType data;
	private class FirstType{
		String respCode;
		String respMsg;
		String orderId;
		String status;
		SecondType data;
	}
	private class SecondType{
		String customerId;
	}
	
}
