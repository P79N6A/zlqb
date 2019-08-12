package com.creativearts.nyd.web.controller.baofoo;

import com.creativearts.nyd.pay.service.baofoo.config.BaofooConfig;
import com.creativearts.nyd.pay.service.baofoo.rsa.RsaCodingUtil;
import com.creativearts.nyd.pay.service.baofoo.util.HttpUtil;
import com.creativearts.nyd.pay.service.baofoo.util.JXMConvertUtil;
import com.creativearts.nyd.pay.service.baofoo.util.MapToXMLString;
import com.creativearts.nyd.pay.service.baofoo.util.SecurityUtil;
import com.creativearts.nyd.web.controller.BaseController;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/12/5
 **/
@RestController
@RequestMapping("/nyd/pay/bfdk")
public class BaofooWithholdController extends BaseController{

    @Autowired
    private BaofooConfig baofooConfig;
    //请求方法
    @RequestMapping("/dk")
    public String Api() throws IOException {
//        String txn_sub_type = request.getParameter("txn_sub_type");
//        log("参数："+txn_sub_type);
//        if (txn_sub_type.isEmpty())
//        {
//            return "参数【txn_sub_type】不能为空";
//        }

        String pay_code = "ICBC";//银行卡编码
        String acc_no = "6222020111122220000";//银行卡卡号
        String id_card = "130102198901016354";//身份证号码
        String id_holder = "张三";//姓名
        String mobile = "15618624753";//银行预留手机号
        String trans_id =  "TID"+System.currentTimeMillis();	//商户订单号
//        String valid_date = request.getParameter("valid_date"); //信用卡有效期
//        String valid_no = request.getParameter("valid_no");//信用卡安全码

//        String CardAction = request.getParameter("CardAction");
//        if (trans_id == null)
//        {
//            trans_id = "TID"+System.currentTimeMillis();
//        }

        Map<String,String> HeadPostParam = new HashMap<String,String>();

        HeadPostParam.put("version", "4.0.1.0");
        HeadPostParam.put("member_id", baofooConfig.getMemberId());
        HeadPostParam.put("terminal_id", baofooConfig.getTerminalId());
        HeadPostParam.put("txn_type", "0431");
        HeadPostParam.put("txn_sub_type", "13");
        HeadPostParam.put("data_type", baofooConfig.getDataType());
        String biz_type = "0000";
        String request_url = "http://tgw.baofoo.com/cutpayment/api/backTransRequest";//测试地址
        String  pfxpath = baofooConfig.getPfxName();//商户私钥
        String  cerpath = baofooConfig.getCerName();//宝付公钥
//        if("2".equals(CardAction)){
//            biz_type = "0102";
//        }

        /**
         * 报文体
         * =============================================
         *
         */
        String trade_date=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易日期
        Map<String,String> XMLArray = new HashMap<String,String>();

        XMLArray.put("txn_sub_type", HeadPostParam.get("txn_sub_type"));
        XMLArray.put("biz_type", biz_type);
        XMLArray.put("terminal_id", HeadPostParam.get("terminal_id"));
        XMLArray.put("member_id", HeadPostParam.get("member_id"));
        XMLArray.put("trans_serial_no", "TISN"+System.currentTimeMillis());
        XMLArray.put("trade_date", trade_date);
        XMLArray.put("additional_info", "附加信息");
        XMLArray.put("req_reserved", "保留");

//        if(txn_sub_type.equals("13")){
            String  txn_amt = String.valueOf(new BigDecimal("60").multiply(BigDecimal.valueOf(100)).setScale(0));//支付金额转换成分
            XMLArray.put("pay_code", pay_code);
            XMLArray.put("pay_cm", "2");
            XMLArray.put("id_card_type", "01");
            XMLArray.put("acc_no", acc_no);
            XMLArray.put("id_card", id_card);
            XMLArray.put("id_holder", id_holder);
            XMLArray.put("mobile", mobile);
//            XMLArray.put("valid_date", valid_date);
//            XMLArray.put("valid_no", valid_no);
            XMLArray.put("trans_id", trans_id);
            XMLArray.put("txn_amt", txn_amt);
//        }else if(txn_sub_type.equals("06")){
//            log("======【 订单查询接口】=======");
//            String orig_trans_id = request.getParameter("orig_trans_id");//订单号
//            XMLArray.put("orig_trans_id", orig_trans_id);
//        }else{
//
//            return "【txn_sub_type】参数错误！";
//        }

        Map<Object,Object> ArrayToObj = new HashMap<Object,Object>();

        String XmlOrJson = "";
        if(HeadPostParam.get("data_type").equals("xml")){
            ArrayToObj.putAll(XMLArray);
            XmlOrJson = MapToXMLString.converter(ArrayToObj,"data_content");
        }else{
            JSONObject jsonObjectFromMap = JSONObject.fromObject(XMLArray);
            XmlOrJson = jsonObjectFromMap.toString();
        }
        log("请求参数："+XmlOrJson);


        String base64str = SecurityUtil.Base64Encode(XmlOrJson);
        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str,pfxpath,baofooConfig.getPfxPwd());

        HeadPostParam.put("data_content", data_content);

        String PostString  = HttpUtil.RequestForm(request_url, HeadPostParam);
        log("请求返回："+ PostString);

        PostString = RsaCodingUtil.decryptByPubCerFile(PostString,cerpath);

        if(PostString.isEmpty()){//判断解密是否正确。如果为空则宝付公钥不正确
            log("=====检查解密公钥是否正确！");
            return "检查解密公钥是否正确！";
        }

        PostString = SecurityUtil.Base64Decode(PostString);
        log("=====返回查询数据解密结果:"+PostString);

        if(HeadPostParam.get("data_type").equals("xml")){
            PostString = JXMConvertUtil.XmlConvertJson(PostString);
            log("=====返回结果转JSON:"+PostString);
        }

        Map<String,Object> ArrayDataString = JXMConvertUtil.JsonConvertHashMap(PostString);//将JSON转化为Map对象。
        log("转换为MAP对象："+ArrayDataString);

        String ReturnStr = "";

        if(ArrayDataString.get("resp_code").toString().equals("0000")){

            ReturnStr = "返回码:"+ArrayDataString.get("resp_code")+", 返回消息:"+ArrayDataString.get("resp_msg");

//            if (txn_sub_type.equals("01") || txn_sub_type.equals("03"))
//            {
//                ReturnStr += ", 绑定编码:"+ArrayDataString.get("bind_id");
//            }
//            else if (txn_sub_type.equals("04") || txn_sub_type.equals("06"))
//            {
//                ReturnStr +=", 成功金额：" + ArrayDataString.get("succ_amt") + "(分)" + ", 商户订单号：" + ArrayDataString.get("trans_id");
//            }
        }
        else {

            ReturnStr = "返回码：" + ArrayDataString.get("resp_code") + ", 返回消息：" + ArrayDataString.get("resp_msg");
        }


        return ReturnStr;
    }


    private void log(String msg) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\t: " + msg);
    }


    /**
     * 代扣交易接口
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
//    public String ApiPay(HttpServletRequest request, HttpServletResponse response) throws Exception{
//        String pay_code = request.getParameter("pay_code");//银行卡编码
//        String acc_no = request.getParameter("acc_no");//银行卡卡号
//        String id_card = request.getParameter("id_card");//身份证号码
//        String id_holder = request.getParameter("id_holder");//姓名
//        String mobile = request.getParameter("mobile");//银行预留手机号
//
//        Map<String,String> HeadPostParam = FormUtil.getParams();//明文参数
//        HeadPostParam.put("txn_sub_type", "13"); //交易子类（代扣）
//
//        String  pfxpath = BaofooAction.getWebRoot()+"CER\\"+BaofooAction.getConstants().get("pfx.name");//商户私钥
//        String  cerpath = BaofooAction.getWebRoot()+"CER\\"+BaofooAction.getConstants().get("cer.name");//宝付公钥
//
//        File pfxfile=new File(pfxpath);
//        if(!pfxfile.exists()){
//            Log.Write("=====私钥文件不存在！======");
//            return "私钥文件不存在！";
//        }
//
//        File cerfile=new File(cerpath);
//        if(!cerfile.exists()){//判断宝付公钥是否为空
//            Log.Write("=====公钥文件不存在！======");
//            return "公钥文件不存在！";
//        }
//
//        String trade_date=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易日期
//        Map<String,String> XMLArray = new HashMap<String,String>();
//        XMLArray.put("txn_sub_type", HeadPostParam.get("txn_sub_type"));
//        XMLArray.put("biz_type", "0000");
//        XMLArray.put("terminal_id", HeadPostParam.get("terminal_id"));
//        XMLArray.put("member_id", HeadPostParam.get("member_id"));
//        String  txn_amt = String.valueOf(new BigDecimal(request.getParameter("txn_amt")).multiply(BigDecimal.valueOf(100)).setScale(0));//支付金额转换成分
//        XMLArray.put("pay_code", pay_code);
//        XMLArray.put("pay_cm", "2");//传默认值
//        XMLArray.put("id_card_type", "01");//身份证传固定值。
//        XMLArray.put("acc_no", acc_no);
//        XMLArray.put("id_card", id_card);
//        XMLArray.put("id_holder", id_holder);
//        XMLArray.put("mobile", mobile);
//        XMLArray.put("valid_date", "");//信用卡有效期
//        XMLArray.put("valid_no", "");//信用卡安全码
//        XMLArray.put("trans_id", "TID"+System.currentTimeMillis());//商户订单号
//        XMLArray.put("txn_amt", txn_amt);
//        XMLArray.put("trans_serial_no", "TISN"+System.currentTimeMillis());
//        XMLArray.put("trade_date", trade_date);
//        XMLArray.put("additional_info", "附加信息");
//        XMLArray.put("req_reserved", "保留");
//
//
//        String XmlOrJson = "";
//        if(HeadPostParam.get("data_type").equals("xml")){
//            Map<Object,Object> ArrayToObj = new HashMap<Object,Object>();
//            ArrayToObj.putAll(XMLArray);
//            XmlOrJson = MapToXml.Coverter(ArrayToObj, "data_content");
//        }else{
//            JSONObject jsonObjectFromMap = JSONObject.parseObject(JSON.toJSONString(XMLArray));
//            XmlOrJson = jsonObjectFromMap.toString();
//        }
//        Log.Write("请求参数："+XmlOrJson);
//
//        String base64str = SecurityUtil.Base64Encode(XmlOrJson);
//        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str,pfxpath,BaofooAction.getConstants().get("pfx.pwd"));
//        HeadPostParam.put("data_content", data_content);//加入请求密文
//
//        String PostString  = HttpUtil.RequestForm(BaofooAction.getConstants().get("post.url"), HeadPostParam);
//        Log.Write("请求返回："+ PostString);
//        PostString = RsaCodingUtil.decryptByPubCerFile(PostString,cerpath);
//        if(PostString.isEmpty()){//判断解密是否正确。如果为空则宝付公钥不正确
//            Log.Write("=====检查解密公钥是否正确！");
//            return "检查解密公钥是否正确！";
//        }
//        PostString = SecurityUtil.Base64Decode(PostString);
//        Log.Write("=====返回查询数据解密结果:"+PostString);
//        if(HeadPostParam.get("data_type").equals("xml")){
//            PostString = JXMConvertUtil.XmlConvertJson(PostString);
//            Log.Write("=====返回结果转JSON:"+PostString);
//        }
//
//        Map<String,Object> ArrayDataString = JXMConvertUtil.JsonConvertHashMap(PostString);//将JSON转化为Map对象。
//        Log.Write("转换为MAP对象："+ArrayDataString);
//        String ReturnStr="返回码：" + ArrayDataString.get("resp_code") + ", 返回消息：" + ArrayDataString.get("resp_msg");
//        if(ArrayDataString.get("resp_code").toString().equals("0000")){
//            ReturnStr +=", 成功金额：" + ArrayDataString.get("succ_amt") + "分" + ", 商户订单号：" + ArrayDataString.get("trans_id");
//        }
//        return ReturnStr;
//    }
//
//    /**
//     * 查询接口 31
//     * @param request
//     * @param response
//     * @return
//     * @throws Exception
//     */
//    public String QueryOrderId(HttpServletRequest request,HttpServletResponse response) throws Exception{
//
//        Log.Write("=======查询订单接口31=========");
//        String orig_trans_id = request.getParameter("orig_trans_id");//订单号
//
//        Map<String,String> HeadPostParam = FormUtil.getParams();//明文参数
//        HeadPostParam.put("txn_sub_type", "31");
//        String orig_trade_date=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//日期
//        Map<String,Object> XMLArray = new HashMap<String,Object>();
//        XMLArray.put("txn_sub_type", HeadPostParam.get("txn_sub_type"));
//        XMLArray.put("biz_type", "0000");
//        XMLArray.put("terminal_id", HeadPostParam.get("terminal_id"));
//        XMLArray.put("member_id", HeadPostParam.get("member_id"));
//        XMLArray.put("orig_trans_id",orig_trans_id);
//        XMLArray.put("trans_serial_no", "TISN"+System.currentTimeMillis());
//        XMLArray.put("orig_trade_date", orig_trade_date);
//        XMLArray.put("additional_info", "附加信息");
//        XMLArray.put("req_reserved", "保留");
//
//        String  pfxpath = BaofooConfig.getConstants().get("pfx.name");//商户私钥
//        String  cerpath = BaofooConfig.getConstants().get("cer.name");//宝付公钥
//
//        File pfxfile=new File(pfxpath);
//        if(!pfxfile.exists()){
//            Log.Write("=====私钥文件不存在！======");
//            return "私钥文件不存在！";
//        }
//
//        File cerfile=new File(cerpath);
//        if(!cerfile.exists()){//判断宝付公钥是否为空
//            Log.Write("=====公钥文件不存在！======");
//            return "公钥文件不存在！";
//        }
//
//        String XmlOrJson = "";
//        if(HeadPostParam.get("data_type").equals("xml")){
//            Map<Object,Object> ArrayToObj = new HashMap<Object,Object>();
//            ArrayToObj.putAll(XMLArray);
//            XmlOrJson = MapToXml.Coverter(ArrayToObj, "data_content");
//        }else{
//            JSONObject jsonObjectFromMap = JSONObject.parseObject(JSON.toJSONString(XMLArray));
//            XmlOrJson = jsonObjectFromMap.toString();
//        }
//        Log.Write("请求参数："+XmlOrJson);
//
//        String base64str = SecurityUtil.Base64Encode(XmlOrJson);
//        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str,pfxpath,BaofooConfig.getConstants().get("pfx.pwd"));
//        HeadPostParam.put("data_content", data_content);//加入请求密文
//
//        String PostString  = HttpUtil.RequestForm(BaofooConfig.getConstants().get("post.url"), HeadPostParam);
//        Log.Write("请求返回："+ PostString);
//        PostString = RsaCodingUtil.decryptByPubCerFile(PostString,cerpath);
//        if(PostString.isEmpty()){//判断解密是否正确。如果为空则宝付公钥不正确
//            Log.Write("=====检查解密公钥是否正确！");
//            return "检查解密公钥是否正确！";
//        }
//        PostString = SecurityUtil.Base64Decode(PostString);
//        Log.Write("=====返回查询数据解密结果:"+PostString);
//        if(HeadPostParam.get("data_type").equals("xml")){
//            PostString = JXMConvertUtil.XmlConvertJson(PostString);
//            Log.Write("=====返回结果转JSON:"+PostString);
//        }
//
//        Map<String,Object> ArrayDataString = JXMConvertUtil.JsonConvertHashMap(PostString);//将JSON转化为Map对象。
//        Log.Write("转换为MAP对象："+ArrayDataString);
//        String ReturnStr="返回码：" + ArrayDataString.get("resp_code") + ", 返回消息：" + ArrayDataString.get("resp_msg");
//        if(ArrayDataString.get("resp_code").toString().equals("0000")){
//            ReturnStr +=", 成功金额：" + ArrayDataString.get("succ_amt") + "分";
//        }
//        return ReturnStr;
//
//    }
//
//    /**
//     * 异步通知
//     * @param request
//     * @param response
//     * @return
//     * @throws IOException
//     */
//    public String ReturnUrl(HttpServletRequest request,HttpServletResponse response)throws IOException {
//        String data_content = request.getParameter("data_content");//回调参数
//        if(data_content.isEmpty()){//判断参数是否为空
//            Log.Write("=====返回数据为空");
//            return "返回数据为空";
//        }
//        Log.Write("=====返回数据:"+data_content);
//        String  cerpath = BaofooAction.getWebRoot()+"CER\\"+BaofooAction.getConstants().get("cer.name");//宝付公钥
//        String data_type=BaofooAction.getConstants().get("data.type"); //加密报文的数据类型（xml/json）
//
//        File cerfile=new File(cerpath);
//        if(!cerfile.exists()){//判断宝付公钥是否为空
//            System.out.println("宝付公钥文件不存在！");
//            Log.Write("=====公钥文件不存在！======");
//            return "公钥文件不存在！";
//        }
//
//        data_content = RsaCodingUtil.decryptByPubCerFile(data_content,cerpath);
//        if(data_content.isEmpty()){//判断解密是否正确。如果为空则宝付公钥不正确
//            Log.Write("=====检查解密公钥是否正确！");
//            return "检查解密公钥是否正确！";
//        }
//        data_content = SecurityUtil.Base64Decode(data_content);
//        Log.Write("=====返回数据解密结果:"+data_content);
//
//        if(data_type.equals("xml")){
//            data_content = JXMConvertUtil.XmlConvertJson(data_content);
//            Log.Write("=====返回结果转JSON:"+data_content);
//        }
//
//        Map<String,Object> ArrayData = JXMConvertUtil.JsonConvertHashMap(data_content);//将JSON转化为Map对象。
//
//        if(!ArrayData.containsKey("resp_code")){
//            return "返回参数异常！XML解析参数[resp_code]不存在";
//        }
//
//        String resp_code = ArrayData.get("resp_code").toString();
//
//        //这里根据ArrayData 对象里的值去写本地服务器端数据
//        //商户自行写入自已的数据。。。。。。。。
//
//        return "OK";//处理完成在页面输出OK（必须）
//
//    }
}
