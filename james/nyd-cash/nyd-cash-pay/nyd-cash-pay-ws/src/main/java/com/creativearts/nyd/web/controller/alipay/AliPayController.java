package com.creativearts.nyd.web.controller.alipay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.creativearts.nyd.pay.config.Prop;
import com.creativearts.nyd.pay.config.PropKit;
import com.creativearts.nyd.pay.config.alipay.AliPayApiConfig;
import com.creativearts.nyd.pay.config.utils.StringUtils;
import com.creativearts.nyd.pay.model.AjaxResult;
import com.creativearts.nyd.pay.service.alipay.AliPayApi;
import com.nyd.zeus.model.PayModelEnum;
import com.nyd.zeus.model.RepayInfo;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 */
@RestController
@RequestMapping(value = "/pay/ali")
public class AliPayController extends AliPayApiController {
	private Logger log = LoggerFactory.getLogger(AliPayController.class);
	
	private  final Prop prop = PropKit.use("alipay.properties");
	private  String charset = "UTF-8";
	private  String private_key = prop.get("privateKey");
	private  String alipay_public_key = prop.get("publicKey");
	private  String service_url = prop.get("serverUrl");
	private  String app_id = prop.get("appId");
	private  String sign_type = "RSA2";
	private  String notify_domain = prop.get("domain");
	
	private AjaxResult result = new AjaxResult();

	/*@Autowired(required = false)
	private RepayContract repayContract;*/

	@Override
	public AliPayApiConfig getApiConfig() {
		AliPayApiConfig aliPayApiConfig = AliPayApiConfig.New()
		.setAppId(app_id)
		.setAlipayPublicKey(alipay_public_key)
		.setCharset(charset)
		.setPrivateKey(private_key)
		.setServiceUrl(service_url)
		.setSignType(sign_type)
		.build();
		return aliPayApiConfig;
	}
	@RequestMapping(value = "/index" ,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String index() throws UnsupportedEncodingException {
		System.out.println(getResponse());
		System.out.println(getRequest());
		System.out.println(getResponse()==null);
		System.out.println(getRequest()==null);
		return "欢迎使用支付宝支付";
	}

	/**
	 * app支付
	 */
	@RequestMapping("/appPay")
	@ResponseBody
	public AjaxResult appPay() throws AlipayApiException{

			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			model.setBody("我是测试数据");
			model.setSubject("App支付测试");
			String a = StringUtils.getOutTradeNo();
			model.setOutTradeNo(a);
			model.setTimeoutExpress("30m");
			model.setTotalAmount("0.01");
			model.setPassbackParams("callback params");
			model.setProductCode("QUICK_MSECURITY_PAY");
			String orderInfo = AliPayApi.startAppPayStr(model, notify_domain + "/app_pay_notify");
			result.success(orderInfo);
			return result;

	}

	/**
	 * Wap支付
	 */
	@RequestMapping("/wapPay")
	public void wapPay() throws IOException, AlipayApiException {
		String body = "我是测试数据";
		String subject = "Wap支付测试";
		String totalAmount = "100";
		String passbackParams = "1";
		String returnUrl = notify_domain + "/return_url";
		String notifyUrl = notify_domain + "/notify_url";

		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setBody(body);
		model.setSubject(subject);
		model.setTotalAmount(totalAmount);
		model.setPassbackParams(passbackParams);
		String outTradeNo = StringUtils.getOutTradeNo();
		System.out.println("wap outTradeNo>"+outTradeNo);
		model.setOutTradeNo(outTradeNo);
		model.setProductCode("QUICK_WAP_PAY");


		AliPayApi.wapPay(getResponse(), model, returnUrl, notifyUrl);

	}
	
	/**
	 * PC支付
	 */
	@RequestMapping()
	public void pcPay() throws IOException, AlipayApiException {

			String totalAmount = "88.88"; 
			String outTradeNo =StringUtils.getOutTradeNo();
			log.info("pc outTradeNo>"+outTradeNo);
			
			String returnUrl = notify_domain + "/alipay/return_url";
			String notifyUrl = notify_domain + "/alipay/notify_url";
			AlipayTradePayModel model = new AlipayTradePayModel();
			
			model.setOutTradeNo(outTradeNo);
			model.setProductCode("FAST_INSTANT_TRADE_PAY");
			model.setTotalAmount(totalAmount);
			model.setSubject("PC支付测试");
			model.setBody("PC支付测试");
			//花呗分期相关的设置
			/**
			 * 测试环境不支持花呗分期的测试
			 * hb_fq_num代表花呗分期数，仅支持传入3、6、12，其他期数暂不支持，传入会报错；
			 * hb_fq_seller_percent代表卖家承担收费比例，商家承担手续费传入100，用户承担手续费传入0，仅支持传入100、0两种，其他比例暂不支持，传入会报错。
			 */
//			ExtendParams extendParams = new ExtendParams();
//			extendParams.setHbFqNum("3");
//			extendParams.setHbFqSellerPercent("0");
//			model.setExtendParams(extendParams);
			
			AliPayApi.tradePage(getResponse(),model , notifyUrl, returnUrl);

		
	}

	//查询接口 用于查看支付宝支付信息
	@RequestMapping("/queryByBillNo")
	public void tradeQueryByStr() {
		String out_trade_no = getRequest().getParameter("out_trade_no");

		AlipayTradeQueryModel model = new AlipayTradeQueryModel();
		model.setOutTradeNo(out_trade_no);

		try {
			String resultStr = AliPayApi.tradeQuery(model).getBody();
			renderText(resultStr);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}



	


	

	/**
	 * 关闭订单
	 */
	@RequestMapping("/tradeClose")
	public void tradeClose(){
		String outTradeNo = getRequest().getParameter("out_trade_no");
		String tradeNo = getRequest().getParameter("trade_no");
		try {
			AlipayTradeCloseModel model = new AlipayTradeCloseModel();
			model.setOutTradeNo(outTradeNo);

			model.setTradeNo(tradeNo);

			String resultStr = AliPayApi.tradeClose(model).getBody();
			renderText(resultStr);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}
	

	@RequestMapping(value = "/return_url")
	public String return_url() throws AlipayApiException {

			// 获取支付宝GET过来反馈信息
			Map<String, String> map = AliPayApi.toMap(getRequest());
			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}

			boolean verify_result = AlipaySignature.rsaCheckV1(map, alipay_public_key, charset,
					sign_type);

			if (verify_result) {// 验证成功
				// TODO 请在这里加上商户的业务逻辑程序代码
				System.out.println("return_url 验证成功");
				return "success";
			} else {
				System.out.println("return_url 验证失败");
				// TODO
				return "failure";
			}

	}
	@RequestMapping(value = "/notify_url",produces="text/plain;charset=UTF-8")
	public String notify_url() throws Exception {
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = AliPayApi.toMap(getRequest());

//			for (Map.Entry<String, String> entry : params.entrySet()) {
//				System.out.println(entry.getKey() + " = " + entry.getValue());
//			}
			System.out.println(JSON.toJSONString(params));
			boolean verify_result = AlipaySignature.rsaCheckV1(params, alipay_public_key, charset,
					sign_type);

			if (verify_result) {// 验证成功
				RepayInfo repayInfo = new RepayInfo();
				repayInfo.setUserId(params.get("passback_params"));
				repayInfo.setBillNo(params.get("out_trade_no"));
				repayInfo.setRepayChannel(PayModelEnum.ZFB.getCode());
				repayInfo.setRepayNo(params.get("trade_no"));
				repayInfo.setUserZfbId(params.get("buyer_id"));
				repayInfo.setUserZfbName(params.get("buyer_logon_id"));
				repayInfo.setRepayAmount(new BigDecimal(Double.valueOf(params.get("total_amount"))));
				if("TRADE_SUCCESS".equals(params.get("trade_status").trim())) {
					repayInfo.setRepayStatus("0");
				}else{
					repayInfo.setRepayStatus("1");
				}
				repayInfo.setRepayTime(DateUtils.parseDate(params.get("gmt_payment"),"yyyy-MM-dd HH:mm:ss"));
//				repayContract.save(repayInfo);
				System.out.println("notify_url 验证成功succcess");
				return "success";
			} else {
				System.out.println("notify_url 验证失败");
				// TODO
				return "failure";
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return "failure";
		}
	}

	//=======其实异步通知实现的方法都一样  但是通知中无法区分支付的方式(没有提供支付方式的参数)======================================================================
	/**
	 * App支付支付回调通知
	 * https://doc.open.alipay.com/docs/doc.htm?treeId=54&articleId=106370&
	 * docType=1#s3
	 */
	@RequestMapping("/app_pay_notify")
	public String app_pay_notify() throws AlipayApiException {

			// 获取支付宝POST过来反馈信息
			Map<String, String> params = AliPayApi.toMap(getRequest());
		System.out.println(JSON.toJSONString(params));
			for (Map.Entry<String, String> entry : params.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}
			// 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
			// boolean AlipaySignature.rsaCheckV1(Map<String, String> params,
			// String publicKey, String charset, String sign_type)
			boolean flag = AlipaySignature.rsaCheckV1(params, alipay_public_key, charset,
					sign_type);
			if (flag) {
				// TODO
				return "success";

			} else {
				// TODO
				return "failure";
			}

	}


	
}