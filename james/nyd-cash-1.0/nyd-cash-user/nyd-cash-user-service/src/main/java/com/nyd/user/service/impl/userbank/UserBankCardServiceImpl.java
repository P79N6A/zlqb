package com.nyd.user.service.impl.userbank;

import java.util.List;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.nyd.order.model.common.ChkUtil;
import com.nyd.order.model.common.DateUtils;
import com.nyd.user.api.zzl.UserBankCardService;
import com.nyd.user.api.zzl.UserSqlService;
import com.nyd.user.dao.mapper.UserBindMapper;
import com.nyd.user.model.vo.AppConfirmOpenVO;
import com.nyd.user.model.vo.AppPreCardVO;
import com.nyd.user.model.vo.UserBankInfo;
import com.nyd.zeus.api.zzl.HelibaoQuickPayService;
import com.nyd.zeus.model.helibao.util.StatusConstants;
import com.nyd.zeus.model.helibao.vo.pay.req.BindCardSendValidateCodeVo;
import com.nyd.zeus.model.helibao.vo.pay.req.ConfirmBindCardVo;
import com.nyd.zeus.model.helibao.vo.pay.req.QuickPayBindCardPreOrderVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.BindCardPreOrderResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.BindCardSendValidateCodeResponseVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.ConfirmBindCardResponseVo;
import com.tasfe.framework.support.model.ResponseData;

@Service(value = "userBankCardService")
public class UserBankCardServiceImpl implements UserBankCardService{

    private static Logger logger = LoggerFactory.getLogger(UserBankCardServiceImpl.class);

    @Autowired
	private UserSqlService userSqlService;
    
    @Autowired
    private HelibaoQuickPayService helibaoQuickPayService;
    
    @Autowired
    private UserBindMapper userBindMapper;
	
    @Override
	public ResponseData preBindingBank(AppPreCardVO vo) {
		logger.info("银行卡认证,预绑卡接口,请求参数:{}", JSONObject.toJSONString(vo));
		ResponseData common = new ResponseData();
		try {
			//判断 银行卡是否绑定
//			
			String  sql = "select * from t_user_bank where soure = 2 and default_flag = 0 and bank_account = '"+vo.getCardNumber()+"'";

			List<JSONObject> bankSeachCardList = userSqlService.query(sql);
			if(bankSeachCardList.size()>0){
				//判断银行卡绑定用户是否是当前用户
				if(!bankSeachCardList.get(0).getString("user_id").equals(vo.getUserId())){
					common.setCode(StatusConstants.BANKCARD_EXISTENCE_CODE);
					common.setMsg(StatusConstants.BANKCARD_EXISTENCE_CODE_MSG);
					common.setStatus("1");
					return common;
				}
			}
			//合利宝
			common = preHelibao(vo);
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("app用户预绑卡接口调用异常，请求参数为：id:{}", new Object[] {JSONObject.toJSONString(vo)});
			common.setCode(StatusConstants.ERROR_CODE);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setStatus("1");
		}
		return common;
		
		
	}
	
	
	/**
	 * 预绑卡 合利宝
	 * @param vo
	 * @return
	 * @throws Exception 
	 */
	private ResponseData preHelibao(AppPreCardVO vo) throws Exception {
		ResponseData common = new ResponseData();
		//商户订单号
		String merchantNumber = vo.getMerchantNumber();
		//查询用户信息
		String userSpl = "select * from t_user where user_id = '"+vo.getUserId()+"'";
		JSONObject userJson = userSqlService.queryOne(userSpl);
		
		//根据支付渠道查询银行卡
		String  sql = "select * from t_user_bank where soure = 2 and default_flag = 0 and user_id = '"+vo.getUserId()+"'";
		List<JSONObject> bankCardList = userSqlService.query(sql);
		//判断是否存在绑定合利宝银行卡
		if(bankCardList.size()>0){
			//判断当前银行卡是否有效
			common.setCode(StatusConstants.OPENACCOUNT_BINDCARD_EXISTENCE_CODE);
			common.setMsg(StatusConstants.OPENACCOUNT_BINDCARD_EXISTENCE_CODE_MSG);
			common.setStatus("1");
			return common;
		}
		//判断是否存在有效的预绑卡商户单号
		if(!ChkUtil.isEmpty(merchantNumber)){
			//存在 调用鉴权绑卡短信接口
			BindCardSendValidateCodeVo requestVo = new BindCardSendValidateCodeVo();
			requestVo.setP3_orderId(merchantNumber);
			logger.info("绑卡短信调用,请求参数:"+JSONObject.toJSONString(requestVo));
			BindCardSendValidateCodeResponseVo resultVo = helibaoQuickPayService.bindCardSendValidateCode(requestVo);
			logger.info("绑卡短信接口返回,返回参数:"+JSONObject.toJSONString(resultVo));
			//判断发送短信是否成功
			if("0000".equals(resultVo.getRt2_retCode())){
				common.setCode(StatusConstants.SUCCESS_CODE);
				common.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
				common.setStatus("0");
				common.setData(resultVo.getRt5_orderId());		//鉴权绑卡预下单 成功返回商户号 绑卡时入库
			}else{
				common.setCode(resultVo.getRt2_retCode());
				common.setMsg("获取短信验证码失败，失败原因："+resultVo.getRt3_retMsg());
				common.setStatus("1");
			}

		}else{
			//不存在调用合利宝鉴权绑卡预下单接口
			QuickPayBindCardPreOrderVo requestVo = new QuickPayBindCardPreOrderVo();
			requestVo.setP6_payerName(userJson.getString("real_name"));  //用户姓名
			requestVo.setP8_idCardNo(userJson.getString("id_number"));		//证件号码
			requestVo.setP9_cardNo(vo.getCardNumber()); 	//银行卡号
			requestVo.setP13_phone(vo.getMobile()); 	//银行预留手机号
			logger.info("鉴权预绑卡接口调用,请求参数:"+JSONObject.toJSONString(requestVo));
			BindCardPreOrderResponseVo resultVo = helibaoQuickPayService.quickPayBindCardPreOrder(requestVo);
			logger.info("鉴权预绑卡接口返回,返回参数:"+JSONObject.toJSONString(resultVo));
			//判断发送短信是否成功
			if("0000".equals(resultVo.getRt2_retCode())){
				if("SUCCESS".equals(resultVo.getSmsStatus())){
					common.setCode(StatusConstants.SUCCESS_CODE);
					common.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
					common.setStatus("0");
					common.setData(resultVo.getRt6_orderId());		//鉴权绑卡预下单 成功返回商户号 绑卡时入库
				}else{
					common.setCode(resultVo.getRt2_retCode());
					common.setMsg("获取短信验证码失败，失败原因："+resultVo.getSmsMsg());
					common.setStatus("1");
				}
			}else{
				common.setCode(resultVo.getRt2_retCode());
				common.setMsg("获取短信验证码失败，失败原因："+resultVo.getRt3_retMsg());
				common.setStatus("1");
			}

		}
		return common;
	}
	/**
	 * app确认绑卡
	 * @param vo
	 * @return
	 */
	@Override
	public ResponseData confirmBindCard(AppConfirmOpenVO vo) {
		logger.info("银行卡认证,确认绑卡接口,请求参数:{}", JSONObject.toJSONString(vo));
		ResponseData common = new ResponseData();
		try {
				common = confirmBindHelibao(vo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("确认绑卡(开户)异常，请求参数为：id:{}", new Object[] {JSONObject.toJSONString(vo)});
			common.setCode(StatusConstants.ERROR_CODE);
			common.setMsg(StatusConstants.SYS_ERROR_MSG);
			common.setStatus("1");
		}
		return common;
	}	
	
	
	
	/**
	 * app 确认开户 合利宝
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	private ResponseData confirmBindHelibao(AppConfirmOpenVO vo) throws Exception {
		//返回
		ResponseData common =  new ResponseData();

		//查询用户信息
		String userSpl = "select * from t_user where user_id = '"+vo.getUserId()+"'";
		JSONObject userJson = userSqlService.queryOne(userSpl);
		vo.setUserName(userJson.getString("real_name"));
		vo.setCustIc(userJson.getString("id_number"));
		
		//调用合利宝确认绑卡请求参数
		ConfirmBindCardVo requestVo = new ConfirmBindCardVo();
		requestVo.setP3_orderId(vo.getMerchantNumber());
		requestVo.setP5_validateCode(vo.getVerifyCode());
		logger.info("合利宝确认绑卡接口调用,请求参数:"+JSONObject.toJSONString(requestVo));
		ConfirmBindCardResponseVo resultVo = helibaoQuickPayService.bindCard(requestVo);
		logger.info("合利宝确认绑卡接口返回,返回参数:"+JSONObject.toJSONString(resultVo));
		if("0000".equals(resultVo.getRt2_retCode())){
		
			if("SUCCESS".equals(resultVo.getRt7_bindStatus())){
				//成功
				//绑卡成功 修改之前银行卡状态为0
//				isUpdateId = calenBindCard(vo);
				vo.setHlbUserId(resultVo.getRt5_userId());
				vo.setBankCode(resultVo.getRt8_bankId());
				saveCardStatus(vo, resultVo.getRt10_bindId());
				common.setCode(StatusConstants.SUCCESS_CODE);
				common.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
				common.setStatus("0");
			}else if("FAILED".equals(resultVo.getRt7_bindStatus())){
				//失败
				common.setCode(resultVo.getRt2_retCode());
				common.setMsg("授权失败,失败原因："+	resultVo.getRt3_retMsg());
				common.setStatus("1");
			}else{
				//处理中
				resultProcessing(vo,resultVo.getRt10_bindId());
				
			}
		
		}else {
			common.setCode(resultVo.getRt2_retCode());
			common.setMsg("授权失败,失败原因："+resultVo.getRt3_retMsg());
			common.setStatus("1");
		}
		return common;
	
	}
	
	
	
	
	
	/**
	 * 开户成功之后修改绑定卡状态
	 * @param vo 用户数据
	 * @param protocolNo  业务订单号
	 * @throws Exception
	 */
	private void saveCardStatus(AppConfirmOpenVO vo,String protocolNo) throws Exception{
		//添加新的卡数据
		UserBankInfo bankInfo = new UserBankInfo();
		bankInfo.setUserId(vo.getUserId()); 	//用户id
		bankInfo.setAccountName(vo.getUserName());//姓名
		bankInfo.setBankAccount(vo.getCardNumber());//银行卡号
		bankInfo.setAccountType(""); 			//账号类型
		bankInfo.setReservedPhone(vo.getMobile());	//手机号
		bankInfo.setDeleteFlag(0);
		bankInfo.setAccountIc(vo.getCustIc());//用户身份证
		bankInfo.setCreateTime(DateUtils.getCurrentTime());
		bankInfo.setUpdateTime(DateUtils.getCurrentTime());
		bankInfo.setSoure(2);
		bankInfo.setProtocolNo(protocolNo);
		bankInfo.setMerchantNumber(vo.getMerchantNumber());
		bankInfo.setHlbUserId(vo.getHlbUserId());
		bankInfo.setBankCode(vo.getBankCode());
		bankInfo.setBankName("--");
//		mapper.insert(custBankCard);
		userBindMapper.insertUserBankInfo(bankInfo);
	}
	
	
	private void resultProcessing(AppConfirmOpenVO vo,String bindId) throws Exception{
		//添加新的卡数据
		UserBankInfo bankInfo = new UserBankInfo();
		bankInfo.setUserId(vo.getUserId()); 	//用户id
		bankInfo.setAccountName(vo.getUserName());//姓名
		bankInfo.setBankAccount(vo.getCardNumber());//银行卡号
		bankInfo.setAccountType(""); 			//账号类型
		bankInfo.setReservedPhone(vo.getMobile());	//手机号
		bankInfo.setDeleteFlag(0);
		bankInfo.setAccountIc(vo.getCustIc());//用户身份证
		bankInfo.setCreateTime(DateUtils.getCurrentTime());
		bankInfo.setUpdateTime(DateUtils.getCurrentTime());
		bankInfo.setSoure(2);
		bankInfo.setProtocolNo(bindId);
		bankInfo.setMerchantNumber(vo.getMerchantNumber());
		bankInfo.setBankCode(vo.getBankCode());
		bankInfo.setHlbUserId(vo.getHlbUserId());
		//添加数据
		userBindMapper.insertUserBankInfo(bankInfo);

	}
	public ResponseData<List<JSONObject>> queryBankList(String userId) throws Exception {
		ResponseData common =  new ResponseData();
		//查询用户信息
		String bankSql = "select * from t_user_bank where user_id =  '"+userId+"'" +"order by create_time desc";
		List<JSONObject> bankCardList = userSqlService.query(bankSql);

		if(bankCardList.size()>0){
			common.setData(bankCardList);
			common.setStatus("0");
			common.setCode(StatusConstants.SUCCESS_CODE);
			common.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
		}else{
			logger.info("暂无绑定卡数据", JSON.toJSONString(userId));
			common.setStatus("1");
			common.setCode(StatusConstants.SUCCESS_CODE);
			common.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
		}
		return  common;
	}
}
