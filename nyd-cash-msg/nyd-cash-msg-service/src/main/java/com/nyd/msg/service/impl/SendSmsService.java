package com.nyd.msg.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.nyd.msg.dao.mapper.SendSmsLogMapper;
import com.nyd.msg.entity.SendSmsLog;
import com.nyd.msg.service.channel.TianRuiChannelStrategy;
import com.nyd.msg.service.channel.model.SmsConfigDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nyd.msg.dao.ISysSmsConfigDao;
import com.nyd.msg.dao.ISysSmsParamDao;
import com.nyd.msg.dao.mapper.AppNameConfigMapper;
import com.nyd.msg.entity.SysSmsConfig;
import com.nyd.msg.entity.SysSmsParam;
import com.nyd.msg.exception.ErrorInfo;
import com.nyd.msg.exception.OverTimesException;
import com.nyd.msg.exception.ValidateException;
import com.nyd.msg.model.CommonResponse;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.model.SmsRequestBatch;
import com.nyd.msg.service.ISendSmsService;
import com.nyd.msg.service.code.ChannelEnum;
import com.nyd.msg.service.code.DaHanResultEnum;
import com.nyd.msg.service.code.ResultCode;
import com.nyd.msg.service.factory.ChannelStrategyFactory;
import com.nyd.msg.service.utils.Message;
import com.nyd.msg.service.utils.PostUtil;
import com.nyd.msg.service.validation.SmsRequestBatchValidator;
import com.nyd.msg.service.validation.SmsRequestValidator;
import com.nyd.msg.service.validation.ValidatorUtil;
import com.tasfe.framework.redis.RedisService;
import com.tasfe.framework.support.model.ResponseData;

@Service(value = "sendSmsService")
public class SendSmsService implements ISendSmsService {
	// @Autowired
	public static final String DEF_REGEX = "\\$\\{(.+?)\\}";
	Logger logger = LoggerFactory.getLogger(SendSmsService.class);
	@Autowired
	private ISysSmsParamDao sysSmsParamDao;
	@Autowired
	private ISysSmsConfigDao sysSmsConfigDao;

	@Autowired
	private AppNameConfigMapper appNameConfigMapper;

	@Autowired
	private RedisService redisService;

	@Autowired
	private RedisTemplate redisTemplate;

	private long rateLimit = 1;

	@Autowired
	private ChannelStrategyFactory channelStrategyFactory;

	@Autowired
	private TianRuiChannelStrategy tianRuiChannelStrategy;

	@Autowired
	private SendSmsLogMapper sendSmsLogMapper;

	public ResponseData sendSingleSmsPro(SmsRequest vo) {
		logger.info("****新发送短信入口***" + vo.toString());
		// 验证码校验
		try {
			ValidatorUtil.validateObject(vo, new SmsRequestValidator());
		} catch (ValidateException e) {
			return ResponseData.error(e.getErrorInfo().getErrorMsg());
		}
		// 拉数据库配置
		List<SysSmsConfig> configList = sysSmsConfigDao.queryList();
		logger.info("<<<< 配置参数 >>>> " + JSONObject.toJSONString(configList));
		if (configList == null || configList.isEmpty()) {
			logger.warn("缺少短信平台.");
			return ResponseData.error("缺少短信平台");
		}
		// 查询短信模板 现在最主要的是拿模板id
		SysSmsParam sysSmsParam = sysSmsParamDao.selectBySourceType(vo.getSmsType());
		if (sysSmsParam == null) {
			return ResponseData.error("不支持的短信类型");
		}

		String verifyCode = null;
		// 验证码
		if (sysSmsParam.getCodeFlag() == 1) {
			String cacheKey1 = vo.getSmsType() + vo.getCellphone();
			String cacheKey2 = cacheKey1 + "count";
			verifyCode = generateVerifyCode(6);
			redisService.setString(cacheKey1, verifyCode, 120000);
			if (sysSmsParam.getCount() != null) {
				Integer curentCount = (Integer) redisService.getObject(cacheKey2, 2);
				if (curentCount == null) {
					redisService.setObject(cacheKey2, 1, getDuring(sysSmsParam.getDuring()));
				} else {
					redisService.incr(cacheKey2, 1);
				}
			}
		}
		boolean resultFlag = false;
		for (int size = 0; size < configList.size(); size++) {
			SmsConfigDto smsConfigDto = new SmsConfigDto()
					.setSign("【助乐钱包】")
					.setTemplateId(sysSmsParam.getTianRuiYunCode())
					.setMobile(vo.getCellphone())
					.setSmsPlatUrlSingle(configList.get(size).getSmsPlatUrlSingle())
					.setSmsPlatAccount(configList.get(size).getSmsPlatAccount())
					.setSmsPlatPwd(configList.get(size).getSmsPlatPwd())
			;
			if ("39".equals(String.valueOf(vo.getSmsType()))){
				smsConfigDto.setContent(
						vo.getMap().get("custName") +
						"##" + vo.getMap().get("prince") +
						"##" + vo.getMap().get("amount") +
						"##" + vo.getMap().get("sumAmount") +
						"##" + vo.getMap().get("phone"));
			}else if("50".equals(String.valueOf(vo.getSmsType()))){
				smsConfigDto.setContent(
						vo.getMap().get("custName") +
						"##" + vo.getMap().get("price") +
						"##" + vo.getMap().get("empName") +
						"##" + vo.getMap().get("expireTime"));
			}else if("38".equals(String.valueOf(vo.getSmsType()))){
				smsConfigDto.setContent(vo.getMap().get("phone").toString());
			}else if("9".equals(String.valueOf(vo.getSmsType()))){
				smsConfigDto.setContent(
						vo.getMap().get("金额") +
						"##" + vo.getMap().get("日期") +
						"##助乐钱包");
			}else if("33".equals(String.valueOf(vo.getSmsType()))){
				smsConfigDto.setContent(
						vo.getMap().get("userName") +
						"##" + vo.getMap().get("limitAmount") +
						"##" + vo.getMap().get("appNmae"));
			}
			else if(verifyCode != null){
				smsConfigDto.setContent(verifyCode);
			}
			try {
				resultFlag = tianRuiChannelStrategy.sendSms(smsConfigDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (resultFlag) {
			return ResponseData.success("成功");
		}else{
			return ResponseData.error("短信平台都没发成功或者符合条件的短信平台没有");
		}
	}

	@Override
	public ResponseData sendSingleSms(SmsRequest vo) {
		logger.info("****发送短信入口***" + vo.toString());
		try {
			ValidatorUtil.validateObject(vo, new SmsRequestValidator());
		} catch (ValidateException e) {
			return ResponseData.error(e.getErrorInfo().getErrorMsg());
		}


		Map<String, Object> params = new HashMap<>();
		List<SysSmsConfig> configList = sysSmsConfigDao.queryList();
		logger.info("<<<< 配置参数 >>>> " + JSONObject.toJSONString(configList));
		if (configList == null || configList.isEmpty()) {
			logger.warn("缺少短信平台.");
			return ResponseData.error("缺少短信平台");
		}

		SysSmsParam param = sysSmsParamDao.selectBySourceType(vo.getSmsType());
		if (param == null) {
			return ResponseData.error("不支持的短信类型");

		}
		Map<String, Object> map = new HashMap<>();
		if (vo.getMap() != null) {
			map = vo.getMap();
		}
		// 如果是马甲包需要传入appName
		if (StringUtils.isNotBlank(vo.getAppName())) {
			try {
				String appCode = vo.getAppName();
				String appName = appNameConfigMapper.selectNameByCode(appCode);
				if (StringUtils.isNotBlank(appName)) {
					map.put("appName", appName);
				} else {
					map.put("appName", "助乐钱包");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("select appName error!");
			}
		} else {
			map.put("appName", "信信贷");
		}
		vo.setMap(map);
		vo.setSign(String.valueOf(map.get("appName")));
		String template = null;
		try {
			template = setParams(vo, param);
		} 
		catch(OverTimesException e) {
			logger.warn("send too many msg.",e);
			return ResponseData.error(e.getErrorInfo().getErrorMsg());
		}
		catch (Exception e) {
			logger.error("setParams failed.", e);
			return ResponseData.error("请稍后再试");
		}
		if (template == null || template.length() == 0) {
			return ResponseData.error("模板为空");
		}

		logger.info("template:" + template);

		for (int size = 0; size < configList.size(); size++) {
			SysSmsConfig config = configList.get(size);
			ChannelEnum channelEnum = ChannelEnum.getMap().get(config.getChannelFlag());
			boolean resultFlag = false;
			String channelCode = channelEnum.getCode();
			/*boolean isExceeds = this.isExceedLimit(vo, channelCode);
			if (isExceeds) {
				logger.warn("exceeds rate limit, return failed.");
				return ResponseData.error("短信发送过于频繁，请稍后再试");
			}*/
			try {
				if ("1".equals(vo.getType())) {
					if (channelEnum != ChannelEnum.TCBATCH && channelEnum != ChannelEnum.DHST) {
						continue;
					} else {
						Message message = new Message(config.getSmsPlatUrlBatch(), config.getSmsPlatAccount(),
								config.getSmsPlatPwd(), template, vo.getCellphone(),vo.getAppName(),vo.getSign());
						resultFlag = channelStrategyFactory.buildChannel(channelEnum).sendSms(message, true);
					}
				} else {
					if((String.valueOf(vo.getSmsType())).equals("39")|| vo.getSmsType() == 39){
						Message message = new Message(config.getSmsPlatUrlSingle(),  config.getSmsPlatAccount(),
								config.getSmsPlatPwd(), template, vo.getCellphone(),vo.getAppName(),vo.getSign());
						resultFlag = channelStrategyFactory.buildChannel(channelEnum).sendSms(message, false);
						logger.info("****扣费发送短信出口***" + vo.getSmsType() + message.toString());
					}else{
						Message message = new Message(config.getSmsPlatUrlSingle(), config.getSmsPlatAccount(),
								config.getSmsPlatPwd(), template, vo.getCellphone(),vo.getAppName(),vo.getSign());
						resultFlag = channelStrategyFactory.buildChannel(channelEnum).sendSms(message, false);
						logger.info("****正常发送短信出口***" + vo.getSmsType()  + message.toString());
					}

				}
				if (resultFlag) {
					return ResponseData.success("成功");
				} else {
					continue;
				}
			} catch (Exception e) {
				logger.error("没type异常",e);
				Message message = new Message(config.getSmsPlatUrlSingle(), config.getSmsPlatAccount(),
						config.getSmsPlatPwd(), template, vo.getCellphone(),vo.getAppName(),vo.getSign());
				resultFlag = channelStrategyFactory.buildChannel(channelEnum).sendSms(message, false);
				logger.info("****发送短信出口***" + message.toString());
				if (resultFlag) {
					return ResponseData.success("成功");
				} else {
					continue;
				}
			}

		}
		return ResponseData.error("短信平台都没发成功或者符合条件的短信平台没有");

	}

	@Override
	public ResponseData sendBatchSms(SmsRequestBatch batchVo) {
		logger.info("batch 进入");
		try {
			ValidatorUtil.validateObject(batchVo, new SmsRequestBatchValidator());
		} catch (ValidateException e) {
			return ResponseData.error(e.getErrorInfo().getErrorMsg());
		}
		List<String> mobiles = batchVo.getCellPhones();

		StringBuilder mobillesb = new StringBuilder();
		for (String mobile : mobiles) {
			mobillesb.append(mobile).append(":");
		}
		String mobileStrs = mobillesb.substring(0, mobillesb.length() - 1).toString();
		SmsRequest request = new SmsRequest();
		request.setCellphone(mobileStrs);
		request.setMap(batchVo.getReplaceMap());
		request.setSmsType(batchVo.getSmsType());
		request.setAppName(batchVo.getAppName());
		request.setType("1");
		// ThreadLocalUtils.setThreadLocalFlag(ThreadLocalUtils.BATCH);
		return sendSingleSms(request);
	}

	@Override
	public ResponseData getVerifyCode(SmsRequest vo) {
		logger.info("查询短信入口");
		try {
			ValidatorUtil.validateObject(vo, new SmsRequestValidator());
		} catch (ValidateException e) {
			return ResponseData.error(e.getErrorInfo().getErrorMsg());
		}
		String key = vo.getSmsType() + vo.getCellphone();
		return ResponseData.success(redisService.getString(key));
	}

	@Override
	public String generateVerifyCode(int digit) {
		String[] verificationCodeArrary = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		String verificationCode = "";
		Random random = new Random();
		// 此处是生成验证码
		for (int i = 0; i < digit; i++) {
			verificationCode += verificationCodeArrary[random.nextInt(verificationCodeArrary.length)];
		}
		return verificationCode;
	}

	/**
	 * map内有替换的字段 source 类似 ${}
	 *
	 * @return
	 */
	public static String render(String template, Map<String, Object> data) {
		return render(template, data, DEF_REGEX);
	}

	public static String render(String template, Map<String, Object> data, String regex) {
		if (StringUtils.isBlank(template)) {
			return "";
		}
		if (StringUtils.isBlank(regex)) {
			return template;
		}
		if (data == null || data.size() == 0) {
			return template;
		}
		try {
			StringBuffer sb = new StringBuffer();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(template);
			while (matcher.find()) {
				String name = matcher.group(1);// 键名
				Object value = data.get(name);// 键值
				if (value == null) {
					value = "";
				}
				matcher.appendReplacement(sb, value.toString());
			}
			matcher.appendTail(sb);
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return template;

	}

	private long getDuring(Integer during) {
		if (during == null) {
			long secondsLeftToday = 86400 - DateUtils.getFragmentInSeconds(Calendar.getInstance(), Calendar.DATE);
			return secondsLeftToday;
		} else {
			return during;
		}
	}

	/**
	 * 设置参数
	 */
	private String setParams(SmsRequest vo, SysSmsParam param) throws OverTimesException {

		String key = vo.getSmsType() + vo.getCellphone() + "sms";
		String cacheKey1 = vo.getSmsType() + vo.getCellphone();
		String cacheKey2 = cacheKey1 + "count";

		if (redisService.acquireLock(key, 40) == null) {
			return null;
		}
		try {
			String mobile = vo.getCellphone();
			Integer redis_count = (Integer) redisService.getObject(cacheKey2, 2);
			if (param.getCount() != null && redis_count != null && param.getCount() <= redis_count) {
				logger.warn("mobile {} exceeds day limits {}",mobile,redis_count);
				OverTimesException exception = new OverTimesException();
				exception.setErrorInfo(
						new ErrorInfo(ResultCode.MSG_OVER_TIMES.getCode(), ResultCode.MSG_OVER_TIMES.getMessage()));
				throw exception;
			}

			Map<String, Object> map = new HashMap<>();
			if (vo.getMap() != null) {
				map = vo.getMap();
			}
			// 需要发送短信验证码
			if (param.getCodeFlag() == 1) {
				String verifyCode = generateVerifyCode(6);
				map.put("验证码", verifyCode);
				redisService.setString(cacheKey1, verifyCode, 120000);
				if (param.getCount() != null) {
					Integer curentCount = (Integer) redisService.getObject(cacheKey2, 2);
					if (curentCount == null) {
						redisService.setObject(cacheKey2, 1, getDuring(param.getDuring()));
					} else {
						redisService.incr(cacheKey2, 1);
					}

				}

			}

//			// 如果是马甲包需要传入appName
//			if (StringUtils.isNotBlank(vo.getAppName())) {
//				try {
//					String appCode = vo.getAppName();
//					String appName = appNameConfigMapper.selectNameByCode(appCode);
//					map.put("appName", appName);
//				} catch (Exception e) {
//					e.printStackTrace();
//					throw new RuntimeException("select appName error!");
//				}
//			} else {
//				map.put("appName", "侬要贷");
//			}
			return render(param.getSmsTemplate(), map);
		} catch (Exception e) {
			if (e instanceof OverTimesException) {
				throw e;
			}

			e.printStackTrace();
		} finally {
			redisService.releaseLock(key);
		}
		return "";
	}

	private boolean isExceedLimit(SmsRequest message, String channelCode) {
		String appName = message.getAppName();
		String mobile = message.getCellphone();
		Integer smsType = message.getSmsType();
		if (StringUtils.isBlank(appName)) {
			appName = "侬要贷";
			logger.warn("appName is empty,set default value {} ", appName);
		}
		String key = "SendSmsMessage-" + appName + "-" + channelCode + "-" + mobile+"-"+smsType;
		long cnt = redisTemplate.opsForValue().increment(key, 1);
		if (cnt == 1) {
			logger.info("set key {} expire.",key);
			redisTemplate.expire(key, 60, TimeUnit.SECONDS);
		}
		if (cnt > rateLimit) {
			logger.warn("send sms count {} exceeds limit {}, app {} mobile {}", cnt, rateLimit, appName, mobile);
			return true;
		}

		return false;

	}
	
	public static void main(String[] args) {
		// SendSmsService sendSmsService = new SendSmsService();
		// System.out.println(sendSmsService.generateVerifyCode(6));
	}

	@Override
	public CommonResponse<JSONArray> smsReport() {
		logger.info("****获取短信回复***");
		CommonResponse<JSONArray> common = new CommonResponse<JSONArray>();
		List<SysSmsConfig> configList = sysSmsConfigDao.querySmsReportList();
		if(CollectionUtils.isEmpty(configList)){
			logger.warn("缺少短信平台.");
			common.setSuccess(false);
			return common;
		}
		SysSmsConfig config = configList.get(0);
		JSONObject json = new JSONObject();
		json.put("account", config.getSmsPlatAccount());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	    Date curerntDate = new Date();
	    String formatCurerntDate = format.format(curerntDate);
	    String encryptPswd = DigestUtils.md5Hex(config.getSmsPlatPwd());
		json.put("password", encryptPswd);
        String resultStr =  PostUtil.post(config.getSmsPlatUrlSingle(),String.valueOf(json));
        if(StringUtils.isBlank(resultStr)) {
        	common.setSuccess(false);
        	logger.error("大汉三通返回异常");
        	return common;
        } else {
        	logger.info(" 大汉三通短信返回：" + resultStr);
        	JSONObject retJson = JSONObject.parseObject(resultStr);
        	if(DaHanResultEnum.SUCCESS.getCode().equals(String.valueOf(retJson.get("result")))){
        		common.setSuccess(true);
        		String delivers = String.valueOf(retJson.get("delivers"));
        		if(com.alibaba.dubbo.common.utils.StringUtils.isNotEmpty(delivers)){
        			JSONArray array = JSONArray.parseArray(delivers);
            		common.setData(array);
        		}
        		
        	}
        }
		
		return common;
	}

	/**
	 * 增加短信流水记录
	 * @author WangXinHua
	 * @Date   2019/8/22 17:21
	 * @param message 请求参数
	 * @param response 返回参数
	 * @param channel 短信渠道号
	 * @return
	 * @throws
	 *
	 */
	public void saveSendSmsLog(Message message,String response,int channel){
		SendSmsLog sendSmsLog = new SendSmsLog();
		sendSmsLog.setCreateTime(new Date());
		sendSmsLog.setRequest(JSON.toJSONString(message));
		sendSmsLog.setChannel(channel);
		sendSmsLog.setResponse(response);
		sendSmsLog.setPhone(message.getCellPhones());
		sendSmsLog.setMsgCode(message.getMsgCode());
		sendSmsLog.setMsgId(message.getMsgId());
		sendSmsLogMapper.insert(sendSmsLog);
	}

}
