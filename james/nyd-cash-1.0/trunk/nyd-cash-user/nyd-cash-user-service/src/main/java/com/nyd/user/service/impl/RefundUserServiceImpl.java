package com.nyd.user.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.member.api.MemberLogContract;
import com.nyd.member.model.MemberLogModel;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderDetailContract;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.WithholdOrderInfo;
import com.nyd.user.api.RefundUserContract;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.mapper.RefundUserMapper;
import com.nyd.user.entity.RefundUser;
import com.nyd.user.model.RefundInfo;
import com.nyd.user.model.RefundUserDto;
import com.nyd.user.model.RefundUserInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.model.dto.WithholdResultDto;
import com.nyd.user.model.vo.RefundOrClickVipVo;
import com.nyd.user.service.RefundService;
import com.nyd.user.service.UserService;
import com.nyd.user.service.consts.UserConsts;
import com.nyd.user.service.util.DateUtil;
import com.nyd.user.service.util.RestTemplateApi;
import com.nyd.user.service.util.UserProperties;
import com.tasfe.framework.support.model.ResponseData;
@Service(value ="refundUserContract")
public class RefundUserServiceImpl implements RefundUserContract{
	
	private static Logger logger = LoggerFactory.getLogger(RefundUserServiceImpl.class);
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private OrderContract orderContract;
	@Autowired
	private OrderDetailContract orderDetailContract;
	
	@Autowired
	private UserProperties  userProperties;
	
	@Autowired
	private RestTemplateApi restTemplateApi;
	
	@Autowired
	private RefundUserMapper refundUserMapper;	
	
	@Autowired
	private MemberLogContract memberLogContract;
	
	@Autowired
    private RedisTemplate redisTemplate;
	
	@Autowired
	private UserService userService;
	@Autowired
	private RefundService refundService;
	
	public static String KEY =	"nyd:refunduser";
	
	

	@Override
	public ResponseData findRefundCash(RefundUserDto dto) {
		logger.info("退款管理添加用户接口前端传入参数"+JSON.toJSONString(dto));	
		ResponseData data = ResponseData.success();
		String accountNumber =dto.getAccountNumber();
		String userName = dto.getUserName();
		if(StringUtils.isBlank(accountNumber) || StringUtils.isBlank(userName)) {
			logger.info("参数不能为空");
			return this.returnError(UserConsts.PARAM_ERROR);		 
		}
		List<AccountDto> accountList = null;
		try {
			accountList = accountDao.findByAccountNumber(accountNumber);
		} catch (Exception e) {
			logger.info("find account error",e);
			return this.returnError( UserConsts.DB_ERROR_MSG);	
		}
		if(accountList == null || accountList.size() == 0) {
			return this.returnError("手机号错误,没有查到该用户的信息");	
		}
		String userId = accountList.get(0).getUserId();		
		if(StringUtils.isBlank(userId)) {
			logger.info("uerId为空");
			return this.returnError("用户信息为空");	
		}
		try {
			List<UserInfo> user = userService.findByUserId(userId);
			if(user != null && user.size() > 0) {
				if(!dto.getUserName().equals(user.get(0).getRealName())) {
					return this.returnError("用户手机号或姓名不符");	
				}
			}
		} catch (Exception e) {
			logger.info("查询用户信息异常：" + e.getMessage());
		}
		ResponseData<MemberLogModel>  modelData = memberLogContract.getMemberLogByUserId(userId);
		if("1".equals(modelData.getStatus())) {
			logger.info("调用 ordercontract error");
			return this.returnError(UserConsts.DB_ERROR_MSG);
		}
		String  memberId = null;
		if("0".equals(modelData.getStatus()) && modelData.getData() != null) {
			  memberId = modelData.getData().getMemberId();	
			  if(StringUtils.isBlank(memberId)) {
				  logger.info("没有代扣记录"+accountNumber);
			  }
		}			
	     String payOrderNo = null;			
	     ResponseData<WithholdOrderInfo>  withholdData = orderContract.findWithholdOrderByMemberIdDesc(memberId);
		 if("0".equals(withholdData.getStatus()) && withholdData.getData() != null) {
			 payOrderNo  = withholdData.getData().getPayOrderNo();
		 }else {	
			 return this.returnError("该用户无缴费记录");
		 }				 
		Map<String, Object> param = new HashMap<String, Object>();
		int [] state = {3};//代扣成功
		param.put("payOrderNo", payOrderNo);
		param.put("state", state);
		String url = userProperties.getQueryAllWithholdOrder();//代扣记录流水url
		logger.info("查询代扣评估费记录流水传入参数{},url{}",JSON.toJSONString(param),url);			
		ResponseData resp = restTemplateApi.postForObject(url, param,ResponseData.class);
		logger.info("响应信息:" + JSON.toJSONString(resp));
		List<WithholdResultDto> withholdList = new ArrayList<WithholdResultDto>();
		if(resp != null &&  "0".equals(resp.getStatus())) {
			String json = JSON.toJSONString(resp.getData());
			if(StringUtils.isBlank(json)) {
				return this.returnError("该用户无缴费记录");
			}
			withholdList = JSON.parseArray(json, WithholdResultDto.class);
		}else {
			return this.returnError(UserConsts.DB_ERROR_MSG);
		}
		BigDecimal refundAmonut = BigDecimal.ZERO;
		for(WithholdResultDto  withhold :withholdList) {
			if("评估报告".equals(withhold.getBusinessOrderType())) {
				refundAmonut =refundAmonut.add(new BigDecimal(withhold.getAmount()));
			}
		}			
		redisTemplate.opsForValue().set(KEY+accountNumber, payOrderNo,30*24*60,TimeUnit.MINUTES);
		return this.returnSuccess(refundAmonut, "代扣成功的金额");
											
	}
	
	
	private ResponseData<RefundUserDto> returnError(String message){
		ResponseData data = ResponseData.error(message);
		RefundUserDto result = new RefundUserDto();
		return data.setData(BigDecimal.ZERO);
	}
	
	private ResponseData<RefundUserDto> returnSuccess(BigDecimal refundAmonut,String message){
		ResponseData data = ResponseData.success(message);
		return data.setData(refundAmonut);
		
	}


	@Override
	public ResponseData addUser(RefundUserDto dto) {
		logger.info("添加白名单用户前端传入参数"+JSON.toJSONString(dto));	
		ResponseData data = ResponseData.success();
		String accountNumber =dto.getAccountNumber();
		String userName = dto.getUserName();
		BigDecimal refundAmonut = dto.getRefundAmonut();
		if(StringUtils.isBlank(accountNumber) || StringUtils.isBlank(userName) || refundAmonut == null) {
			logger.info("参数不能为空");
			return this.returnError(UserConsts.PARAM_ERROR);	
		}
		if(refundAmonut.compareTo(BigDecimal.ZERO) == 0) {
			logger.info("退款金额不能为0");
			return this.returnError("退款金额不能为0");	
		}
		RefundUser refundUser = new RefundUser();
		List<AccountDto> accountList = null;
		try {
			accountList = accountDao.findByAccountNumber(accountNumber);
		} catch (Exception e) {
			logger.info("find account error",e);
			return this.returnError( UserConsts.DB_ERROR_MSG);	
		}
		if(accountList == null || accountList.size() == 0) {
			return this.returnError("手机号错误,没有查到该用户的信息");	
		}
		String userId = accountList.get(0).getUserId();	
		//校验用户是否已存在白名单
		RefundUserInfo check = new RefundUserInfo();
		check.setUserId(userId);
		try {
			RefundUserInfo res = refundUserMapper.getRefundUserByUserId(check);
			if(res != null) {
				return this.returnError("白名单信息已存在");	
			}
		} catch (Exception e1) {
			logger.info("查询用户白名单信息失败：" + e1.getMessage());
		}
		refundUser.setUserId(userId);
		refundUser.setUserName(userName);
		refundUser.setAccountNumber(accountNumber);
		refundUser.setRefundAmonut(refundAmonut);
		refundUser.setIfRemove(0);
		refundUser.setDeleteFlag(0);
		try {
			refundUserMapper.saveRefundUser(refundUser);
			//停止代扣
			Map<String, Object> param = new HashMap<String, Object>();
			String payOrderNo =(String)redisTemplate.opsForValue().get(KEY+accountNumber);
			param.put("payOrderNo", payOrderNo);
			String url = "http://" + userProperties.getCommonPayIp() + ":" + userProperties.getCommonPayPort() + "/common/pay/stopPayStateByNo";
			logger.info("停止代扣传入参数{},url{}",JSON.toJSONString(param),url);	
			ResponseData resp = restTemplateApi.postForObject(url, param,ResponseData.class);
			logger.info("响应信息:" + JSON.toJSONString(resp));
			if("0".equals(resp.getStatus())) {
				logger.info("停止代扣成功"+accountNumber);
				redisTemplate.delete(KEY+accountNumber);
			}else {
				logger.info("停止代扣失败"+accountNumber);
			}
		} catch (Exception e) {
			logger.info("save refunduser error",e);
			data =ResponseData.error(UserConsts.DB_ERROR_MSG);
		}	
		return data;
	}


	@Override
	public ResponseData updateUser(RefundUserDto dto) {
		logger.info("移除用户前端传入参数"+JSON.toJSONString(dto));
		ResponseData data = ResponseData.success();
		String accountNumber = dto.getAccountNumber();
		int ifRemove =dto.getIfRemove();
		if(StringUtils.isBlank(accountNumber)) {
			logger.info("accountNumber参数不能为空");
			return this.returnError(UserConsts.PARAM_ERROR);
		}
		if(ifRemove == 0 || ifRemove ==  1) {
			RefundUser refundUser = new RefundUser();
			refundUser.setAccountNumber(accountNumber);
			refundUser.setIfRemove(ifRemove);
			try {
				refundUserMapper.updateRefundUser(refundUser);
			} catch (Exception e) {
				logger.info("update refunduser error",e);
				data.error(UserConsts.DB_ERROR_MSG);
			}
			return data;
		}else {
			logger.info("ifRemove 参数不合法");
			return this.returnError("ifRemove 参数不合法");			
		}
	}


	@Override
	public ResponseData selectRefundUser(RefundUserDto dto) {
		logger.info("用户列表查询前端传入参数"+JSON.toJSONString(dto));
		ResponseData response = ResponseData.success();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("accountNumber", dto.getAccountNumber());
		map.put("userName", dto.getUserName());		         
        //设置默认分页条件
		Integer  pageNum = dto.getPageNum();
		Integer  pageSize = dto.getPageSize();
        if (pageNum == null || pageNum== 0) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 20;
        }
        Integer total = 0;
        try {
        	total =refundUserMapper.queryRefundUserInfoCount(map);
		} catch (Exception e) {
			logger.info("select refunduser error",e);
		}
        if (total == null) {
            total = 0;
        }
        PageHelper.startPage(pageNum, pageSize);
        logger.info("查询总个数 total is " + total);
        List<RefundUser> refundList = new ArrayList<RefundUser>();
        try {
        	refundList = refundUserMapper.queryRefundUserInfo(map);
		} catch (Exception e) {
			logger.info("select refunduser error",e);
		}
        PageInfo pageInfo = new PageInfo(refundList);
        pageInfo.setTotal(total);
        response.setData(pageInfo);
        //logger.info(JSON.toJSONString(response));
		return response;
	}


	@Override
	public ResponseData haveInWhiteList(RefundUserInfo info) {
		ResponseData response = ResponseData.success();
		boolean flag = false;
		try {
			Integer count = refundUserMapper.haveInWhiteList(info);
			if(count != null && count > 0) {
				flag = true;
			}
		} catch (Exception e) {
			logger.error("判断白名单异常：" + e.getMessage());
			return response.error("请求查询异常!");
		}
		response.setData(flag);
		logger.info("获取用户白名单返回结果"+JSON.toJSONString(response));
		return response;
	}
	
	@Override
	public ResponseData ifShowRefund(RefundUserInfo info) throws Exception {
		ResponseData response = ResponseData.success();
		RefundOrClickVipVo vo = new RefundOrClickVipVo(false,false,false);
		ResponseData<List<RefundInfo>> refundInfos = null;
		try {
			refundInfos = refundService.getRefundByUserId(info.getUserId());
		} catch (Exception e) {
			logger.error("查询退款申请记录异常：" + e.getMessage());
		}
		if(refundInfos != null && refundInfos.getData() != null && refundInfos.getData().size() >0) {
			if(DateUtil.getDays(refundInfos.getData().get(0).getCreateTime(), new Date()) < 30) {
				vo.setIfShowRefund(true);
				vo.setIfRefund(true);
				logger.info("响应结果1：" + JSON.toJSONString(vo));
				return response.setData(vo);
			}
		}
		ResponseData<List<OrderInfo>> order = orderContract.getLastOrderByUserId(info.getUserId());
		if(order != null && order.getData() != null && order.getData().size() > 0) {
			OrderInfo or = order.getData().get(0);
			if(DateUtil.getDays(or.getLoanTime(), new Date())  < 30) {
				ResponseData<OrderDetailInfo> details = orderDetailContract.getOrderDetailByOrderNo(or.getOrderNo());
				if(details != null && details.getData() != null) {
					OrderDetailInfo de = details.getData();
					logger.info("查询订单详情为：" + JSON.toJSONString(de));
					if(new Integer(1).compareTo(de.getClickVipFlag()) == 0) {
						vo.setIfClickVip(true);
						vo.setIfShowRefund(true);
					}else {
						vo.setIfShowRefund(true);
					}
				}
				logger.info("响应结果2：" + JSON.toJSONString(vo));
				return response.setData(vo);
			}
		}
		logger.info("响应结果3：" + JSON.toJSONString(vo));
		return response.setData(vo);
	}
	
	@Override
	public ResponseData updateClickVip(RefundUserInfo info) throws Exception {
		ResponseData response = ResponseData.success();
		ResponseData<List<RefundInfo>> refundInfos = null;
		try {
			refundInfos = refundService.getRefundByUserId(info.getUserId());
		} catch (Exception e) {
			logger.error("查询退款申请记录异常：" + e.getMessage());
		}
		if(refundInfos != null && refundInfos.getData() != null && refundInfos.getData().size() >0) {
			if(DateUtil.getDays(refundInfos.getData().get(0).getCreateTime(), new Date()) < 30) {
				return response.error("存在退款申请，无法认领VIP新口子");
			}
		}
		ResponseData<List<OrderInfo>> order = orderContract.getLastOrderByUserId(info.getUserId());
		if(order != null && order.getData() != null && order.getData().size() > 0) {
			OrderInfo or = order.getData().get(0);
			if(DateUtil.getDays(or.getLoanTime(), new Date())  < 30) {
				ResponseData detail = orderDetailContract.updateClickVip(or.getOrderNo());
				if(detail != null && detail.getStatus().equals("0")) {
					return response.success();
				}
			}
		}
		return response.error("更新VIP新口子标识失败");
	}
	
	@Override
	public void stopWithhold(String accountNumber) {
		//停止代扣
		Map<String, Object> param = new HashMap<String, Object>();
		String payOrderNo =(String)redisTemplate.opsForValue().get(KEY+accountNumber);
		param.put("payOrderNo", payOrderNo);
		String url = "http://" + userProperties.getCommonPayIp() + ":" + userProperties.getCommonPayPort() + "/common/pay/stopPayStateByNo";
		logger.info("停止代扣传入参数{},url{}",JSON.toJSONString(param),url);	
		ResponseData resp = restTemplateApi.postForObject(url, param,ResponseData.class);
		logger.info("响应信息:" + JSON.toJSONString(resp));
		if("0".equals(resp.getStatus())) {
			logger.info("停止代扣成功"+accountNumber);
			redisTemplate.delete(KEY+accountNumber);
		}else {
			logger.info("停止代扣失败"+accountNumber);
		}
	}

}
