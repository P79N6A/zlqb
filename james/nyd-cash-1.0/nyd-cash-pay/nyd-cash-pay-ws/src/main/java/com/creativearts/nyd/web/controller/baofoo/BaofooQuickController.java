package com.creativearts.nyd.web.controller.baofoo;

import com.creativearts.nyd.pay.model.baofoo.PreOrderPayVo;
import com.creativearts.nyd.pay.service.baofoo.BaofooPayService;
import com.creativearts.nyd.web.controller.BaseController;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cong Yuxiang
 * 2017/12/5
 **/
@RestController
@RequestMapping("/nyd/pay/bfquick")
public class BaofooQuickController extends BaseController{

    @Autowired
    private BaofooPayService baofooPayService;

    @RequestMapping("/index")
    public String index(){
        System.out.println("index");
        return "index";
    }

    /**
     * 预支付
     * @return
     * @throws IOException
     */
    @RequestMapping("/readyPay")
    public ResponseData ReadyPay(@RequestBody PreOrderPayVo preOrderPayVo) {
        return baofooPayService.quickReadyPayBf(preOrderPayVo);
    }



    /**
     * 确认支付
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public String ActionPay(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String business_no = request.getParameter("business_no");//预绑卡唯一码
//        String sms_code = request.getParameter("sms_code");// 短信验证码
//
//        Map<String, String> HeadPostParam =(Map<String, String>) HeadMap.GetPostHead();
//
//        Map<String, Object> DataContent = new HashMap<String, Object>();
//
//        DataContent.put("terminal_id", HeadPostParam.get("terminal_id"));
//        DataContent.put("member_id", HeadPostParam.get("member_id"));
//        DataContent.put("sms_code", sms_code);
//        DataContent.put("business_no", business_no);
//        DataContent.put("trans_serial_no", "TSN"+System.currentTimeMillis());
//        DataContent.put("additional_info", "附加字段");
//        DataContent.put("req_reserved", "保留字段");
//
//        String PostString = HeadMap.Send(HeadPostParam, DataContent, "https://vgw.baofoo.com/quickpay/api/confirmorder");
//
//        if(null == PostString || PostString.isEmpty()){ return "返回异常，检查网络通讯！"; }
//
//        Map<?,?>ParseMap = ParseStr.ToMap(PostString);
//
//        if(!ParseMap.containsKey("ret_code")){
//            return "返回参数异常！XML解析参数[ret_code]不存在";
//        }
//
//        if(!ParseMap.get("ret_code").toString().equals("0000")){//如果基础参数错误，如商户号或终端号错，则无需解析参数date_content
//            return ParseMap.get("ret_code")+","+ParseMap.get("ret_msg");
//        }
//
//        String  SplitDataContent = ParseMap.get("data_content").toString();//取出date_content
//        Log.Write("=====返回密文======"+SplitDataContent);
//
//
//        String  cerpath = BaofooConfig.getConstants().get("cer.name");//宝付公钥
//        String data_content = RsaCodingUtil.decryptByPubCerFile(SplitDataContent,cerpath);
//        if(data_content.isEmpty()){//判断解密是否正确。如果为空则宝付公钥不正确
//            Log.Write("=====检查解密公钥是否正确！");
//            return "检查解密公钥是否正确！";
//        }
//        data_content = SecurityUtil.Base64Decode(data_content);
//        Log.Write("=====返回数据解密结果:"+data_content);
//        if(HeadPostParam.get("data_type").equals("xml")){
//            data_content = JXMConvertUtil.XmlConvertJson(data_content);
//            Log.Write("=====返回结果转JSON:"+data_content);
//        }
//        Map<String,Object> ArrayDataString = JXMConvertUtil.JsonConvertHashMap(data_content);//将JSON转化为Map对象。
//        if(!ArrayDataString.containsKey("resp_code")){
//            return "返回参数异常！XML解析参数[resp_code]不存在";
//        }
//
//        String retCode = ArrayDataString.get("resp_code").toString();
//
//        if(retCode.equals("0000")){
//            return "订单状态："+ArrayDataString.get("resp_code")+",应答消息："+ArrayDataString.get("resp_msg")+",成功金额："+ArrayDataString.get("succ_amt")+"（分）";
//        }
//        return "订单状态："+ArrayDataString.get("resp_code")+",应答消息："+ArrayDataString.get("resp_msg");
        return null;
    }

    /**
     * 绑定关系查询
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public String QuickPayBindQuery(HttpServletRequest request,HttpServletResponse response) throws IOException {

//        String user_id = request.getParameter("user_id");//平台用户ID
//
//        Map<String, String> HeadPostParam =(Map<String, String>) HeadMap.GetPostHead();
//        Map<String, Object> DataContent = new HashMap<String, Object>();
//
//        DataContent.put("terminal_id", HeadPostParam.get("terminal_id"));
//        DataContent.put("member_id", HeadPostParam.get("member_id"));
//        DataContent.put("user_id", user_id);
//        DataContent.put("trans_serial_no", "TSN"+System.currentTimeMillis());
//        DataContent.put("additional_info", "附加字段");
//        DataContent.put("req_reserved", "保留字段");
//
//        String PostString = HeadMap.Send(HeadPostParam, DataContent, "https://vgw.baofoo.com/quickpay/api/querybind");
//
//
//        if(null == PostString ||PostString.isEmpty()){
//            return "返回异常，检查网络通讯！";
//        }
//
//        Map<?,?>ParseMap = ParseStr.ToMap(PostString);
//
//        if(!ParseMap.containsKey("ret_code")){
//            return "返回参数异常！XML解析参数[ret_code]不存在";
//        }
//
//        if(!ParseMap.get("ret_code").toString().equals("0000")){//如果基础参数错误，如商户号或终端号错，则无需解析参数date_content
//            return ParseMap.get("ret_code")+","+ParseMap.get("ret_msg");
//        }
//
//        String  SplitDataContent = ParseMap.get("data_content").toString();//取出date_content
//
//        Log.Write("=====返回密文======"+SplitDataContent);
//        String  cerpath = BaofooConfig.getConstants().get("cer.name");//宝付公钥
//        String data_content = RsaCodingUtil.decryptByPubCerFile(SplitDataContent,cerpath);
//        if(data_content.isEmpty()){//判断解密是否正确。如果为空则宝付公钥不正确
//            Log.Write("=====检查解密公钥是否正确！");
//            return "检查解密公钥是否正确！";
//        }
//        data_content = SecurityUtil.Base64Decode(data_content);
//        Log.Write("=====返回数据解密结果:"+data_content);
//        if(HeadPostParam.get("data_type").equals("xml")){
//            data_content = JXMConvertUtil.XmlConvertJson(data_content);
//            Log.Write("=====返回结果转JSON:"+data_content);
//        }
//
//        Map<String,Object> ArrayDataString = JXMConvertUtil.JsonConvertHashMap(data_content);//将JSON转化为Map对象。
//
//        if(!ArrayDataString.containsKey("resp_code")){
//            return "返回参数异常！XML解析参数[resp_code]不存在";
//        }
//
//        String retCode = ArrayDataString.get("resp_code").toString();
//
//        if(retCode.equals("0000")){
//
//            String BindStr = "";
//            if(!ArrayDataString.containsKey("card_list")){
//                BindStr = ArrayDataString.get("bind_id").toString();
//            }else{
//                Log.Write("类型："+ArrayDataString.get("card_list").getClass().getName());
//                List<Map<String,Object>> CardList = (List<Map<String,Object>>) ArrayDataString.get("card_list");
//                BindStr ="请遍历CardList";
//            }
//
//            return "订单状态："+ArrayDataString.get("resp_code")+",应答消息："
//                    +ArrayDataString.get("resp_msg")+",用户bind_id："
//                    +BindStr;
//        }
//
//        return "订单状态："+ArrayDataString.get("resp_code")+",应答消息："+ArrayDataString.get("resp_msg");
        return "";
    }




}
