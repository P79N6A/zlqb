package com.nyd.capital.service.wsm;

import com.nyd.capital.model.WsmRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Cong Yuxiang
 * 2017/11/13
 **/
@Component
public class WsmBase {
    @Autowired
    private WsmConfig wsmConfig;
    private final String HEX_CHARS = "0123456789abcdef";
    /**
     * 初始化HttpsURLConnection.
     *
     * @throws Exception
     */
   public void initHttpsURLConnection() throws Exception{
       // 声明SSL上下文
       SSLContext sslContext = null;
       // 实例化主机名验证接口
       HostnameVerifier hnv = new MyHostnameVerifier();

           sslContext = getSSLContext(wsmConfig.getClientkeystorePassword(), wsmConfig.getClientkeystore(), wsmConfig.getClientkeystore());

       if (sslContext != null) {
           HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
                   .getSocketFactory());
       }
       HttpsURLConnection.setDefaultHostnameVerifier(hnv);
   }

    /**
     * 获得SSLSocketFactory.
     *
     * @param password
     *            密码
     * @param keyStorePath
     *            密钥库路径
     * @param trustStorePath
     *            信任库路径
     * @return SSLSocketFactory
     * @throws Exception
     */
    public  SSLContext getSSLContext(String password,
                                           String keyStorePath, String trustStorePath) throws Exception{
        // 实例化密钥库
        KeyManagerFactory keyManagerFactory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        // 获得密钥库
        KeyStore keyStore = getKeyStore(password, keyStorePath);
        // 初始化密钥工厂
        keyManagerFactory.init(keyStore, password.toCharArray());

        // 实例化信任库
        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        // 获得信任库
        KeyStore trustStore = getKeyStore(password, trustStorePath);
        // 初始化信任库
        trustManagerFactory.init(trustStore);
        // 实例化SSL上下文
        SSLContext ctx = SSLContext.getInstance("TLS");
        // 初始化SSL上下文
        ctx.init(keyManagerFactory.getKeyManagers(),
                trustManagerFactory.getTrustManagers(), null);
        // 获得SSLSocketFactory
        return ctx;
    }
    /**
     * 获得KeyStore.
     *
     * @param keyStorePath
     *            密钥库路径
     * @param password
     *            密码
     * @return 密钥库
     * @throws Exception
     */
    public  KeyStore getKeyStore(String password, String keyStorePath) throws Exception{
        // 实例化密钥库
        KeyStore ks = KeyStore.getInstance("JKS");

        // 获得密钥库文件流
        FileInputStream is = new FileInputStream(keyStorePath);
        // 加载密钥库
        ks.load(is, password.toCharArray());
        // 关闭密钥库文件流
        is.close();
        return ks;
    }

    //设置订单内的值（加密/转型/替换）
//    public Map setOrderVals(Map rowData  , String orderKey , Object orderVal) throws Exception {
//        String setOrderVal="";
//        //测试数据，简单拼接
//        if (orderVal == null ||  orderVal.equals("")) {
//            rowData.put(orderKey, setOrderVal);
//        }else{
//            setOrderVal  = orderVal.toString();					//obj转string
//            if(wsmConfig.getEncryptKeys().contains(orderKey)){				//查看key是否存在需要加密的字段列表中
//                setOrderVal =  wsmConfig.encrypt(setOrderVal,wsmConfig.getCsjmsy(),wsmConfig.getCsjmsy());
//            }
//
//            if(wsmConfig.getIntKeys().contains(orderKey)){						//查看key是否存在需要转整型字段的列表中
//                rowData.put(orderKey, Integer.parseInt(setOrderVal)); 	//String 转 Integer
//            }else{
//                rowData.put(orderKey, setOrderVal);
//            }
//        }
//        return rowData;
//    }

    //创建签名
    public  String generateSendSign(WsmRequest param) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//      Sign= Md5(Sha1(mid+shmyc+shddh+xm+sfzh+sjh+kh+sqje)+timestamp)
        String sha1 = SHA1Utilencrypt(new StringBuilder()
                .append(param.getMid())
                .append(param.getShmyc())
                .append(param.getShddh())
                .append(param.getXm())
                .append(param.getSfzh())
                .append(param.getSjh())
                .append(param.getKh())
                .append(param.getSqje()).toString());
        String orderSign = getMD5(sha1 + param.getTimestamp());
        return orderSign.toLowerCase();		//英文大写转小写
    }
    //md5加密
    public  String getMD5(String message) {
        String md5str = "";
        try {
            // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 2 将消息变成byte数组
            byte[] input = message.getBytes();

            // 3 计算后获得字节数组,这就是那128位了
            byte[] buff = md.digest(input);

            // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            md5str = bytesToHex(buff);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }

    /**
     * 二进制转十六进制
     *
     * @param bytes
     * @return
     */
    public  String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        // 把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toUpperCase();
    }
    //sha1加密
    public  String SHA1Utilencrypt(String content) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(content.getBytes());
            byte result[] = md.digest();
            return toHexString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public  String toHexString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_CHARS.charAt(b[i] >>> 4 & 0x0F));
            sb.append(HEX_CHARS.charAt(b[i] & 0x0F));
        }
        return sb.toString();
    }



    public int getSecondTimestamp(){
        Date date=new Date();
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0,length-3));
        } else {
            return 0;
        }
    }


    /**
     * 发送请求.

     */
    public String post(String path, String post) {
        String line1 = null;
        HttpsURLConnection urlCon = null;
        try {
            urlCon = (HttpsURLConnection) (new URL(path)).openConnection();
            urlCon.setDoInput(true);
            urlCon.setDoOutput(true);
            urlCon.setRequestMethod("POST");
            urlCon.setRequestProperty("Content-Length",
                    String.valueOf(post.getBytes().length));
            urlCon.setUseCaches(false);
            post = "data=" + URLEncoder.encode(post, "utf-8");
            byte[] bytes = post.getBytes("utf-8");
            urlCon.getOutputStream().write(bytes);
            urlCon.getOutputStream().flush();
            urlCon.getOutputStream().close();
            InputStream inputStream = urlCon.getInputStream();
            int available = inputStream.available();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    inputStream));
            String line;

            while ((line = in.readLine()) != null) {
                line1 = line;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line1;
    }

    //查询订单请求方法
    public  String postQuery(String path, String post) {

        String line1 = null;
        HttpsURLConnection urlCon = null;
        try {
            urlCon = (HttpsURLConnection) (new URL(path)).openConnection();
            urlCon.setDoInput(true);
            urlCon.setDoOutput(true);
            urlCon.setRequestMethod("POST");
            urlCon.setRequestProperty("Content-Length",
                    String.valueOf(post.getBytes().length));
            urlCon.setUseCaches(false);
            post = "data=" + post;
            byte[] bytes = post.getBytes("utf-8");
            urlCon.getOutputStream().write(bytes);
            urlCon.getOutputStream().flush();
            urlCon.getOutputStream().close();
            InputStream inputStream = urlCon.getInputStream();
            int available = inputStream.available();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    inputStream));
            String line;

            while ((line = in.readLine()) != null) {
                line1 = line;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line1;
    }


}
