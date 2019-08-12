package com.nyd.capital.service.pocket.util;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * 口袋给的实例类
 *
 * @author 3
 */
public class KdaiSignUtils {

    private static Logger logger = Logger.getLogger(KdaiSignUtils.class);

    /**
     * 生成签名字符串
     *
     * @param reqMap
     * @param key
     * @return
     */
    public static String createSign(Map<String, String> reqMap, String key) {

        // 加签原串
        String str = "";
        try {
            // 对请求参数进行urlencode加密
            for (Map.Entry<String, String> entry : reqMap.entrySet()) {
                reqMap.put(entry.getKey(), URLEncoder.encode(entry.getValue(), "utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 对参数进行升序排序
        List<Map.Entry<String, String>> sortMap = KdaiSignUtils.argSort(reqMap);

        for (int i = 0; i < sortMap.size(); i++) {
            str += sortMap.get(i).getKey() + "=" + sortMap.get(i).getValue() + "&";
        }
        // 清除最后一个&
        if (str.length() > 1) {
            str = str.substring(0, str.length() - 1);
        }
        str += ("&key_cert=" + key);

        return md5(new String(str.getBytes()));
    }

    /**
     * 对请求参数进行处理
     *
     * @param reqMap
     * @return
     */
    public static String reqStrUtil(Map<String, String> reqMap) {
        String reqStr = "";
        for (Map.Entry<String, String> entry : reqMap.entrySet()) {
            reqStr += entry.getKey() + "=" + entry.getValue() + "&";
        }
        return reqStr.substring(0, reqStr.length() - 1);
    }

    /**
     * 排序 a-z 根据键排序
     *
     * @param map
     * @return
     */
    public static List<Map.Entry<String, String>> argSort(Map<String, String> map) {
        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        // 然后通过比较器来实现排序
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            // 升序排序
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }

        });

        return list;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param requestUrl 发送请求的 URL
     * @param param      请求参数，请求参数是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPostRequestBody(String requestUrl, String param) throws Exception {
        logger.info("=======目标URL：" + requestUrl);
        logger.info("=======调用口袋理财请求参数：" + param);
        // System.out.println("=======调用口袋理财请求参数：" + param);
        OutputStreamWriter out = null;
        InputStream inputStream = null;
        String result = "";
        try {
            URL url = new URL(requestUrl);
            URLConnection urlConnection = url.openConnection();
            // 设置doOutput属性为true表示将使用此urlConnection写入数据
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
            // 得到请求的输出流对象
            out = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
            // 把数据写入请求的Body
            out.write(param);
            out.flush();
            // 从服务器读取响应
            inputStream = urlConnection.getInputStream();
            result = IOUtils.toString(inputStream, "utf-8");
            logger.info("=======口袋理财返回结果：" + result);
        } catch (Exception e) {
            logger.error("=======调用口袋理财出现异常：" + e);
        } finally {
            if (out != null) {
                out.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
        return result;
    }

    /**
     * 口袋回调验签
     *
     * @param params
     * @param securtyKey
     * @return
     */
    public static boolean verifySign(JSONObject params, String securtyKey){
        if (params.getString("sign") == null) {
            return false;
        }
        String resultSign = params.getString("sign");

        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap = (Map) params;
        resultMap.remove("sign");
        resultMap.put("code", params.get("code") + "");
        String sign = createSign(resultMap, securtyKey);
        return resultSign.equals(sign);

    }

    public static void main(String[] args){
        JSONObject params = new JSONObject();
        params.put("order_id", "KD_201708101408034_LJBTWQ");
        params.put("code", "0");
        params.put("opr_dat", "20170810");
        params.put("result", "&打款成功&");
        params.put("sign",
                "Y29kZT0wJm9wcl9kYXQ9MjAxNzA4MTAmb3JkZXJfaWQ9S0RfMjAxNzA4MTAxNDA4MDM0X0xKQlRXUSZyZXN1bHQ9JTI2JUU2JTg5JTkzJUU2JUFDJUJFJUU2JTg4JTkwJUU1JThBJTlGJTI2KiprZGxjKio=");
        System.out.println(verifySign(params, "**kdlc**"));
        System.out.println(md5("123"));
    }

    /**
     * HTTP POST 请求，参数是JSON串
     *
     * @param url
     * @param param
     * @return
     * @throws Exception
     */
    public static String httpPostWithJSON(String url, String param) throws Exception {
        logger.info("=======目标URL：" + url);
        logger.info("=======调用口袋理财请求参数：" + param);
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = HttpClients.createDefault();
        String respContent = null;
        // 解决中文乱码问题
        StringEntity entity = new StringEntity(param, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpResponse resp = client.execute(httpPost);
        if (resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he, "UTF-8");
        }
        logger.info("=======口袋理财返回结果：" + respContent);
        return respContent;
    }

    /**
     * MD5算法
     *
     * @param data
     * @return
     */
    public static String md5(String data) {
        // 如果有空则返回""
        String s = data == null ? "" : data;
        try {
            // 将字符串转为字节数组
            byte[] strTemp = s.getBytes("utf-8");
            // 加密器
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            // 执行加密
            mdTemp.update(strTemp);
            // 加密结果
            byte[] byteArray = mdTemp.digest();
            return Hex.encodeHexString(byteArray);
        } catch (Exception e) {
            return null;
        }
    }
}
