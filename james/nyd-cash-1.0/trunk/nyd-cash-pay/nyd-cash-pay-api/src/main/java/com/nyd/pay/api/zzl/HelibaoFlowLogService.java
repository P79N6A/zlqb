package com.nyd.pay.api.zzl;

import java.util.Map;



public interface HelibaoFlowLogService<T> {
	
	
	public void saveFlow(String bizType,String orderNumber,Map reqContent,Map respContent,String remark,String custInfoId);
	
	
//	public void deleteSql();

}
