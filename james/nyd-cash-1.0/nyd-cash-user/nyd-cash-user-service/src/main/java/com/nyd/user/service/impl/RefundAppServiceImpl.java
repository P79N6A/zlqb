package com.nyd.user.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.application.api.QiniuContract;
import com.nyd.user.api.RefundAppContract;
import com.nyd.user.api.RefundUserContract;
import com.nyd.user.dao.mapper.RefundAmountMapper;
import com.nyd.user.dao.mapper.RefundAppMapper;
import com.nyd.user.dao.mapper.RefundMapper;
import com.nyd.user.dao.mapper.RefundUserMapper;
import com.nyd.user.model.RefundAmountInfo;
import com.nyd.user.model.RefundAppInfo;
import com.nyd.user.model.RefundInfo;
import com.nyd.user.model.RefundUserDto;
import com.nyd.user.model.RefundUserInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.model.vo.RefundAppVo;
import com.nyd.user.service.RefundAppService;
import com.nyd.user.service.RefundService;
import com.nyd.user.service.UserService;
import com.nyd.user.service.consts.UserConsts;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
@Service("refundAppContract")
public class RefundAppServiceImpl implements RefundAppService,RefundAppContract {
    private static Logger logger = LoggerFactory.getLogger(RefundAppServiceImpl.class);
	
	@Autowired
	RefundAppMapper refundAppMapper;
	@Autowired
	RefundAmountMapper refundAmountMapper;
	@Autowired
	RefundUserMapper refundUserMapper;
	@Autowired
	RefundMapper refundMapper;
	@Autowired
	RefundService refundService;
	@Autowired
	RefundUserContract refundUserContract;
	
	@Autowired
	UserService userService;
	
	 @Autowired
	 QiniuContract qiniuContract;
	
	@Override
    public void save(RefundAppInfo refund) throws Exception{
		refund.setAppCode(generaterCode());
		ResponseData key = qiniuContract.base64Upload(refund.getAppLogoImge());
		if(key != null && key.getStatus().equals("0")) {
			refund.setAppLogo((String)key.getData());
		}
		refund.setAppLogoImge(null);
		logger.info("保存refund app 信息：" + JSON.toJSONString(refund));
		refundAppMapper.save(refund);
    }
	@Override
    public void update(RefundAppInfo refund) throws Exception{
		if(!StringUtils.isBlank(refund.getAppLogoImge()) && refund.getAppLogoImge().equals(refund.getAppLogo())) {
			refund.setAppLogoImge(null);
		}
		if(!StringUtils.isBlank(refund.getAppLogoImge())) {
			ResponseData key = qiniuContract.base64Upload(refund.getAppLogoImge());
			if(key != null && key.getStatus().equals("0")) {
				refund.setAppLogo((String)key.getData());
			}
		}
		refundAppMapper.update(refund);
	}
	@Override
    public ResponseData<List<RefundAppInfo>> queryRefundApp(RefundAppInfo info) throws Exception{
		ResponseData responseData = ResponseData.success();
		Integer pageNum = info.getPageNum();
        Integer pageSize = info.getPageSize();
        //设置默认分页条件
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 20;
        }
        Integer total = 0;
        try {
			total = refundAppMapper.queryRefundAppTotal(info);
		} catch (Exception e) {
			logger.error("查询口子列表个数异常：" + e.getMessage());
		}
        logger.info("查询总个数 total is " + total);
        if (total == null) {
            total = 0;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<RefundAppInfo> list = null;
        try {
            list = refundAppMapper.queryRefundApp(info);
        } catch (Exception e) {
        	logger.error("查询口子列表失败：" + e.getMessage());
            responseData = ResponseData.error("查询口子列表失败：" + e.getMessage());
            return responseData;
        }
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setTotal(total);
        responseData.setData(pageInfo);
        return responseData;
	}
	
	@Override
    public ResponseData getTaskListByUserId(RefundAppInfo info) throws Exception{
		logger.info("获取任务列表传入参数"+JSON.toJSONString(info));
		ResponseData responseData = ResponseData.success();
		if(info.getIfVipList() != null && info.getIfVipList().equals("Y")) {
			BigDecimal amount = null;
			try {
				amount = getRefundAmount(info);
			}catch(Exception e) {
				logger.error("获取缴费金额异常： " + e.getMessage());
				return responseData.error("获取缴费金额失败！");
			}
			RefundAppInfo appInfo = new RefundAppInfo();
			if(amount == null) {
				appInfo.setCount(10);
			}else {
				appInfo.setCount(getCountByAmount(amount));
			}
			List<RefundAppInfo> appList1 = new ArrayList<RefundAppInfo>();
			appList1 = refundAppMapper.getRefundAppListByCount(appInfo);
			if(appList1 == null || appList1.size() == 0) {
				return responseData.error("查询推荐APP列表失败");
			}
			for(RefundAppInfo app : appList1) {
				ResponseData data = qiniuContract.download(app.getAppLogo());
				if(data != null && data.getStatus().equals("0")) {
					app.setAppLogoUrl((String)data.getData());
				}
				//更新每日推荐数
				app.setRealRecomNumFlag(1);
				refundAppMapper.updateRecomNum(app);
			}
			RefundAppVo res1 = new RefundAppVo();
			res1.setAppList(appList1);
			responseData.setData(res1);
	        return responseData;
		}
		//判断是否有已存在申请
		RefundInfo refund = new RefundInfo();
		refund.setUserId(info.getUserId());
		List<RefundInfo> refundList = refundMapper.queryRefund(refund);
		//查询推荐app列表
		List<RefundAppInfo> appList = new ArrayList<RefundAppInfo>();
		//查询是否已存在申请
		if(refundList != null && refundList.size() > 0 ) {
			RefundInfo lis = refundList.get(0);
			if(lis.getRequestStatus().equals(1002) || lis.getRequestStatus().equals(1000) || lis.getRequestStatus().equals(999) || lis.getRequestStatus().equals(1001) ) {
				String appListStr = lis.getAppList();
				if(!StringUtils.isBlank(appListStr)) {
					String[] apps = appListStr.split(";");
					for(String app:apps) {
						RefundAppInfo temp = refundAppMapper.getRefundAppByAppCode(app);
						ResponseData data = qiniuContract.download(temp.getAppLogo());
						if(data != null && data.getStatus().equals("0")) {
							temp.setAppLogoUrl((String)data.getData());
						}
						appList.add(temp);
					}
				}
				RefundAppVo res = new RefundAppVo();
				res.setAppList(appList);
				res.setRequestStatus(lis.getRequestStatus());
				res.setRefundNo(lis.getRefundNo());
				responseData.setData(res);
				logger.info("获取任务列表返回结果"+JSON.toJSONString(responseData));
				return  responseData;
			}
		}
		//查询白名单信息
		RefundUserInfo whiteInfo = new RefundUserInfo();
		whiteInfo.setUserId(info.getUserId());
		RefundUserInfo userInfo = refundUserMapper.getRefundUserByUserId(whiteInfo);
		UserInfo user = null;
		if(userInfo == null) {
			List<UserInfo> users = userService.findByUserId(info.getUserId());
			if(users == null || users.size() == 0) {
				return responseData.error("没有查询到用户信息");
			}
			user = users.get(0);
			userInfo = new RefundUserInfo();
			//return responseData.error("查询信息失败");
			RefundUserDto dto = new RefundUserDto();
			dto.setAccountNumber(info.getAccountNumber());
			dto.setUserName(user.getRealName());
			ResponseData cash = refundUserContract.findRefundCash(dto);
			if(cash != null && cash.getStatus().equals("0")&& cash.getData() != null) {
				userInfo.setRefundAmonut((BigDecimal)cash.getData());
			}else {
				return responseData.error("获取缴费金额失败！");
			}
		}
		RefundAppInfo appInfo = new RefundAppInfo();
		/*if(userInfo.getRefundAmonut().compareTo(new BigDecimal(50)) < 0) {
			appInfo.setCount(3);
		}else {
			appInfo.setCount(5);
		}*/
		appInfo.setCount(getCountByAmount(userInfo.getRefundAmonut()));
		appList = refundAppMapper.getRefundAppListByCount(appInfo);
		if(appList == null || appList.size() == 0) {
			return responseData.error("查询推荐APP列表失败");
		}
		String appListStr = "";
		for(RefundAppInfo app : appList) {
			ResponseData data = qiniuContract.download(app.getAppLogo());
			if(data != null && data.getStatus().equals("0")) {
				app.setAppLogoUrl((String)data.getData());
				appListStr += app.getAppCode()+ ";" ;
			}
			//更新每日推荐数
			app.setRealRecomNumFlag(1);
			refundAppMapper.updateRecomNum(app);
		}
		//生成退款订单信息
		RefundInfo refundInfo = new RefundInfo();
		BeanUtils.copyProperties(userInfo, refundInfo);
		if(user != null) {
			refundInfo.setAccountNumber(info.getAccountNumber());
			refundInfo.setUserId(info.getUserId());
			refundInfo.setUserName(user.getRealName());
		}
		refundInfo.setAppList(appListStr);
		refundInfo.setAppName(info.getAppName());
		try {
			refundService.save(refundInfo);
		}catch(Exception e) {
			logger.info("save t_refund error",e);
		}
		RefundAppVo res = new RefundAppVo();
		res.setAppList(appList);
		res.setRequestStatus(999);
		responseData.setData(res);
        return responseData;
	}
	/**
	 * 根据缴费金额查询任务数
	 * @param amount
	 * @return
	 */
	private Integer getCountByAmount(BigDecimal amount) {
		RefundAmountInfo count = refundAmountMapper.getCountByAmount(amount);
		if(count != null && count.getRegisterCount() != null) {
			return count.getRegisterCount();
		}
		return 10;
	}
	/**
	 * 根据userId 查询缴费金额
	 * @param info
	 * @return
	 * @throws Exception
	 */
	private BigDecimal getRefundAmount(RefundAppInfo info) throws Exception{
		List<UserInfo> user = null;
		try {
			user = userService.findByUserId(info.getUserId());
		} catch (Exception e) {
			logger.error("查询用户信息失败:" + e.getMessage());
			throw e;
		}
		if(user != null && user.size() > 0) {
			RefundUserDto dto = new RefundUserDto();
			dto.setAccountNumber(info.getAccountNumber());
			dto.setUserName(user.get(0).getRealName());
			ResponseData data = refundUserContract.findRefundCash(dto);
			if(data != null && data.getStatus().equals("0")) {
				return (BigDecimal)data.getData();
			}
		}
		return null;
	}
	private String generaterCode() {
		String prx = "APP";
		int i = (int)((Math.random()*9+1)*1000);
		return prx+i;
	}
	@Override
	public void resetRealRecomNum() throws Exception {
		refundAppMapper.resetRealRecomNum();
	}
}
