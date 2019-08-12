package com.creativearts.nyd.pay.service.baofoo.util;

import java.util.HashMap;
import java.util.Map;

public class ParseStr{
	
	public static Map<?,?> ToMap(String Acpt){
		Map<String,String> ReturnBack = new HashMap<String,String> ();
		String[] Pairs = Acpt.split("&");
		for(String pair : Pairs) {
		  String[] keyval = pair.split("=");
		  if(keyval.length>1){
			  ReturnBack.put(keyval[0], keyval[1]);
		  }else{
			  ReturnBack.put(keyval[0], "");
		  }
			  
		}
		return ReturnBack;
	}
}