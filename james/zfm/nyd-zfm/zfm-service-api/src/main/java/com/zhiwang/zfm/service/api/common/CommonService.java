package com.zhiwang.zfm.service.api.common;

/**   
 * @ClassName:  CommonService  
 * @Description: 共用service接口 
 * @Author: taohui 
 * @CreateDate: 2018年12月3日 下午2:36:12 
 * @Version: v1.0  
 */
public interface CommonService {
	
	/** 
	 * @Description: 校验提现金额是否超过当日可提现金额(取redis里面当日已提现金额  数据库里面配的最大可提现金额)
	 * @Author: taohui   
	 * @param withdrawMoney  提现金额
	 * @return  true  可提现  false 不可提现
	 * @throws Exception
	 * @CreateDate: 2018年12月8日 上午11:25:32
	 * @throws Exception 异常
	 */
	public boolean checkDayMaxAmt(double withdrawMoney) throws Exception;
	
	/** 
	 * @Description: 更新当日已经提现金额  (redis里面只增加)
	 * @Author: taohui   
	 * @param withdrawMoney  提现金额
	 * @throws Exception
	 * @CreateDate: 2018年12月8日 上午11:25:35
	 * @throws Exception 异常
	 */
	public void updateDayMaxAmt(double withdrawMoney) throws Exception;
}
