package com.nyd.capital.model.dld;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCallBackParams implements Serializable{

	String orderid;
	String code;
	String LentTime;
	String msg;
	String signature;
	String dldShopKey;
	String fundCode;
	
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append(this.getCode());
		sb.append(this.getMsg());
		sb.append(this.getOrderid());
		sb.append(this.getDldShopKey());
		return sb.toString();
	}
	
	public Map<String,Object> getSignMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", this.getCode());
		map.put("LentTime", this.getLentTime());
		map.put("msg", this.getMsg());
		map.put("orderid", this.getOrderid());
		map.put("dldKey", this.getDldShopKey());
		return map;
		
	}
	
}
