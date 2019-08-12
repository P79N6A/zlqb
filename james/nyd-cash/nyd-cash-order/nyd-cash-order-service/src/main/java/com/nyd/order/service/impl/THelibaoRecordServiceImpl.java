package com.nyd.order.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.order.api.zzl.THelibaoRecordService;
import com.nyd.order.dao.mapper.THelibaoRecordMapper;
import com.nyd.order.entity.Order;
import com.nyd.order.model.OrderCashOut;
import com.nyd.order.model.THeLiBaoLoanRecord;
import com.nyd.order.model.THelibaoRecord;
import com.nyd.order.model.common.ChkUtil;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.order.model.order.ImageUrlVo;
import com.nyd.user.api.zzl.UserForSLHServise;
import com.nyd.user.api.zzl.UserForZQServise;
import com.nyd.user.model.t.UserInfo;
import com.nyd.zeus.api.zzl.HelibaoEntrustedLoanService;
import com.nyd.zeus.model.helibao.util.StatusConstants;
import com.nyd.zeus.model.helibao.vo.entrustedloan.MerchantUserQueryResVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.MerchantUserQueryVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.MerchantUserResVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.MerchantUserUploadResVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.MerchantUserUploadVo;
import com.nyd.zeus.model.helibao.vo.entrustedloan.MerchantUserVo;

@Transactional
@Service(value ="tHelibaoRecordService")
public class THelibaoRecordServiceImpl<T> implements THelibaoRecordService<T> {
	private Logger logger = LoggerFactory.getLogger(THelibaoRecordServiceImpl.class);
	@Autowired
    private THelibaoRecordMapper<THelibaoRecord> mapper;
	@Autowired
	private UserForSLHServise userForSLHServise;
	@Autowired
	HelibaoEntrustedLoanService helibaoEntrustedLoanService;
	@Autowired
	private UserForZQServise userForZQServise;
	private static final String SUCCESS = "0000";
	private static final String REGISTER_SUCCESS="INIT";
	private static final String UPLOAD_SUCCESS="UPLOADED";

	private static final String USER_SUCCESS = "AVAILABLE";
	public THelibaoRecordMapper<THelibaoRecord> getMapper() {
		return mapper;
	}

	@Override
	public THelibaoRecord getTHelibaoRecord(String userId) {
		THelibaoRecord entity= new THelibaoRecord();
		try {
			 entity =	mapper.getEntityByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}


	@Override
	public int insertOrderRecord(THelibaoRecord T) {
		return mapper.insertOrderRecord(T);
	}


	@Override
	public int insertTHeLiBaoLoanRecord(THeLiBaoLoanRecord T) {
		return mapper.insertOrderLoanRecord(T);
	}


	@Override
	public THeLiBaoLoanRecord getTHeLiBaoLoanRecord(String orderId) {
		THeLiBaoLoanRecord entity= new THeLiBaoLoanRecord();
		try {
			 entity =	mapper.getOrderLoanRecord(orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}


	@Override
	public OrderCashOut getOrder(String orderId) {
		OrderCashOut entity= new OrderCashOut();
		try {
			 entity =	mapper.getOrderCashOut(orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}


	@Override
	public CommonResponse userRegister(THelibaoRecord vo) {
		CommonResponse common = new CommonResponse();
		//File ocrFornt = null;
		//File ocrOppo = null;
		try {
			if(vo.getFailCount()==null) {
				vo.setFailCount(0);
			}
			//THelibaoRecord helibaoCustRecord = BeanCommonUtils.copyProperties(vo, THelibaoRecord.class);
			if(vo.getFailCount() !=null&&vo.getFailCount() > 20 ) {
				
				logger.error("调用合利宝注册错误次数超限,参数:{}",JSON.toJSONString(vo));
				return common;
			}
			// 合利宝注册
			if(vo.getStatus() == 0){
				MerchantUserVo mVo = merChantUserRequest(vo.getUserId());
				MerchantUserResVo merResult = helibaoEntrustedLoanService.userRegister(mVo);
				if (REGISTER_SUCCESS.equals(merResult.getRt7_userStatus()) && SUCCESS.equals(merResult.getRt2_retCode())
				) {
					//调用成功、修改客户合利宝流水表状态
					vo.setStatus(1);
					vo.setUpdateTime(new Date());
					vo.setUserNumber(ChkUtil.isEmpty(merResult.getRt6_userId()) ? "" : merResult.getRt6_userId());
						mapper.updateBySelective(vo);
					vo.setStatus(1);
				} else {
					logger.error("调用合利宝注册，返回异常，请求参数{}，响应参数", JSONObject.toJSONString(mVo), JSONObject.toJSONString(merResult));
					vo.setStatus(0);
					vo.setUpdateTime(new Date());
					vo.setFailCount(vo.getFailCount() + 1);
						mapper.updateBySelective(vo);
					common.setSuccess(false);
					common.setCode(StatusConstants.ERROR_CODE);
					return common;
				}
			}
			// 合利宝认证 身份证正面
			ImageUrlVo ocrInfo = queryImgUrl(vo.getUserId());
			 MerchantUserUploadVo merVo = new MerchantUserUploadVo();
			if(vo.getStatus()==1) { 
				if(ocrInfo.getFrontUrl().trim()==""||ocrInfo.getFrontUrl()==null) {
					common.setSuccess(false);
					common.setCode(StatusConstants.ERROR_CODE);
					logger.info("mongo 没有该用户身份证信息 ！");
					return common;
				}
					merVo.setP4_userId(vo.getUserNumber());
					merVo.setP6_credentialType("FRONT_OF_ID_CARD");
				MerchantUserUploadResVo helibaoFrontResult = helibaoEntrustedLoanService.userUpload(merVo, ocrInfo.getFrontUrl());
				if (UPLOAD_SUCCESS.equals(helibaoFrontResult.getRt7_credentialStatus()) &&
						SUCCESS.equals(helibaoFrontResult.getRt2_retCode())
						&& !ChkUtil.isEmpty(vo.getUserNumber())) {

					vo.setStatus(2); 
					vo.setUpdateTime(new Date());
						mapper.updateBySelective(vo);
					vo.setStatus(2);
				}else {
					logger.error("调用合利宝上传资质正面异常，请求参数{}，响应参数{}", JSONObject.toJSONString(vo)
							, JSONObject.toJSONString(helibaoFrontResult));
					vo.setFailCount(vo.getFailCount() + 1);
					vo.setUpdateTime(new Date());
						mapper.updateBySelective(vo);
					common.setSuccess(false);
					common.setCode(StatusConstants.ERROR_CODE);
					return common;
				}
			}
			// 合利宝认证身份证反面
			if(vo.getStatus() == 2) {
				if(ocrInfo.getBackUrl().trim()==""||ocrInfo.getBackUrl()==null) {
					common.setSuccess(false);
					common.setCode(StatusConstants.ERROR_CODE);
					logger.info("mongo 没有该用户身份证信息 ！");
					return common;
				}
				merVo.setP6_credentialType("BACK_OF_ID_CARD");
				merVo.setP4_userId(vo.getUserNumber());
				MerchantUserUploadResVo helibaoOppoResult = helibaoEntrustedLoanService.userUpload(merVo, ocrInfo.getBackUrl());
				if (UPLOAD_SUCCESS.equals(helibaoOppoResult.getRt7_credentialStatus())
						&& SUCCESS.equals(helibaoOppoResult.getRt2_retCode())) {
					vo.setStatus(3);
					vo.setUpdateTime(new Date());
						mapper.updateBySelective(vo);
					common.setSuccess(true);
					common.setCode(StatusConstants.SUCCESS_CODE);
				}  else {
					logger.error("调用合利宝上传资质反面异常，请求参数{}，响应参数{}", JSONObject.toJSONString(vo)	, JSONObject.toJSONString(helibaoOppoResult));
					vo.setUpdateTime(new Date());
						mapper.updateBySelective(vo);
					common.setSuccess(false);
					common.setCode(StatusConstants.ERROR_CODE);

				}
			}
			if(vo.getStatus()==3) {
				vo.setStatus(4);
				vo.setUpdateTime(new Date());
				mapper.updateBySelective(vo);	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			throw new RuntimeException();
		}
		return common;
	}


	private MerchantUserVo merChantUserRequest(String userId) {
		MerchantUserVo mVo = new MerchantUserVo();
		// 查询用户信息 
		UserInfo u =userForSLHServise.getInfoUserId(userId);
		mVo.setP4_legalPerson(u.getUserName());
		mVo.setP5_legalPersonID(u.getIdCard());
		mVo.setP6_mobile(u.getPhone());
		mVo.setP7_business("B2C");
		return mVo;
	}


	@Override
	public List<THelibaoRecord> getHelibaoRecordFail() {
		List<THelibaoRecord> data;
		try {
			 data = mapper.getallUncertifiedOrder();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return data;
	}


	private ImageUrlVo queryImgUrl(String userId) {
		com.nyd.user.model.vo.ImageUrlVo view =new com.nyd.user.model.vo.ImageUrlVo();
		view.setUserId(userId);
		ImageUrlVo vo =new ImageUrlVo();
		try {
			view = userForZQServise.queryImgUrl(view).getData();
			  vo.setFrontUrl(view.getFrontUrl());
			  vo.setBackUrl(view.getBackUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return vo;
	}
		@Override
		public List<THelibaoRecord> getHelibaoRecordSuccess() {
			List<THelibaoRecord> list;
			try {
				list = mapper.getallSuccessOrder();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return list;
		}

		@Override
		public  CommonResponse queryHelibaoResult(THelibaoRecord vo) {
			CommonResponse common = new CommonResponse();
			THelibaoRecord helibaoCustRecord = new THelibaoRecord();
			try {
				MerchantUserQueryVo userVo = new MerchantUserQueryVo();
				userVo.setP4_userId(vo.getUserNumber());
				userVo.setP3_orderId(vo.getOrderNo());
				MerchantUserQueryResVo queryResult = helibaoEntrustedLoanService.userQuery(userVo);
				if(USER_SUCCESS.equals(queryResult.getRt7_userStatus())
						&& SUCCESS.equals(queryResult.getRt2_retCode())){
					helibaoCustRecord.setUserId(vo.getUserId());
					helibaoCustRecord.setStatus(5);
					helibaoCustRecord.setUpdateTime(new Date());
					mapper.changeStatusByCustId(helibaoCustRecord);
					common.setSuccess(true);
					common.setCode(StatusConstants.SUCCESS_CODE);
				}else {
					logger.error("用户资质查询返回异常，响应参数{}，客户id{}，用户id{}", JSONObject.toJSONString(queryResult)
					,vo.getUserId(),helibaoCustRecord.getUserNumber());

					common.setSuccess(false);
					common.setCode(StatusConstants.ERROR_CODE);
					return common;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("用户资质查询异常，异常信息,客户id{}", e.getMessage(),vo.getUserId());
			}
			return common;
		}

		@Override
		public void updateStatus(String orderId) {
			try {
				mapper.updateOrderStatus(orderId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
}
