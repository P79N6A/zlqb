package com.creativearts.nyd.web.controller.weixinpay;


import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.config.Prop;
import com.creativearts.nyd.pay.config.PropKit;
import com.creativearts.nyd.pay.config.utils.HttpKit;
import com.creativearts.nyd.pay.config.utils.IpKit;
import com.creativearts.nyd.pay.config.utils.StrKit;
import com.creativearts.nyd.pay.model.AjaxResult;
import com.creativearts.nyd.pay.service.weixinpay.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.creativearts.nyd.pay.service.weixinpay.H5ScencInfo.H5;


/**
 * 商户模式下微信支付
 */
@RestController
@RequestMapping("/pay/weixin")
public class WxPayController extends WxPayApiController {
    static Logger log = LoggerFactory.getLogger(WxPayController.class);
    private AjaxResult ajax = new AjaxResult();
    private static final Prop prop = PropKit.use("weixin/wxpay.properties");
    //商户相关资料
    String appid = prop.get("appId");
    String mch_id = prop.get("mchId");
    String partnerKey = prop.get("partnerKey");
    String notify_url = prop.get("domain") + "/pay_notify";

    @Override
    public WxPayApiConfig getApiConfig() {
        WxPayApiConfig apiConfig = WxPayApiConfig.New()
                .setAppId(appid)
                .setMchId(mch_id)
                .setPaternerKey(partnerKey)
                .setPayModel(WxPayApiConfig.PayModel.BUSINESSMODEL);
        return apiConfig;
    }

    @RequestMapping(value = "/index", produces = "text/plain;charset=UTF-8")
    public void index() {
        renderText("欢迎使用IJPay 商户模式下微信支付  - by Javen");
    }

    @RequestMapping(value = "/getKey", produces = "text/plain;charset=UTF-8")
    public void getKey() {
        String getsignkey = WxPayApi.getsignkey(mch_id, partnerKey);
        renderText(getsignkey);
    }

    /**
     * 微信H5 支付
     * 注意：必须再web页面中发起支付且域名已添加到开发配置中
     */
    @RequestMapping("/wapPay")
    public void wapPay() {
        String ip = IpKit.getRealIp(getRequest());
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        H5ScencInfo sceneInfo = new H5ScencInfo();

        H5 h5_info = new H5();
        h5_info.setType("Wap");
        //此域名必须在商户平台--"产品中心"--"开发配置"中添加
        h5_info.setWap_url("https://pay.qq.com");
        h5_info.setWap_name("腾讯充值");
        sceneInfo.setH5_info(h5_info);

        Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig()
                .setAttach("H5支付测试")
                .setBody("H5支付测试")
                .setSpbillCreateIp(ip)
                .setTotalFee("520")
                .setTradeType(WxPayApi.TradeType.MWEB)
                .setNotifyUrl(notify_url)
                .setOutTradeNo(String.valueOf(System.currentTimeMillis()))
                .setSceneInfo(h5_info.toString())
                .build();

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info(xmlResult);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");
        if (!PaymentKit.codeIsOK(return_code)) {
            ajax.addError(return_msg);
            renderText(JSON.toJSONString(ajax));
            return;
        }
        String result_code = result.get("result_code");
        if (!PaymentKit.codeIsOK(result_code)) {
            ajax.addError(return_msg);
            renderText(JSON.toJSONString(ajax));
            return;
        }
        // 以下字段在return_code 和result_code都为SUCCESS的时候有返回

        String prepay_id = result.get("prepay_id");
        String mweb_url = result.get("mweb_url");

        System.out.println("prepay_id:" + prepay_id + " mweb_url:" + mweb_url);
        try {
            getResponse().sendRedirect(mweb_url);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("跳转失败");
        }
//		redirect(mweb_url);
    }


    /**
     * 微信APP支付
     */
    @RequestMapping(value = "/appPay", produces = "application/json;charset=utf-8")
    public void appPay() {
        //不用设置授权目录域名
        //统一下单地址 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1#

        String ip = IpKit.getRealIp(getRequest());
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig()
                .setAttach("weixinPay测试attach")
                .setBody("weixinPay App付测试")
                .setSpbillCreateIp(ip)
                .setTotalFee("100")
                .setTradeType(WxPayApi.TradeType.APP)
                .setNotifyUrl(notify_url)
                .setOutTradeNo(String.valueOf(System.currentTimeMillis()))
                .build();

        String xmlResult = WxPayApi.pushOrder(false, params);

        log.info(xmlResult);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");
        if (!PaymentKit.codeIsOK(return_code)) {
            ajax.addError(return_msg);
            renderText(JSON.toJSONString(ajax));
            return;
        }
        String result_code = result.get("result_code");
        if (!PaymentKit.codeIsOK(result_code)) {
            ajax.addError(return_msg);
            renderText(JSON.toJSONString(ajax));
            return;
        }
        // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
        String prepay_id = result.get("prepay_id");
        //封装调起微信支付的参数 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12
        Map<String, String> packageParams = new HashMap<String, String>();
        packageParams.put("appid", WxPayApiConfigKit.getWxPayApiConfig().getAppId());
        packageParams.put("mch_id", WxPayApiConfigKit.getWxPayApiConfig().getMchId());
        packageParams.put("prepayid", prepay_id);
        packageParams.put("package", "Sign=WXPay");
        packageParams.put("noncestr", System.currentTimeMillis() + "");
        packageParams.put("timestamp", System.currentTimeMillis() / 1000 + "");
        String packageSign = PaymentKit.createSign(packageParams, WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey());
        packageParams.put("sign", packageSign);

        String jsonStr = JSON.toJSONString(packageParams);
        log.info("最新返回apk的参数:" + jsonStr);
        renderText(jsonStr);
    }


    @RequestMapping("/pay_notify")
    public void pay_notify() {
        //获取所有的参数
        StringBuffer sbf = new StringBuffer();

        Enumeration<String> en = getRequest().getParameterNames();
        while (en.hasMoreElements()) {
            Object o = en.nextElement();
            sbf.append(o.toString() + "=" + getRequest().getParameter(o.toString()));
        }

        log.error("支付通知参数：" + sbf.toString());

        // 支付结果通用通知文档: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
        String xmlMsg = HttpKit.readData(getRequest());
        System.out.println("支付通知=" + xmlMsg);
        Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);

//		String appid  = params.get("appid");
//		//商户号
//		String mch_id  = params.get("mch_id");
        String result_code = params.get("result_code");
//		String openId      = params.get("openid");
//		//交易类型
//		String trade_type      = params.get("trade_type");
//		//付款银行
//		String bank_type      = params.get("bank_type");
//		// 总金额
//		String total_fee     = params.get("total_fee");
//		//现金支付金额
//		String cash_fee     = params.get("cash_fee");
//		// 微信支付订单号
//		String transaction_id      = params.get("transaction_id");
//		// 商户订单号
//		String out_trade_no      = params.get("out_trade_no");
//		// 支付完成时间，格式为yyyyMMddHHmmss
//		String time_end      = params.get("time_end");

        /////////////////////////////以下是附加参数///////////////////////////////////

        String attach = params.get("attach");
//		String fee_type      = params.get("fee_type");
//		String is_subscribe      = params.get("is_subscribe");
//		String err_code      = params.get("err_code");
//		String err_code_des      = params.get("err_code_des");


        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        // 避免已经成功、关闭、退款的订单被再次更新
//		Order order = Order.dao.getOrderByTransactionId(transaction_id);
//		if (order==null) {
        if (PaymentKit.verifyNotify(params, WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey())) {
            if (("SUCCESS").equals(result_code)) {
                //更新订单信息
                log.warn("更新订单信息:" + attach);

                //发送通知等

                Map<String, String> xml = new HashMap<String, String>();
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                renderText(PaymentKit.toXml(xml));
                return;
            }
        }
//		}
        renderText("");
    }
}
