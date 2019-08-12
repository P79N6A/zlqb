package com.nyd;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import org.junit.Before;
import org.junit.Test;


public class AliPayTest {
    private final String SERVER_URL = "https://openapi.alipaydev.com/gateway.do";//支付宝网关（沙箱测试固定）
//  private final String SERVER_URL = "https://openapi.alipay.com/gateway.do";//支付宝网关（固定）

    //应用私钥
    private final String PRIVATE_KEY ="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCV+175Ruyy8pAlVvX+2Md3u32s2ym2Wy4QQWPEiQkkksXLlkcwTV0Nr3hUUWIaB84F+wdiSqVsH/0hvxxXSbkpH/eieS+UcFqLkiZw+sCYajZGr9moe5TlMq9R0xI6sFAI7hKU3Az/l98GghLmy3KdyYSYXUDYdEGCSmtfDgouCDOu/OzMtuWW6dFA0qxQ9trXeLGzvjEAXHAE6bXXu9+sKK77blocuVUtHOVqsITq6AHAfPSSUKIixbTWI+2yJgOeRwgKaAuPTaEEtmq0JN85nXcjw5H8rGSF3FV5tui5NSgWHnQPvt6wOwSv3LtnLkgsFxmDqtnp/92q7ocqGnABAgMBAAECggEBAIcWm4uhQ09GxWisc/OwI7GHcYIBAQgcBlhj3U3HNDH59SgTWll6RScvdg1UJC1X/ExMt3Dj1S2XfnMn5IoGgD03at+vkZh8vkFaST+Xy2M8v1hZyY1Eim8KgPkNSS0NY3k3ngrwTNDmZN5CyG2Sr9DJEqH91Ith4yNMjntWdPrRzQzXL/uezjHI6VMF2jDN2UIJ+mpvh1BO4rbwxdUbzoxLqzUghxqz0Zi0fRcLG3hBBYns66k6GXyhWjz5LoDlY1/XoNVxiRgG5JvuYjdlZFPiLW3JmZM/WEl0ovCY3lQCnqBJGpFSc05Kb5EXOkCOXke0Kx5MaxZpD2HVIG4Sj5UCgYEAzO3eeQIkFLvwfwhMnhA4ztXbFvY1KhpkcA5joKa+35vlnTG5D6Xz+Drr22ykoLpvXpst5K7dFLiZCNCFdRXq79BXcJfMfHkpS8Q2OqVUeKZRyK9RuXkZRrUL23qANf/tsQbba4xnnz67Z1fnXVAMm0iWMepsZVHrWKC6Ex0goicCgYEAu1v1+0i3UMo4Xq5Me6j+6tEetMpAS3FlzNdHapATR7dvBFcZbp6JLwIDE/XiNkV32DP4QMtlWbdgH/vNi6ufavy2knOJ2WeYwv8qhXFYPaFFZ5l7Njd1fTbaJWEt/Fi+01J79ZtbtBEWSlwoTWhNPEKl1FP0wxH0Jnvkq+U8vZcCgYBYusWQ73Rf5eADoLxL1ApExCPQlVn/mN3UBubsMEaE2yNYJKdUNy6jVB+7Khc94HtIIziDw5LF3gek4WRnhc3rd/HkNlW7r1pKC/LPAbbJZnoSwpZIED75dnpj1W6d8uoftDBdKLXVJXxPCqiKppZOxecCRdYZKJcWEMGAdSQkEQKBgCn3t2nKKAXYxjevI0Kv4CoJHa+xfiVoCT2RG1YrX6Y42I1IVVIKAQCemsHl2f3+IZI0Ue9xoAowoT+Iz8nxb4wvf1Xeu4oPQpCrR4MMmI91iG5kWs0SLycVGlg1JG/aESSU3V9upWP4T93oNr7gFizhYPPmunTXmLDmH5rBhpGRAoGAESOnJQYc9CjmavNbZnZa2noGmxmYjyOKB+JFp+vvg6xpBM4zoQ+ntzT7WTlRg/ggzsWUwYdQLmuBp5f/V3Tz+w0+1egJJLr0QKc3Bkgi3LH4Py2lDN0ivYKlOWKDTprkRaZgihmE02KqkGLcAGzKNNEhLjrUbvHATByXL42kNIs=";
    //以下信息不需要修改
    private final String FORMAT = "json";
    private final String CHARSET = "utf-8";
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxQtJ51tIGi+o6HyWyljqtE/hFng4o0C9JU8dyR/kLKvL0EtfwpEtj5swaiSJSywzzkNdIRROW3geBFXNuDzkCtH6NNgdMm5fqNeKM4xk1X5wdWcCZvJg7lrddoIKvfGB2xFp7fCm0bfyhsUdvwz0unDmwHXuhwptWDn4EVu86sLIuK1IHU/UvUhiGQ0VMHhDv2r00inHK8biOTlEw9TjgTm2llv59ak7pg3GvVtQMl3JrniMNtjdgpN3DoCZUg1wj0n2xOU9Z+Vefotkz3TwpLLBPnjgNO4xcYp0nsnd8vBLXyrmpk5F4VOizpZV/V8RaQUNgVr3EM+YhRd9OqLHqwIDAQAB";
    private final String SIGN_TYPE = "RSA2";

    //app支付
    private final String APP_ID = "2016090900468212";//沙箱APPID(测试)

    private String AUTH_CODE = "287108181444710874";//付款码(一码一用)

    private AlipayClient alipayClient;
    @Before
    public void setUp(){
        alipayClient = new DefaultAlipayClient(SERVER_URL, APP_ID, PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
    }

    @Test
    public void testTradePay() {//当面付
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();

        model.setOutTradeNo(System.currentTimeMillis()+"");//订单号
        model.setSubject("单车");//商品说明
        model.setTotalAmount("10");//金额
        model.setAuthCode(AUTH_CODE);//沙箱钱包中的付款码
        model.setScene("bar_code");//wave_code 声波支付; bar_code 条码支付

        try {
            request.setBizModel(model);
            AlipayTradePayResponse response = alipayClient.execute(request);
            System.out.println(JSON.toJSONString(response));
            System.out.println(response.getBody());
            System.out.println(response.getTradeNo());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTransToaccount(){//单笔转账(退款)
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        //生成 转账信息
        String bizContent = getBizContent01();
        request.setBizContent(bizContent);

        try {
            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);

            System.out.println(response.isSuccess());
            System.out.println(response.getCode());
            System.out.println(response.getBody());
        } catch (AlipayApiException e) {

            e.printStackTrace();
        }
    }

    private String getBizContent01(){
        //qhskwo0031@sandbox.com(买家)  pcbwne3288@sandbox.com(商家)
        //ALIPAY_USERID     ALIPAY_LOGONID
        String outBizNo = System.currentTimeMillis()+"";

        JSONObject object = new JSONObject();
        object.put("out_biz_no", outBizNo);//唯一订单号
        object.put("payee_type", "ALIPAY_LOGONID");//收款账户类型
        object.put("payee_account", "qhskwo0031@sandbox.com");//收款帐号
        object.put("payee_real_name", "沙箱环境");//收款方真实姓名

        object.put("amount", 0.1);//金额
        object.put("remark", "转账备注");//转账备注

        return object.toJSONString();
    }

    @Test
    public void testTransQuery(){//单条转账记录查询
        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
        String bizContent = "{out_biz_no:1501047155276}";
        request.setBizContent(bizContent);

        try {
            AlipayFundTransOrderQueryResponse response = alipayClient.execute(request);

            System.out.println(response.isSuccess());
            System.out.println(response.getCode());
            System.out.println(response.getBody());

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

}
