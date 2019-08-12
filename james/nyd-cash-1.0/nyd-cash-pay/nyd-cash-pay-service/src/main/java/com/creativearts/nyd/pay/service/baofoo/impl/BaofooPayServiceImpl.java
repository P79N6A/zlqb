package com.creativearts.nyd.pay.service.baofoo.impl;

import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.baofoo.PreOrderPayResponse;
import com.creativearts.nyd.pay.model.baofoo.PreOrderPayVo;
import com.creativearts.nyd.pay.service.baofoo.BaofooPayService;
import com.creativearts.nyd.pay.service.baofoo.config.BaofooConfig;
import com.creativearts.nyd.pay.service.baofoo.util.ParseStr;
import com.tasfe.framework.support.model.ResponseData;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/12/15
 **/
@Service
public class BaofooPayServiceImpl implements BaofooPayService{

    Logger logger = LoggerFactory.getLogger(BaofooPayServiceImpl.class);

    @Autowired
    private BaofooConfig baofooConfig;
    @Override
    public ResponseData quickReadyPayBf(PreOrderPayVo preOrderPayVo) {
        String user_id = preOrderPayVo.getUserId();//平台用户ID

        BigDecimal txn_amt_num = new BigDecimal(preOrderPayVo.getAmount()).multiply(BigDecimal.valueOf(100));//金额转换成分
        String  txn_amt = String.valueOf(txn_amt_num.setScale(0));//支付金额

        Map<String, String> headPostParam = baofooConfig.getPostHeadQuick();

        Map<String, Object> DataContent = new HashMap<String, Object>();
        String trade_date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());// 交易日期

        DataContent.put("terminal_id", headPostParam.get("terminal_id"));
        DataContent.put("member_id", headPostParam.get("member_id"));
        DataContent.put("user_id", user_id);
        DataContent.put("trans_id", preOrderPayVo.getBillNo());
        DataContent.put("txn_amt", txn_amt);
        DataContent.put("trade_date", trade_date);

//		DataContent.put("share_info", "01");
//		DataContent.put("notify_url", id_card);1 txt_sub_type=02时不能为空；2单位(分) 格式



        DataContent.put("trans_serial_no", "bftsn"+System.currentTimeMillis());
        DataContent.put("additional_info", "附加字段");
        DataContent.put("req_reserved", "保留字段");

        //用户信息
//        DataContent.put("acc_no","6222020111122220000");
//        DataContent.put("card_holder","张三");
//        DataContent.put("id_card_type","01");
//        DataContent.put("id_card","130102198901016354");
//        DataContent.put("mobile","15618624753");
        DataContent.put("acc_no",preOrderPayVo.getAccNo());
        DataContent.put("card_holder",preOrderPayVo.getCardName());
        DataContent.put("id_card_type","01");
        DataContent.put("id_card",preOrderPayVo.getIdCard());
        DataContent.put("mobile",preOrderPayVo.getMobile());

//        String[] Pairs = time_meberid_transno.split("_");

        Map<String,String> RiskItem = new HashMap<String,String>();
        //-------风控基础参数-------------
        RiskItem.put("goodsCategory", "02");//商品类目 详见附录《商品类目》
        RiskItem.put("userLoginId", "bofootest");//用户在商户系统中的登陆名（手机号、邮箱等标识）
        RiskItem.put("userEmail", "");
        RiskItem.put("userMobile", "15821798636");//用户手机号
        RiskItem.put("registerUserName", "大圣");//用户在商户系统中注册使用的名字
        RiskItem.put("identifyState", "1");//用户在平台是否已实名，1：是 ；0：不是
        RiskItem.put("userIdNo", "341182197807131732");//用户身份证号
        RiskItem.put("registerTime", "20170223113233");//格式为：YYYYMMDDHHMMSS
        RiskItem.put("registerIp", "10.0.0.0");//用户在商户端注册时留存的IP
        RiskItem.put("chName", "10.0.0.0");//持卡人姓名
        RiskItem.put("chIdNo", "");//持卡人身份证号
        RiskItem.put("chCardNo", "");//持卡人银行卡号
        RiskItem.put("chMobile", "");//持卡人手机
        RiskItem.put("chPayIp", "");//持卡人支付IP
//        RiskItem.put("deviceOrderNo", Pairs[2]);//加载设备指纹中的订单号


//
//        RiskItem.put("gameName", "15821798636");//充值游戏名称
//        RiskItem.put("userAcctId", "15821798636");//游戏账户ID
//        RiskItem.put("rechargeType", "0");//充值类型 (0:为本账户充值或支付、1:为他人账户充值或支付； 默认为 0)
//        RiskItem.put("gameProdType", "02");//01：点券类 、 02：金币类 、 03：装备道具类 、 04：其他
//        RiskItem.put("gameAcctId", "");//被充值游戏账户ID,若充值类型为1 则填写
//        RiskItem.put("gameLoginTime", "20");//游戏登录次数，累计最近一个月
//        RiskItem.put("gameOnlineTime", "100");//游戏在线时长，累计最近一个月


        DataContent.put("risk_item", JSONObject.fromObject(RiskItem).toString());//放入风控参数


        String PostString = null;
        try {
            PostString = baofooConfig.Send(headPostParam, DataContent,baofooConfig.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("调用send异常",e);
        }
//        System.out.println(PostString);
        if(null == PostString || PostString.isEmpty()){
            return ResponseData.error("返回异常，检查网络通讯！");
        }
//
        Map<String,String> ParseMap = (Map<String,String>) ParseStr.ToMap(PostString);

        if(!ParseMap.containsKey("ret_code")){
            return ResponseData.error("解析参数[ret_code]不存在");
        }

        if(!ParseMap.get("ret_code").toString().equals("0000")){

            return ResponseData.error("ret_code:"+ParseMap.get("ret_code")+"***"+ParseMap.get("ret_msg"));
        }

        String  SplitDataContent = ParseMap.get("data_content").toString();//取出date_content
        String result=null;
        try {
            result = baofooConfig.process(SplitDataContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(result == null){
            return ResponseData.error("error");
        }else {
            return ResponseData.success(JSON.parseObject(result, PreOrderPayResponse.class));
        }
    }

    @Override
    public ResponseData quickPayBf() {
        return null;
    }
}
