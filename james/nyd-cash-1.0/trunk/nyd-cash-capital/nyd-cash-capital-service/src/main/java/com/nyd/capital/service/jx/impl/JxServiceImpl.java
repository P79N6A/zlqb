package com.nyd.capital.service.jx.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.api.service.JxApi;
import com.nyd.capital.entity.UserJx;
import com.nyd.capital.model.jx.*;
import com.nyd.capital.service.UserJxService;
import com.nyd.capital.service.jx.JxService;
import com.nyd.capital.service.jx.config.JxConfig;
import com.nyd.capital.service.jx.util.JxMapAssemblyUtils;
import com.nyd.capital.service.jx.util.JxSHAUtil;
import com.nyd.capital.service.validate.ValidateUtil;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserBankContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.AccountDto;
import com.tasfe.framework.support.model.ResponseData;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuqiu
 */
@Service("jxApi")
public class JxServiceImpl implements JxService,JxApi {

    Logger logger = LoggerFactory.getLogger(JxServiceImpl.class);

    @Autowired
    private JxConfig jxConfig;

    @Autowired
    private JxSHAUtil jxSHAUtil;

    @Autowired
    private ValidateUtil validateUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserIdentityContract userIdentityContract;

    @Autowired
    private UserBankContract userBankContract;

    @Autowired
    private UserAccountContract userAccountContract;
    @Autowired
    private UserJxService userJxService;


    /**
     * 即信五合一接口
     * @param request
     * @return
     */
    @Override
    public ResponseData jxFiveComprehensive(JxFiveComprehensiveRequest request) {
        logger.info("jxFiveComprehensive interface request param:"+JSON.toJSON(request));
        try {
            /***************通用字段赋值******************/
            /*request.setVersion(jxConfig.getVersion());
            request.setTxCode(jxConfig.getOpenHtml());
            request.setInstCode(jxConfig.getInstCode());
            request.setChannel(jxConfig.getChannel());
            request.setTxDate(getDate(1));
            request.setTxTime(getDate(2));
            request.setSeqNo(String.valueOf((int)((Math.random()*9+1)*100000)));
            request.setAcqRes(jxConfig.getAcqRes());*/
            JxCommonRequest jxCommonRequest = commomObjectSetValue();
            BeanUtils.copyProperties(jxCommonRequest,request);
            request.setTxCode(jxConfig.getOpenHtml());


            /***********将对象封装到map集合中************/
            Map<String,String> map = new HashMap<>();
            map.put("version",request.getVersion());
            map.put("txCode",request.getTxCode());
            //5001是需要签名的 ;5004是不需要签名的
            if ("5001".equals(request.getInstCode())){
                map.put("instCode",request.getInstCode());
            }
            map.put("channel",request.getChannel());
            map.put("txDate",request.getTxDate());
            map.put("txTime",request.getTxTime());
            map.put("seqNo",request.getSeqNo());
            if (StringUtils.isNotBlank(request.getAcqRes())){
                map.put("acqRes",request.getAcqRes());
            }
            map.put("mobile",request.getMobile());
            map.put("realName",request.getRealName());
            map.put("idCardNumber",request.getIdCardNumber());
            map.put("returnUrl",request.getReturnUrl());
            map.put("bankCardNumber",request.getBankCardNumber());
            if (StringUtils.isNotBlank(request.getBankCardNumber())){
                map.put("platform",request.getPlatform());
            }
            logger.info("五合一接口添加到map集合:"+JSON.toJSON(map));

            //获取sign
            String sign = jxSHAUtil.getSign(map);
            logger.info("五合一接口请求sign:"+sign);
            request.setSign(sign);
            logger.info("五合一接口完整请求参数:"+JSON.toJSON(request));

            //参数校验
            validateUtil.validate(request);

            //发起请求
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity entity = new HttpEntity(JSON.toJSONString(request), headers);

            String url = jxConfig.getCommonUrl();
            logger.info("五合一接口请求URL:"+url);

            ResponseEntity<JxFiveComprehensiveResponse> result = restTemplate.exchange(url, HttpMethod.POST, entity, JxFiveComprehensiveResponse.class);
            JxFiveComprehensiveResponse jxFiveComprehensiveResponse = result.getBody();

            logger.info("五合一接口返回的对象:"+JSON.toJSON(jxFiveComprehensiveResponse));

            if ("0".equals(jxFiveComprehensiveResponse.getStatusCode())){
                ResponseData responseData = ResponseData.success();
                responseData.setData(jxFiveComprehensiveResponse);
                return responseData;
            }else {
                logger.info("调用五合一接口失败原因:" + result.getBody().getMessage());
                return ResponseData.error(result.getBody().getMessage());
            }

        }catch (Exception e){
            logger.error("调用五合一出错啦!",e);
            e.printStackTrace();
            return ResponseData.error("服务器开小差,请稍后再试");

        }

    }

    /**
     * 即信获取验证码
     * @param request
     * @return
     */
    @Override
    public ResponseData jxGetCheckCode(JxGetCheckCodeRequest request) {
        logger.info("get check code intergace request param:"+JSON.toJSON(request));
        try {
            /***************通用字段赋值******************/
            JxCommonRequest jxCommonRequest = commomObjectSetValue();
            BeanUtils.copyProperties(jxCommonRequest,request);
            request.setTxCode(jxConfig.getGetCheckCode());

            /***********将对象封装到map集合中************/
            Map<String,String> map = new HashMap<>();
            map.put("version",jxConfig.getVersion());
            map.put("txCode",request.getTxCode());
            //5001是需要签名的 ;5004是不需要签名的
            if ("5001".equals(request.getInstCode())){
                map.put("instCode",request.getInstCode());
            }
            map.put("channel",request.getChannel());
            map.put("txDate",request.getTxDate());
            map.put("txTime",request.getTxTime());
            map.put("seqNo",request.getSeqNo());
            if (StringUtils.isNotBlank(request.getAcqRes())){
                map.put("acqRes",request.getAcqRes());
            }
            if (StringUtils.isNotBlank(request.getMemberId())){
                map.put("memberId",request.getMemberId());
            }

            map.put("kind",request.getKind());

            if (StringUtils.isNotBlank(request.getMobile())){
                map.put("mobile",request.getMobile());
            }

            if (StringUtils.isNotBlank(request.getBankCardNumber())){
                map.put("bankCardNumber",request.getBankCardNumber());
            }
            logger.info("获取验证码添加到map集合:"+JSON.toJSON(map));

            //获取sign
            String sign = jxSHAUtil.getSign(map);
            logger.info("获取验证码请求sign:"+sign);
            request.setSign(sign);
            logger.info("获取验证码完整请求参数:"+JSON.toJSON(request));

            //参数校验
            validateUtil.validate(request);

            //发起请求
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity entity = new HttpEntity(JSON.toJSONString(request), headers);

            ResponseEntity<JxGetCheckCodeResponse> result = restTemplate.exchange(jxConfig.getCommonUrl(), HttpMethod.POST, entity, JxGetCheckCodeResponse.class);
            JxGetCheckCodeResponse jxGetCheckCodeResponse = result.getBody();
            logger.info("获取验证码返回的对象:"+JSON.toJSON(jxGetCheckCodeResponse));

            if ("0".equals(jxGetCheckCodeResponse.getStatusCode())){
                ResponseData responseData = ResponseData.success();
                responseData.setData(jxGetCheckCodeResponse);
                return responseData;
            }else {
                logger.info("获取验证码失败原因:" + result.getBody().getMessage());
                return ResponseData.error(result.getBody().getMessage());
            }

        }catch (Exception e){
            logger.error("获取验证码出错啦!",e);
            e.printStackTrace();
            return ResponseData.error("服务器开小差,请稍后再试");
        }

    }

    /**
     * 即信提现
     * @param request
     * @return
     */
    @Override
    public ResponseData jxWithDraw(JxWithDrawRequest request) {
        logger.info("withdraw  intergace request param:"+JSON.toJSON(request));
        try {
            /***************通用字段赋值******************/
            JxCommonRequest jxCommonRequest = commomObjectSetValue();
            BeanUtils.copyProperties(jxCommonRequest,request);
            request.setTxCode(jxConfig.getWithDraw());

            //提现回调url
            request.setReturnUrl(jxConfig.getWithDrawCallBack());
            //忘记密码url
            request.setForgotPasswordUrl(jxConfig.getForgotPasswordUrl());

            /***********将对象封装到map集合中************/
            Map<String,String> map = new HashMap<>();
            map.put("version",request.getVersion());
            map.put("txCode",request.getTxCode());
            //5001是需要签名的 ;5004是不需要签名的
            if ("5001".equals(request.getInstCode())){
                map.put("instCode",request.getInstCode());
            }
            map.put("channel",request.getChannel());
            map.put("txDate",request.getTxDate());
            map.put("txTime",request.getTxTime());
            map.put("seqNo",request.getSeqNo());
            if (StringUtils.isNotBlank(request.getAcqRes())){
                map.put("acqRes",request.getAcqRes());
            }
            map.put("memberId",String.valueOf(request.getMemberId()));
//            BigDecimal amount = request.getAmount().setScale(1,BigDecimal.ROUND_UP);
//            request.setAmount(amount);
            map.put("amount",request.getAmount());
            map.put("returnUrl",request.getReturnUrl());
            map.put("forgotPasswordUrl",request.getForgotPasswordUrl());
            map.put("loanId",String.valueOf(request.getLoanId()));

            logger.info("提现添加到map集合:"+JSON.toJSON(map));

            //获取sign
            String sign = jxSHAUtil.getSign(map);
            logger.info("提现请求sign:"+sign);
            request.setSign(sign);
            logger.info("提现完整请求参数:"+JSON.toJSON(request));

            //参数校验
            validateUtil.validate(request);

            //发起请求
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity entity = new HttpEntity(JSON.toJSONString(request), headers);

            ResponseEntity<JxWithDrawResponse> result = restTemplate.exchange(jxConfig.getCommonUrl(), HttpMethod.POST, entity, JxWithDrawResponse.class);
            JxWithDrawResponse jxWithDrawResponse = result.getBody();

            if ("0".equals(jxWithDrawResponse.getStatusCode())){
                ResponseData responseData = ResponseData.success();
                responseData.setData(jxWithDrawResponse);
                return responseData;
            }else {
                logger.info("提现失败原因:" + result.getBody().getMessage());
                return ResponseData.error(result.getBody().getMessage());
            }

        }catch (Exception e){
            logger.error("提现出错啦!",e);
            e.printStackTrace();
            return ResponseData.error("服务器开小差,请稍后再试");
        }

    }

    /**
     * 即信放款查询
     * @param request
     * @return
     */
    @Override
    public ResponseData jxLoanQuery(JxLoanQueryRequest request) {
        logger.info("loan query intergace request param:"+ JSON.toJSON(request));
        try {
            /***************通用字段赋值******************/
            JxCommonRequest jxCommonRequest = commomObjectSetValue();
            BeanUtils.copyProperties(jxCommonRequest,request);
            request.setTxCode(jxConfig.getLoanQuery());

            /***********将对象封装到map集合中************/
            Map<String,String> map = new HashMap<>();
            map.put("version",request.getVersion());
            map.put("txCode",request.getTxCode());
            //5001是需要签名的 ;5004是不需要签名的
            if ("5001".equals(request.getInstCode())){
                map.put("instCode",request.getInstCode());
            }
            map.put("channel",request.getChannel());
            map.put("txDate",request.getTxDate());
            map.put("txTime",request.getTxTime());
            map.put("seqNo",request.getSeqNo());
            if (StringUtils.isNotBlank(request.getAcqRes())){
                map.put("acqRes",request.getAcqRes());
            }


            map.put("loanId",String.valueOf(request.getLoanId()));
            logger.info("放款查询添加到map集合:"+JSON.toJSON(map));
            //获取sign
            String sign = jxSHAUtil.getSign(map);
            logger.info("放款查询请求sign:"+sign);
            request.setSign(sign);
            logger.info("放款查询完整请求参数:"+JSON.toJSON(request));

            //参数校验
            validateUtil.validate(request);

            //发起请求
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity entity = new HttpEntity(JSON.toJSONString(request), headers);

            ResponseEntity<String> result = restTemplate.exchange(jxConfig.getCommonUrl(), HttpMethod.POST, entity, String.class);

            //JxLoanQueryResponse jxLoanQueryResponse = result.getBody();
            Map<String, Object> map1 = JxMapAssemblyUtils.json2Map(result.getBody());
            logger.info("放款查询返回的对象:"+JSON.toJSON(map1));

            if ("0".equals(map1.get("statusCode"))){
                ResponseData responseData = ResponseData.success();
                responseData.setData(map1);
                return responseData;
            }else {
                logger.info("放款查询失败原因:" + map.get("message"));
                return ResponseData.error(map.get("message"));
            }

        }catch (Exception e){
            logger.error("放款查询出错啦!",e);
            e.printStackTrace();
            return ResponseData.error("服务器开小差,请稍后再试");
        }

    }
    /**
     * 
     * 推单查询.
     * @see com.nyd.capital.service.jx.JxService#queryPushStatus(com.nyd.capital.model.jx.JxQueryPushStatusRequest)
     */
	@Override
	public ResponseData queryPushStatus(JxQueryPushStatusRequest jxQueryPushStatusRequest) {
		try {
			logger.info("即信推单查询,请求参数(缺少sign):" + JSON.toJSON(jxQueryPushStatusRequest));
			Map<String,String> requestMap = JxMapAssemblyUtils.getMapWithoutSignQueryPushStatus(jxQueryPushStatusRequest,jxConfig);
			String sign = jxSHAUtil.getSign(requestMap);
			logger.info("即信推单查询请求参数所需sign:" + sign);
			requestMap.put("sign", sign);
			logger.info("即信推单查询组装请求参数:{}",JSON.toJSON(requestMap));
			
			//参数校验
			validateUtil.validate(jxQueryPushStatusRequest);
			//发起请求
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);//APPLICATION_JSON,APPLICATION_FORM_URLENCODED
			HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestMap, headers);
			
			ResponseEntity<JxQueryPushStatusResponse> result = restTemplate.exchange(jxConfig.getCommonUrl(), HttpMethod.POST, entity, JxQueryPushStatusResponse.class);
			JxQueryPushStatusResponse jxQueryPushStatusResponse = result.getBody();
			logger.info("即信推单查询后,返回的内容:{}", JSON.toJSON(result.getBody()));
			if("0".equals(jxQueryPushStatusResponse.getStatusCode())) {
				ResponseData responseData = ResponseData.success();
				responseData.setData(jxQueryPushStatusResponse);
				return responseData;
			}else {
				logger.info("即信推单查询失败原因:{}",jxQueryPushStatusResponse.getMessage());
				return ResponseData.error(jxQueryPushStatusResponse.getMessage());
			}
		} catch (Exception e) {
			logger.error("即信推单查询请求出错", e);
            return ResponseData.error("服务器开小差,请稍后再试");
		}
	}

    /**
     * 根据用户userID去查找四要素
     * @param userId
     * @return
     */
    @Override
    public ResponseData getInformationByUserId(String userId) {
        logger.info("jx通过用户userId获取四要素:"+userId);
        ResponseData responseData = ResponseData.success();

        try {
            JxUserDetail jxUserDetail = new JxUserDetail();
            ResponseData<AccountDto> accountDtoResponseData = userAccountContract.getAccount(userId);
            if ("0".equals(accountDtoResponseData.getStatus())){
                AccountDto accountDto = accountDtoResponseData.getData();
                logger.info("用户账户信息(jx):"+JSON.toJSON(accountDto));
                if (accountDto != null){
                    if (StringUtils.isNotBlank(accountDto.getAccountNumber())){
                        jxUserDetail.setAccountNumber(accountDto.getAccountNumber());
                    }
                }
            }

            ResponseData<UserInfo> userInfoResponseData = userIdentityContract.getUserInfo(userId);
            if ("0".equals(userInfoResponseData.getStatus())){
                UserInfo userInfo = userInfoResponseData.getData();
                logger.info("用户信息(jx):"+JSON.toJSON(userInfo));
                if (userInfo != null){
                    if (StringUtils.isNotBlank(userInfo.getIdNumber())){
                        jxUserDetail.setIdNumber(userInfo.getIdNumber());
                    }
                    if (StringUtils.isNotBlank(userInfo.getRealName())){
                        jxUserDetail.setRealName(userInfo.getRealName());
                    }
                }
            }

            ResponseData<List<BankInfo>> bankContractBankInfos = userBankContract.getBankInfos(userId);
            if ("0".equals(bankContractBankInfos.getStatus())){
                List<BankInfo> list = bankContractBankInfos.getData();
                if (list != null && list.size() > 0 ){
                    BankInfo bankInfo = list.get(0);
                    logger.info("用户银行卡信息(jx):"+JSON.toJSON(bankInfo));
                    if (StringUtils.isNotBlank(bankInfo.getBankAccount())){
                        jxUserDetail.setBankAccount(bankInfo.getBankAccount());
                    }

                }
            }
            logger.info("userId(jx):"+JSON.toJSON(jxUserDetail));
            responseData.setData(jxUserDetail);
            return responseData;

        }catch (Exception e){
            logger.error("获取用户四要素出错啦!",e);
            return ResponseData.error("服务器开小差,请稍后再试");
        }

    }

    @Override
    public ResponseData getUserJxByUserId(String userId) {
        return ResponseData.success(userJxService.getUserJxByUserId(userId));
    }

    @Override
    public void updateUserJx(UserJx userJx) {
       userJxService.updateUserJx(userJx);
    }


    /**
	 * 
	 * 推单外审接口.
	 * @see com.nyd.capital.service.jx.JxService#pushAudit(com.nyd.capital.model.jx.JxPushAuditRequest)
	 */
	@Override
	public ResponseData pushAudit(JxPushAuditRequest jxPushAuditRequest) {
		try {
			logger.info("推单外审接口,请求参数(缺少sign):"+JSON.toJSONString(jxPushAuditRequest));
			Map<String,String> auditMap = JxMapAssemblyUtils.getMapWithoutSignQueryPushAudit(jxPushAuditRequest,jxConfig);
			String sign = jxSHAUtil.getSign(auditMap);
			logger.info("推单外审接口请求参数所需sign:"+sign);
			auditMap.put("sign", sign);
			logger.info("推单外审接口请求参数："+JSON.toJSONString(auditMap));
			
			//参数校验
			validateUtil.validate(jxPushAuditRequest);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<Map<String, String>> entity = new HttpEntity<>(auditMap, headers);
			ResponseEntity<JxPushAuditResponse> responseEntity = restTemplate.exchange(jxConfig.getCommonUrl(), HttpMethod.POST,entity,JxPushAuditResponse.class);
			JxPushAuditResponse jxPushAuditResponse = responseEntity.getBody();
			logger.info("推单外审接口,调用即信接口响应结果:{}", JSON.toJSON(responseEntity.getBody()));
			if("0".equals(jxPushAuditResponse.getStatusCode())) {
				ResponseData responseData = ResponseData.success();
				responseData.setData(jxPushAuditResponse);
				return responseData;
			}else {
				logger.info("推单外审失败原因:{}",jxPushAuditResponse.getMessage());
				return ResponseData.error(jxPushAuditResponse.getMessage());
			}
			
		} catch (RestClientException e) {
			logger.error("推单外审接口请求出错",e);
			return ResponseData.error("服务器开小差,请稍后再试");
		}
		
	}


	/**
	 * 
	 * 推单外审确认.
	 * @see com.nyd.capital.service.jx.JxService#pushAuditConfirm(com.nyd.capital.model.jx.JxPushAuditConfirmRequest)
	 */
	@Override
	public ResponseData pushAuditConfirm(JxPushAuditConfirmRequest jxpushAuditConfirmRequest) {
		try {
			logger.info("推单外审确认接口请求参数(缺少sign)："+JSON.toJSONString(jxpushAuditConfirmRequest));
			Map<String, String> confirmMap = JxMapAssemblyUtils.getMapWithoutSignPushAuditConfirm(jxpushAuditConfirmRequest,jxConfig);
			String sign = jxSHAUtil.getSign(confirmMap);
			logger.info("推单外审确认接口请求所需sign："+sign);
			confirmMap.put("sign", sign);
			logger.info("推单外审确认接口请求参数："+JSON.toJSONString(confirmMap));
			
			//数据校验
			validateUtil.validate(jxpushAuditConfirmRequest);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<Map<String, String>> entity = new HttpEntity<Map<String, String>>(confirmMap, headers);
			ResponseEntity<JxPushAuditConfirmResponse> responseEntity = restTemplate.exchange(jxConfig.getCommonUrl(), HttpMethod.POST, entity, JxPushAuditConfirmResponse.class);
			JxPushAuditConfirmResponse jxPushAuditConfirmResponse = responseEntity.getBody();
			logger.info("推单外审确认接口,响应结果:{}",JSON.toJSONString(jxPushAuditConfirmResponse));
			if("0".equals(jxPushAuditConfirmResponse.getStatusCode())) {
				ResponseData responseData = ResponseData.success();
				responseData.setData(responseEntity.getBody());
				return responseData;
			}else {
				logger.info("推单外审确认接口失败原因:{}",jxPushAuditConfirmResponse.getMessage());
				return ResponseData.error(jxPushAuditConfirmResponse.getMessage());
			}
			
		} catch (RestClientException e) {
			logger.error("推单外审确认请求错误："+e);
			return ResponseData.error("服务器开小差,请稍后再试");
		}
	}


	/**
	 * 
	 * 推单外审结果查询.
	 *
	 */
	@Override
	public ResponseData queryPushAuditResult(JxQueryPushAuditResultRequest jxQueryPushAuditResultRequest) {
		try {
			logger.info("推单外审结果查询请求参数(缺少sign):"+JSON.toJSONString(jxQueryPushAuditResultRequest));
			Map<String,String> auditResultMap = JxMapAssemblyUtils.getMapWithoutSignPushAuditResult(jxQueryPushAuditResultRequest, jxConfig);
			String sign = jxSHAUtil.getSign(auditResultMap);
			logger.info("推单外审结果查询请求所需sign："+sign);
			auditResultMap.put("sign", sign);
			logger.info("推单外审结果查询请求参数:"+JSON.toJSONString(auditResultMap));
			
			//参数校验
			validateUtil.validate(jxQueryPushAuditResultRequest);
			
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<Map<String, String>> entity = new HttpEntity<Map<String, String>>(auditResultMap, httpHeaders);
			ResponseEntity<JxQueryPushAuditResultResponse> responseEntity = restTemplate.exchange(jxConfig.getCommonUrl(), HttpMethod.POST, entity, JxQueryPushAuditResultResponse.class);
			JxQueryPushAuditResultResponse jxQueryPushAuditResultResponse = responseEntity.getBody();
			logger.info("推单外审结果查询响应结果:{}",JSON.toJSONString(jxQueryPushAuditResultResponse));
			if("0".equals(jxQueryPushAuditResultResponse.getStatusCode())) {
				ResponseData responseData = ResponseData.success();
				responseData.setData(responseEntity.getBody());
				return responseData;
			}else {
				logger.info("推单外审结果查询响应结果:{}",jxQueryPushAuditResultResponse.getMessage());
				return ResponseData.error(jxQueryPushAuditResultResponse.getMessage());
			}
			
		} catch (RestClientException e) {
			logger.error("推单外审结果查询请求错误",e);
			return ResponseData.error("服务器开小差,请稍后再试");
		}
	}

    @Override
    public ResponseData queryLoanPhases(JxQueryLoanPhasesRequest jxQueryLoanPhasesRequest) {
        try {
            logger.info("即信还款计划查询,请求参数(缺少sign):" + JSON.toJSON(jxQueryLoanPhasesRequest));
            Map<String,String> requestMap = JxMapAssemblyUtils.getMapQueryLoanPhases(jxQueryLoanPhasesRequest,jxConfig);
            String sign = jxSHAUtil.getSign(requestMap);
            logger.info("即信还款计划查询请求参数所需sign:" + sign);
            requestMap.put("sign", sign);
            logger.info("即信还款计划查询组装请求参数:{}",JSON.toJSON(requestMap));

            //参数校验
            validateUtil.validate(jxQueryLoanPhasesRequest);
            //发起请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestMap, headers);

            ResponseEntity<String> result =  restTemplate.exchange(jxConfig.getCommonUrl(), HttpMethod.POST, entity, String.class);
            logger.info("即信还款计划查询后,返回的内容:{}", JSON.toJSON(result.getBody()));
        	Map<String, Object> map = JxMapAssemblyUtils.json2Map(result.getBody());
        	if (null == map || map.isEmpty()) {
        		return ResponseData.error("调用即信结果出错");
			}
            if("0".equals(map.get("statusCode"))) {
            	ResponseData responseData = ResponseData.success();
				responseData.setData(map);
				return responseData;
            }else {
            	logger.info("即信还款计划查询失败原因:{}",map.get("message"));
                return ResponseData.error(String.valueOf(map.get("message")));
            }
        } catch (Exception e) {
            logger.error("即信还款计划查询请求出错", e);
            return ResponseData.error("服务器开小差,请稍后再试");
        }
    }

    @Override
    public ResponseData repayments(JxRepaymentsRequest jxRepaymentsRequest) {
        try {
            logger.info("即信还款,请求参数(缺少sign):" + JSON.toJSON(jxRepaymentsRequest));
            Map<String,String> requestMap = JxMapAssemblyUtils.getMapRepayments(jxRepaymentsRequest,jxConfig);
            String sign = jxSHAUtil.getSign(requestMap);
            logger.info("即信还款请求参数所需sign:" + sign);
            requestMap.put("sign", sign);
            logger.info("即信还款组装请求参数:{}",JSON.toJSON(requestMap));

            //参数校验
            validateUtil.validate(jxRepaymentsRequest);
            //发起请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> entity = new HttpEntity<>(JSONObject.fromObject(requestMap).toString(), headers);

            ResponseEntity<JxRepaymentsResponse> result = restTemplate.exchange(jxConfig.getCommonUrl(), HttpMethod.POST, entity, JxRepaymentsResponse.class);
            JxRepaymentsResponse jxRepaymentsResponse = result.getBody();
            logger.info("即信还款后,返回的内容:{}", JSON.toJSON(result.getBody()));
            if("0".equals(jxRepaymentsResponse.getStatusCode())) {
                ResponseData responseData = ResponseData.success();
                responseData.setData(jxRepaymentsResponse);
                return responseData;
            }else {
                logger.info("即信还款失败原因:{}",jxRepaymentsResponse.getMessage());
                return ResponseData.error(jxRepaymentsResponse.getMessage());
            }
        } catch (Exception e) {
            logger.error("即信还款请求出错", e);
            return ResponseData.error("服务器开小差,请稍后再试");
        }
    }


    /**
     * 通用字段赋值
     * @return
     */
    private JxCommonRequest commomObjectSetValue(){
        JxCommonRequest request = new JxCommonRequest();
        request.setVersion(jxConfig.getVersion());
//        request.setTxCode(jxConfig.getOpenHtml());
        request.setInstCode(jxConfig.getInstCode());
        request.setChannel(jxConfig.getChannel());
        request.setTxDate(getDate(1));
        request.setTxTime(getDate(2));
        request.setSeqNo(String.valueOf((int)((Math.random()*9+1)*100000)));
        request.setAcqRes(jxConfig.getAcqRes());
        logger.info("commom object:"+JSON.toJSON(request));
        return request;
    }


    /**
     * 针对txDate 和 txTime 这两个字段所需值
     * @param left
     * @return
     */
    private String getDate(int left){
        Date date = new Date();
        SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfHms = new SimpleDateFormat("HHmmss");

        if (left == 1){
            return sdfYmd.format(date);
        }else {
            return sdfHms.format(date);
        }

    }


}
