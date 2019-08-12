package com.zhiwang.zfm.service.impl.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhiwang.zfm.common.util.ArithUtil;
import com.zhiwang.zfm.common.util.ChkUtil;
import com.zhiwang.zfm.common.util.DateUtils;
//import com.zhiwang.zfm.dao.sys.SysParameterMapper;
import com.zhiwang.zfm.entity.sys.SysParameter;
import com.zhiwang.zfm.entity.sys.query.SysParameterSearchForm;
import com.zhiwang.zfm.service.api.common.CommonService;
import com.zhiwang.zfm.service.api.redis.RedisService;

/**   
 * @ClassName:  CommonServiceImpl  
 * @Description: 共用service接口 实现
 * @Author: taohui 
 * @CreateDate: 2018年12月3日 下午2:36:12 
 * @Version: v1.0  
 */
@Service
@Transactional
public class CommonServiceImpl implements CommonService{

	@Autowired
	private RedisService redisService;
	
	/*@Autowired
	private SysParameterMapper<SysParameter> sysParameterMapper;*/
	
	@Override
	public boolean checkDayMaxAmt(double withdrawMoney) throws Exception {
		
		return false;//checkDayMaxAmtImpl(withdrawMoney);
	}

	@Override
	public void updateDayMaxAmt(double withdrawMoney) throws Exception {
		updateDayMaxAmtImpl(withdrawMoney);
	}

	/** 
	 * @Description: 校验提现金额是否超过当日可提现金额
	 * @Author: taohui   
	 * @param maxAmt
	 * @param subAmt
	 * @return
	 * @CreateDate: 2018年12月8日 上午10:31:13
	 * @throws Exception 异常
	 */
	/*private synchronized boolean checkDayMaxAmtImpl(double subAmt) throws Exception{
		
		// 查询配置文件配置
		double maxAmtLimit = 0d;   //数据库中配置当日提现额度
		double maxAmt = 0d;  //今日已经提现额度
		SysParameterSearchForm sysForm = new SysParameterSearchForm();
		List<SysParameter> sysParameterList = sysParameterMapper.pageData(sysForm);
		if (sysParameterList.size() > 0) {
			maxAmtLimit = sysParameterList.get(0).getWithdrawCashAmount().doubleValue();
		}
		
		String value = redisService.get("order-day-maxAmt");
		String todayTime = DateUtils.getCurrentTime(DateUtils.STYLE_3);
		if (ChkUtil.isEmpty(value)) {
			// 初始化
			redisService.set("order-day-maxAmt", todayTime + maxAmt,24*3600);
			return maxAmtLimit >= subAmt;

		} else {
			String timeStamp = value.substring(0, 8);
			if (todayTime.equals(timeStamp)) {
				double daySubAmt = Double.parseDouble(value.substring(8, value.length()));
				return maxAmtLimit >= ArithUtil.add(daySubAmt, subAmt);
			} else {
				redisService.set("order-day-maxAmt", todayTime + maxAmt,24*3600);
				return maxAmtLimit >= subAmt;
			}
		}
	}*/
	
	
	/** 
	 * @Description: 更新当日已经提现金额
	 * @Author: taohui   
	 * @param subAmt
	 * @throws Exception
	 * @CreateDate: 2018年12月8日 上午11:33:46
	 * @throws Exception 异常
	 */
	private synchronized void updateDayMaxAmtImpl(double subAmt) throws Exception{
		
		// 今日已经提现额度
		double maxAmt = 0d;
		
		String todayTime = DateUtils.getCurrentTime(DateUtils.STYLE_3);
		String value = redisService.get("order-day-maxAmt");
		if (ChkUtil.isEmpty(value)) {
			redisService.set("order-day-maxAmt", todayTime + ArithUtil.add(maxAmt, subAmt),24*3600);
		} else {
			String timeStamp = value.substring(0, 8);
			if (todayTime.equals(timeStamp)) {
				double daySubAmt = Double.parseDouble(value.substring(8, value.length()));
				redisService.set("order-day-maxAmt", todayTime + ArithUtil.add(daySubAmt, subAmt),24*3600);
			} else {
				redisService.set("order-day-maxAmt", todayTime + ArithUtil.add(maxAmt, subAmt),24*3600);
			}
		}
	}
}
