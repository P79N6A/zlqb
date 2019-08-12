package com.nyd.msg.service.channel;

import com.google.gson.Gson;
import com.nyd.msg.service.utils.DesUtil;
import com.nyd.msg.service.utils.MWMessage;
import com.nyd.msg.service.utils.Message;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Yuxiang Cong
 **/
@Component
public class MengWangChannelStrategy implements ChannelStrategy{
    Logger logger = LoggerFactory.getLogger(MengWangChannelStrategy.class);
    // json解析器
    private Gson gson					= new Gson();

    // http请求失败
    public static int			ERROR_310099			= -310099;

    // 请求超时时间(毫秒) 5秒
    public static int			HTTP_REQUEST_TIMEOUT	= 5 * 1000;

    // 响应超时时间(毫秒) 60秒
    public static int			HTTP_RESPONSE_TIMEOUT	= 60 * 1000;

    @Override
    public boolean sendSms(Message vo, boolean batch) {
        MWMessage mwMessage = new MWMessage();
        String username = vo.getSmsPlatAccount();
        String password = vo.getSmsPlatPwd();

        mwMessage.setUserid(username);

        String url = vo.getSmsPlatUrl();
        SimpleDateFormat sdf	= new SimpleDateFormat("MMddHHmmss");
        String timeStamp = sdf.format(Calendar.getInstance().getTime());
        mwMessage.setTimestamp(timeStamp);
        // 短信相关的必须参数
        String mobile = vo.getCellPhones();
        mobile = mobile.replaceAll(";",",");//梦网用逗号间隔
        mwMessage.setMobile(mobile);
        String message = vo.getSmsTemplate();
        try {
            password = encryptPwd(username,DesUtil.decrypt(password),timeStamp);
            message = URLEncoder.encode(message, "GBK");
            mwMessage.setPwd(password);
            mwMessage.setContent(message);

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

            String flag = executeNotKeepAliveNotUrlEncodePost(mwMessage, url);
        logger.info("梦网结果:"+flag);
            if (String.valueOf(ERROR_310099).equals(flag)) {

                return false;
            } else {

                return true;
            }


    }
    private String executeNotKeepAliveNotUrlEncodePost(MWMessage message,String httpUrl)
    {
        String result = String.valueOf(ERROR_310099);
        HttpClient httpclient = null;
        try
        {
            //将实体对象，生成JSON字符串
            String entityValue = gson.toJson(message);
            // 定义请求头
            HttpPost httppost = new HttpPost(httpUrl);
            httppost.setHeader("Content-Type", "text/json");


            StringEntity stringEntity = new StringEntity(entityValue, HTTP.UTF_8);

            // 设置参数的编码UTF-8
            httppost.setEntity(stringEntity);

            // 创建连接
            httpclient = new DefaultHttpClient();

            // 设置请求超时时间 设置为5秒
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HTTP_REQUEST_TIMEOUT);
            // 设置响应超时时间 设置为60秒
            httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, HTTP_RESPONSE_TIMEOUT);

            HttpEntity entity = null;
            HttpResponse httpResponse=null;

            try
            {
                // 向网关请求
                httpResponse=httpclient.execute(httppost);
                // 若状态码为200，则代表请求成功
                if(httpResponse!=null && httpResponse.getStatusLine().getStatusCode()==200)
                {
                    //获取响应的实体
                    entity=httpResponse.getEntity();
                    //响应的内容不为空，并且响应的内容长度大于0,则获取响应的内容
                    if(entity != null && entity.getContentLength() > 0)
                    {
                        try
                        {
                            //请求成功，能获取到响应内容
                            result = EntityUtils.toString(entity);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            //获取内容失败，返回空字符串
                            result="";
                        }
                    }else
                    {
                        //请求成功，但是获取不到响应内容
                        result="";
                    }
                }else
                {
                    // 设置错误码
                    result = String.valueOf(ERROR_310099);
                    System.out.println("请求失败："+httpResponse.getStatusLine().toString());
                }

            }catch (Exception e)
            {
                result = String.valueOf(ERROR_310099);
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            result = String.valueOf(ERROR_310099);
            e.printStackTrace();
        }
        finally
        {
            // 关闭连接
            if(httpclient != null)
            {
                try
                {
                    httpclient.getConnectionManager().shutdown();
                }
                catch (Exception e2)
                {
                    // 关闭连接失败
                    e2.printStackTrace();
                }

            }
        }
        return result;

    }
    /**
     *
     *
     * @description 对密码进行加密
     * @param userid
     *        用户账号
     * @param pwd
     *        用户原始密码
     * @param timestamp
     *        时间戳
     * @return 加密后的密码
     */
    public String encryptPwd(String userid, String pwd, String timestamp)
    {
        // 加密后的字符串
        String encryptPwd = null;
        try
        {
            String passwordStr = userid + "00000000" + pwd + timestamp;
            // 对密码进行加密
            encryptPwd = getMD5Str(passwordStr);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // 返回加密字符串
        return encryptPwd;
    }

    /**
     *
     *
     * @description MD5加密方法
     * @param str
     *        需要加密的字符串
     * @return 返回加密后的字符串
     */
    private static String getMD5Str(String str)
    {
        MessageDigest messageDigest = null;
        // 加密前的准备
        try
        {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            //初始化加密类失败，返回null。
            return null;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            //初始化加密类失败，返回null。
            return null;
        }

        byte[] byteArray = messageDigest.digest();

        // 加密后的字符串
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++)
        {
            if(Integer.toHexString(0xFF & byteArray[i]).length() == 1){
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            }
            else{
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }


}
