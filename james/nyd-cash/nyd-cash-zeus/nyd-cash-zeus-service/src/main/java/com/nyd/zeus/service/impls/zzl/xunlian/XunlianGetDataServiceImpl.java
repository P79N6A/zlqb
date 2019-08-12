package com.nyd.zeus.service.impls.zzl.xunlian;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.nyd.zeus.api.zzl.xunlian.XunlianGetDataService;
import com.nyd.zeus.model.helibao.util.chanpay.DateUtils;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFileVO;
import com.nyd.zeus.model.xunlian.req.IdentifyauthVO;
import com.nyd.zeus.model.xunlian.req.XunlianCancelBindVO;
import com.nyd.zeus.model.xunlian.req.XunlianChargeVO;
import com.nyd.zeus.model.xunlian.req.XunlianPaymentVO;
import com.nyd.zeus.model.xunlian.req.XunlianQueryChargeVO;
import com.nyd.zeus.model.xunlian.req.XunlianQueryPayVO;
import com.nyd.zeus.model.xunlian.req.XunlianSignVO;

@Service
public class XunlianGetDataServiceImpl implements XunlianGetDataService {

	private Logger logger = LoggerFactory.getLogger(XunlianGetDataServiceImpl.class);
	
	public static Map<String, String> bankListMap = null;
	private static final String GOODS_NAME = "2#2#商品1简称^100.00^1#商品2简称^50.00^20";

	@Override
	public Map<String,String> getIdentifyAuthData(IdentifyauthVO identifyauthVO,PayConfigFileVO payConfigFileVO) {
		if(null==bankListMap||bankListMap.isEmpty()){
			setBankList();
		}
		String organName = identifyauthVO.getOrganName();
		String organCode = getBankCode(organName);
        logger.info(" 银行名称： " + organName);
        if(StringUtils.isEmpty(organCode)){
        	logger.error(" 不支持的银行");
        	return null;
        }
		Map<String,String> map = convertBean(identifyauthVO, Map.class);
		map.put("cvv2", "");
		map.put("idType", "10");
		map.put("validDate", "");
		map.put("accountType", "90");
		map.put("version", "1.0.1");
		String reqDate = DateUtils.format(new Date(), DateUtils.DATETIME_FULL_QUICK_PAY);
		map.put("reqDate", reqDate);//
		map.put("merchantId", payConfigFileVO.getMemberId());
		map.put("organCode", organCode);
		//获得需要进行加签的字符串（通过拼接元素）
		String preSignStr = MerchantSignAndVerify.createLinkString(map);
		logger.info(preSignStr);
		//调用CFCA方法得到加签sign
		String signedString = new String(MerchantSignAndVerify.sign(preSignStr, payConfigFileVO.getMemberId(),payConfigFileVO));
		//加签sign放入map
		map.put("sign", signedString);
		return map;
	}

	/**
	 * * 方法说明：将bean转化为另一种bean实体 * * @param object * @param entityClass * @return
	 */
	public static <T> T convertBean(Object object, Class<T> entityClass) {
		if (null == object) {
			return null;
		}
		return JSON.parseObject(JSON.toJSONString(object), entityClass);
	}

	public static void setBankList() {
		bankListMap = new HashMap();
		bankListMap.put("4000100005", "工商");
		bankListMap.put("4000200006", "农业");
		bankListMap.put("4000300007", "中国银行");
		bankListMap.put("4000400008", "建设");
		bankListMap.put("4000500009", "邮储");
		bankListMap.put("4000600000", "交通");
		bankListMap.put("4000700001", "中信");
		bankListMap.put("4000800002", "光大");
		bankListMap.put("4000900003", "华夏");
		bankListMap.put("4001000005", "民生");
		bankListMap.put("4001100006", "广发");
		bankListMap.put("4001200007", "招商");
		bankListMap.put("4001300008", "兴业");
		bankListMap.put("4001400009", "浦发");
		bankListMap.put("4001500000", "平安");
		bankListMap.put("4001600001", "上海银行");
		bankListMap.put("4001800003", "恒丰");
		bankListMap.put("4001900004", "浙商");

	}

	public String getBankCode(String bankName) {
		for (Map.Entry<String, String> entry : bankListMap.entrySet()) {
        	if(bankName.contains(entry.getValue())){
        		return entry.getKey();
        	}
        }
		return null;
	}

	@Override
	public Map getSignData(XunlianSignVO xunlianSignVO, PayConfigFileVO payConfigFileVO) {
		Map<String,String> map = convertBean(xunlianSignVO, Map.class);

		map.put("cvv2", "");
		String reqDate = DateUtils.format(new Date(), DateUtils.DATETIME_FULL_QUICK_PAY);
		map.put("reqDate", reqDate);//
		map.put("merchantId", payConfigFileVO.getMemberId());
		map.put("validDate", "");
		map.put("version", "1.0.1");
		//获得需要进行加签的字符串（通过拼接元素）
		String preSignStr = MerchantSignAndVerify.createLinkString(map);
		logger.info(preSignStr);
		//调用CFCA方法得到加签sign
		String signedString = new String(MerchantSignAndVerify.sign(preSignStr, payConfigFileVO.getMemberId(),payConfigFileVO));
		//加签sign放入map
		map.put("sign", signedString);
		return map;
	}

	@Override
	public Map getPayData(XunlianPaymentVO xunlianPaymentVO, PayConfigFileVO payConfigFileVO) {
		Map<String,String> map = convertBean(xunlianPaymentVO, Map.class);

		map.put("orderDesc", GOODS_NAME);
		map.put("subMercId", "");
		map.put("subMercName", "");
		String reqDate = DateUtils.format(new Date(), DateUtils.DATETIME_FULL_QUICK_PAY);
		map.put("reqDate", reqDate);//
		map.put("merchantId", payConfigFileVO.getMemberId());
		map.put("validDate", "");
		map.put("version", "1.0.1");
		map.put("payType", "100001");
		//获得需要进行加签的字符串（通过拼接元素）
		String preSignStr = MerchantSignAndVerify.createLinkString(map);
		logger.info(preSignStr);
		//调用CFCA方法得到加签sign
		String signedString = new String(MerchantSignAndVerify.sign(preSignStr, payConfigFileVO.getMemberId(),payConfigFileVO));
		//加签sign放入map
		map.put("sign", signedString);
		return map;
	}

	@Override
	public Map getQueryPayData(XunlianQueryPayVO xunlianQueryPayVO, PayConfigFileVO payConfigFileVO) {
		Map<String,String> map = convertBean(xunlianQueryPayVO, Map.class);
		map.put("merchantId", payConfigFileVO.getMemberId());
		map.put("type", "1");
		map.put("serialNo", "");
		String reqDate = DateUtils.format(new Date(), DateUtils.DATETIME_FULL_QUICK_PAY);
		map.put("reqDate", reqDate);//
		map.put("version", "1.0.1");

		//获得需要进行加签的字符串（通过拼接元素）
		String preSignStr = MerchantSignAndVerify.createLinkString(map);
		logger.info(preSignStr);
		//调用CFCA方法得到加签sign
		String signedString = new String(MerchantSignAndVerify.sign(preSignStr, payConfigFileVO.getMemberId(),payConfigFileVO));
		//加签sign放入map
		map.put("sign", signedString);
		return map;
	}

	@Override
	public Map getCancelBindData(XunlianCancelBindVO xunlianCancelBindVO, PayConfigFileVO payConfigFileVO) {
		Map<String,String> map = convertBean(xunlianCancelBindVO, Map.class);
		
		map.put("merchantId", payConfigFileVO.getMemberId());
		String reqDate = DateUtils.format(new Date(), DateUtils.DATETIME_FULL_QUICK_PAY);
		map.put("reqDate", reqDate);//
		map.put("version", "1.0.1");

		//获得需要进行加签的字符串（通过拼接元素）
		String preSignStr = MerchantSignAndVerify.createLinkString(map);
		logger.info(preSignStr);
		//调用CFCA方法得到加签sign
		String signedString = new String(MerchantSignAndVerify.sign(preSignStr, payConfigFileVO.getMemberId(),payConfigFileVO));
		//加签sign放入map
		map.put("sign", signedString);
		return map;
	}

	@Override
	public Map getChargeData(XunlianChargeVO xunlianChargeVO, PayConfigFileVO payConfigFileVO) {
		Map<String,String> map = convertBean(xunlianChargeVO, Map.class);
		if(null==bankListMap||bankListMap.isEmpty()){
			setBankList();
		}
		map.put("merchantId", payConfigFileVO.getMemberId());
		String reqDate = DateUtils.format(new Date(), DateUtils.DATETIME_FULL_QUICK_PAY);
		map.put("reqDate", reqDate);//
		map.put("version", "1.0.1");
		map.put("subMercId", "");
		map.put("subMercName", "");
		map.put("idType", "10");
		map.put("accountType", "90");
		
		//TODO
		String bankName = xunlianChargeVO.getBankName();
		map.remove("bankName");
		String organCode = getBankCode(bankName);
        logger.info(" 银行名称： " + bankName);
        if(StringUtils.isEmpty(organCode)){
        	logger.error(" 不支持的银行");
        }
		map.put("organCode", organCode);

		//获得需要进行加签的字符串（通过拼接元素）
		String preSignStr = MerchantSignAndVerify.createLinkString(map);
		logger.info(preSignStr);
		//调用CFCA方法得到加签sign
		String signedString = new String(MerchantSignAndVerify.sign(preSignStr, payConfigFileVO.getMemberId(),payConfigFileVO));
		//加签sign放入map
		map.put("sign", signedString);
		return map;
	}

	@Override
	public Map getQueryChargeData(XunlianQueryChargeVO xunlianQueryChargeVO, PayConfigFileVO payConfigFileVO) {
		Map<String,String> map = convertBean(xunlianQueryChargeVO, Map.class);
		
		map.put("merchantId", payConfigFileVO.getMemberId());
		String reqDate = DateUtils.format(new Date(), DateUtils.DATETIME_FULL_QUICK_PAY);
		map.put("reqDate", reqDate);//
		map.put("version", "1.0.1");
		map.put("type", "0");
		map.put("serialNo", "");
		//获得需要进行加签的字符串（通过拼接元素）
		String preSignStr = MerchantSignAndVerify.createLinkString(map);
		logger.info(preSignStr);
		//调用CFCA方法得到加签sign
		String signedString = new String(MerchantSignAndVerify.sign(preSignStr, payConfigFileVO.getMemberId(),payConfigFileVO));
		//加签sign放入map
		map.put("sign", signedString);
		return map;
	}

}
