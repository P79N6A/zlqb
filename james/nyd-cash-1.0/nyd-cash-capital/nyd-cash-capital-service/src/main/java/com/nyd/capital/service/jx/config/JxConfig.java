package com.nyd.capital.service.jx.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author cm
 */
@Configuration
@Data
public class JxConfig {

    @Value("${jx.appId}")
    private String appId;

    @Value("${aes.Key}")
    private String aesKey;

    @Value("${jx.commonUrl}")
    private String commonUrl;

    /**公用请求参数*/
    //版本
    @Value("${jx.version}")
    private String version;
    
    //接口名称:推单查询
    @Value("${jx.membersStatus}")
    private String membersStatus;
    //接口名称:推单外审
    @Value("${jx.preloan}")
    private String preloan;
    //接口名称:推单外审确认
    @Value("${jx.preloanConfirm}")
    private String preloanConfirm;
    //接口名称:推单外审结果查询
    @Value("${jx.preloanQuery}")
    private String preloanQuery;

    //接口名称:即信开户五合一
    @Value("${jx.openHtml}")
    private String openHtml;

    //接口名称:获取验证码
    @Value("${jx.getCheckCode}")
    private String getCheckCode;

    //接口名称:提现
    @Value("${jx.withDraw}")
    private String withDraw;

    //接口名称:放款查询
    @Value("${jx.loanQuery}")
    private String loanQuery;

    //接口名称:还款计划查询
    @Value("${jx.loanPhases}")
    private String loanPhases;

    //接口名称:还款
    @Value("${jx.repayments}")
    private String repayments;

    //机构号
    @Value("${jx.instCode}")
    private String instCode;
    
    //渠道
    @Value("${jx.channel}")
    private String channel;
    
    //请求方保留
    @Value("${jx.acqRes}")
    private String acqRes;

    //开户回调地址
    @Value("${jx.callBack}")
    private String callBack;

    //提现回调url
    @Value("${jx.withDrawCallBack}")
    private String withDrawCallBack;

    //忘记密码url
    @Value("${jx.forgotPasswordUrl}")
    private String forgotPasswordUrl;


    /**
     * 爬虫插件目录位置
     **/
    @Value("${jx.driverLocation}")
    private String driverLocation;

    /**
     * 请求头1
     **/
    @Value("${jx.requestHeaderName1}")
    private String requestHeaderName1;

    /**
     * 请求头2
     **/
    @Value("${jx.requestHeaderName2}")
    private String requestHeaderName2;

    /**
     * 请求头信息1
     **/
    @Value("${jx.requestHeaderMsg1}")
    private String requestHeaderMsg1;

    /**
     * 请求头信息2
     **/
    @Value("${jx.requestHeaderMsg2}")
    private String requestHeaderMsg2;

    /**
     * 盐
     **/
    @Value("${jx.password}")
    private String password;
    
    /**
     * 签名私钥
     */
    @Value("${jx.privateKey}")
    private String privateKey;
    
    /**
     * 私钥密码
     */
    @Value("${jx.privatePass}")
    private String privatePass;
    
    /**
     * 验签公钥
     */
    @Value("${jx.publicKey}")
    private String publicKey;
}
