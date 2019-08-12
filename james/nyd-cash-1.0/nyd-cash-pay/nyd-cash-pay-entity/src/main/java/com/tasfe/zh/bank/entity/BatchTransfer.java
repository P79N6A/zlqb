package com.tasfe.zh.bank.entity;

import lombok.Data;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Lait on 2017/8/8.
 */
@Data
public class BatchTransfer {

    private Long id;
    /**
     * 接口名称:  <input type="text" name="service" value="create_batch_transfer_to_card">(例如：create_batch_transfer_to_card)
     */
    private String service;


    /**
     * 接口版本:  <input type="text" name="version" value="1.0">(目前版本：1.0)
     */
    private String version;

    /**
     * 合作者身份ID:  <input type="text" name="partner_id" value="200000030006">
     */
    private String partner_id;

    /**
     * 字符集:  <input type="text" name="_input_charset" value="UTF-8">(类型有：UTF-8;GBK;GB2312)<br/>
     */
    private String _input_charset;

    /**
     * 签名: <input type="text" name="sign" value="">(签约合作方的钱包唯一用户号 不可空)<br/>
     */
    private String sign;
    /**
     * 签名方式: <input type="text" name="sign_type" value="ITRUSSRV">(签名方式只支持RSA、MD5、ITRUSSRV 不可空)<br/>
     */
    private String sign_type;

    /**
     * 返回页面路径: <input type="text" name="return_url" value="http://122.224.203.210:18380/instant-trade-demo/batchTransferToCardReturnUrlResponse.jsp">(页面跳转同步返回页面路径  可空)<br/>
     */
    private String return_url;

    /**
     * 备注: <input type="text" name="memo" value=""><br/>
     */
    private String memo;







    /**
     * 商户网站请求号: <input type="text" name="batch_no" value="<%=batchNo%>">(不可空)<br/>
     */
    private String batch_no;

    /**
     * 转账笔数: <input type="text" name="transfer_num" value="1">(不可空)<br/>
     */
    private String transfer_num;
    /**
     * 转账总金额: <input type="text" name="transfer_amount" value="0.01">(不可空)<br/>
     */
    private String transfer_amount;





    /**
     * 转账列表: <input type="text" name="transfer_list" value="<%=batchNo%>1~张三~123456~ICBC~工商银行~支行名称~~上海~上海市~C~0.01~~">(不可空)<br/>
     */
    private String transfer_list;

    /**
     * 操作员Id: <input type="text" name="operator_id" value="123456789">(不可空)<br/>
     */
    private String operator_id;

    /**
     * 会员标识号: <input type="text" name="identity_no" value="payment001@qq.com">(不可空)<br/>
     */
    private String identity_no;

    /**
     * 标识类型: <input type="text" name="identity_type" value="1">(可空)<br/>
     */
    private String identity_type;

    /**
     * 服务器异步通知页面路径 :  <input type="text" name="notify_url" value="http://122.224.203.210:18380/TestDemo/create_batch_transfer_to_card/batchTransferToCardNotifyUrlResponse.jsp ">(可空)<br/>
     */
    private String notify_url;

}
