package com.creativearts.nyd.pay.service.baofoo.config;

import com.creativearts.nyd.pay.service.baofoo.rsa.RsaCodingUtil;
import com.creativearts.nyd.pay.service.baofoo.util.*;
import lombok.Data;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/12/12
 **/
@Component
@Data
public class BaofooConfig {
    @Value("${baofoo.char.set}")
    private String charSet;

    @Value("${baofoo.pfx.name}")
    private String pfxName;

    @Value("${baofoo.pfx.pwd}")
    private String pfxPwd;

    @Value("${baofoo.cer.name}")
    private String cerName;

    @Value("${baofoo.terminal.id}")
    private String terminalId;

    @Value("${baofoo.member.id}")
    private String memberId;

    @Value("${baofoo.data.type}")
    private String dataType;

    @Value("${baofoo.version}")
    private String version;

    @Value("${baofoo.url}")
    private String url;

    @Value("${baofoo.aesKey}")
    private String aesKey;


    @Autowired
    private RestTemplate restTemplate;
    /**
     * 请求的明文参数  快捷支付
     * @return
     */
    public  Map<String,String> getPostHeadQuick(){

        Map<String, String> HeadPostParam = new HashMap<String, String>();
        HeadPostParam.put("version", version);// 版本号
        HeadPostParam.put("input_charset", "1");// 字符集 1 utf-8
        HeadPostParam.put("terminal_id", terminalId);// 终端号
        HeadPostParam.put("member_id", memberId);// 商户号
        HeadPostParam.put("data_type", dataType);// 加密数据类型
        HeadPostParam.put("txt_sub_type","01");//01普通 02分账
        return HeadPostParam;
    }

    /**
     * 发送报文 响应数据
     *
     * @param HeadPostParam
     * @param XMLArray
     * @param request_url
     * @return
     * @return
     * @throws Exception
     */
    public  String Send(Map<String, String> HeadPostParam, Map<String, Object> XMLArray, String request_url) throws IOException {

        Map<Object, Object> ArrayToObj = new HashMap<Object, Object>();

        String XmlOrJson = "";
        if (HeadPostParam.get("data_type").equals("xml")) {
            ArrayToObj.putAll(XMLArray);
            XmlOrJson = MapToXMLString.converter(ArrayToObj, "data_content");
        } else {
            JSONObject jsonObjectFromMap = JSONObject.fromObject(XMLArray);
            XmlOrJson = jsonObjectFromMap.toString();
        }
        Log.Write("请求参数：" + XmlOrJson);
        String base64str = SecurityUtil.Base64Encode(XmlOrJson);

//        String pfxpath = BaofooConfig.getConstants().get("pfx.name");// 商户私钥
        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, pfxName,	pfxPwd);
        Log.Write("加密结果："+data_content);
        HeadPostParam.put("data_content", data_content);// 加密数据
        String PostString = HttpUtil.RequestForm(request_url, HeadPostParam);




        Log.Write("返回参数：" + PostString);
        return PostString;

    }

    /**
     * 解析宝付结果
     * @param str
     */
    public String process(String str) throws IOException {

        String data_content = RsaCodingUtil.decryptByPubCerFile(str,cerName);

        if(data_content.isEmpty()){//判断解密是否正确。如果为空则宝付公钥不正确
            Log.Write("=====检查解密公钥是否正确！");
            return null;
        }
        data_content = SecurityUtil.Base64Decode(data_content);
        Log.Write("=====返回数据解密结果:"+data_content);
        if(dataType.equals("xml")){
            data_content = JXMConvertUtil.XmlConvertJson(data_content);
            Log.Write("=====返回结果转JSON:"+data_content);
        }

        Map<String,Object> ArrayDataString = JXMConvertUtil.JsonConvertHashMap(data_content);//将JSON转化为Map对象。

        if(!ArrayDataString.containsKey("resp_code")){
            Log.Write("====解析出来 没有 resp_code");
            return null;
        }
        return data_content;
    }
    /**
     * 获取设备指纹
     * @param Session_id
     * @return
     */
//    public static Map<String,Object> GetDviceId(String Session_id){
//        Map<String,String> HeadPostParam = new HashMap<String,String>();
//        HeadPostParam.put("member_id", BaofooConfig.getConstants().get("member.id"));
//        HeadPostParam.put("session_id",HeadPostParam.get("member_id")+Session_id);
//        String PostString  = HttpUtil.RequestForm("https://fk.baofoo.com/getDeviceMem", HeadPostParam);
//
//        if(null == PostString ||PostString.isEmpty()){
//            return null;
//        }
//        Log.Write("返回DVICE_ID:"+PostString);
//        Map<String,Object> ArrayDataString = JXMConvertUtil.JsonConvertHashMap(PostString);
//        return ArrayDataString;
//    }

}
