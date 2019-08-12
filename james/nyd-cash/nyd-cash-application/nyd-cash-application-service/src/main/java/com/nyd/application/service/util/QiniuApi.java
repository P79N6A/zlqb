package com.nyd.application.service.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Dengw on 2017/11/15
 * 七牛云存储操作
 */
@Component
public class QiniuApi {
    private static Logger LOGGER = LoggerFactory.getLogger(QiniuApi.class);
    @Autowired
    private AppProperties appProperties;

    //如果是Windows情况下，格式是 D:\\qiniu\\test.png
    //public static String localFilePath = "D:\\qiniu\\haha.jpg";

    public String base64Upload(String file) {
        byte[] fileBytes = base64Decode(file);
        String fileName = upload(fileBytes);
        return fileName;
    }

    public String upload(byte[] fileBytes) {
        String fileName = null;
        //七牛机房位置，华东，华北，华南
        Zone zone = getZone();
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(zone);
        //创建上传对象
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = UUID.randomUUID().toString().replaceAll("-", "");

        //创建认证对象
        Auth auth = Auth.create(appProperties.getQnAccessKey(), appProperties.getQnSecretKey());
        //设置存储空间
        String upToken = auth.uploadToken(appProperties.getQnBbucket(),key);

        try {
            Response response = uploadManager.put(fileBytes, key, upToken);
            //Response response = uploadManager.put(localFilePath, key, upToken);
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            fileName = putRet.hash;
        } catch (QiniuException e) {
            LOGGER.error("upload file to qiniu failed !"+e);
            key = null;
        }
        return key;
    }

    public String download(String fileName) {
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("encoded failed !",e.getMessage());
        }
        String publicUrl = String.format("%s/%s", appProperties.getQnDomainOfBucket(), encodedFileName);

        Auth auth = Auth.create(appProperties.getQnAccessKey(), appProperties.getQnSecretKey());
        //自定义链接过期时间,单位秒
        long expireInSeconds = 300;
        if(appProperties.getQnExpireInSeconds() != null){
            expireInSeconds = Long.parseLong(appProperties.getQnExpireInSeconds());
        }
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        return finalUrl;
    }

    public String downloadAgreeMentTempalte(String fileName) {
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("encoded failed !",e.getMessage());
        }
        String publicUrl = String.format("%s/%s", appProperties.getQnContractBucket(), encodedFileName);

        Auth auth = Auth.create(appProperties.getQnAccessKey(), appProperties.getQnSecretKey());
        long expireInSeconds = 60;//自定义链接过期时间,单位秒
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        return finalUrl;
    }

    /**
     * 获取七牛存储空间机房位置
     * @return Zone
     */
    private Zone getZone(){
        Zone zone = Zone.zone0();
        String zoneType = appProperties.getQnZone();
        if("1".equals(zoneType)){
            zone = Zone.zone1();
        }else if("2".equals(zoneType)){
            zone = Zone.zone2();
        }
        return zone;
    }

    /**
     * base64解码
     * @param str
     * @return byte
     */
    private byte[] base64Decode(String str) {
        //byte[] bytes = Base64.getDecoder().decode(str);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = new byte[0];
        try {
            bytes = decoder.decodeBuffer(str);
        } catch (IOException e) {
            LOGGER.error("encoded failed !");
        }
        return bytes;
    }
    
    /**
     * app上传图片获取QiNiuToken接口
     * @param bucket
     * @param keys
     * @return
     */
    public Map<String,Object> uploadByApp(String bucket, List<String> keys) {
        Map<String,Object> result = new HashMap<>();
        //创建认证对象
        Auth auth = Auth.create(appProperties.getQnAccessKey(), appProperties.getQnSecretKey());
        for (String key:keys) {
            //设置存储空间
            String upToken = auth.uploadToken(bucket,key);
            result.put(key,upToken);
        }
        return result;
    }

}
