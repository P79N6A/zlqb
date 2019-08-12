package com.nyd.user.service.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Dengw on 2017/11/16
 */
@Component
@Getter
public class UserProperties {
    //md5加密前缀
    @Value("${md5.key}")
    private String md5Key;

    //登录超时
    @Value("${login.timeout}")
    private  String loginTimeout;

    //资料完整度阈值
    @Value("${min.score}")
    private  String minScore;

    //是否开启jo三要素验证
    @Value("${joVerifyFalg}")
    private  String joVerifyFalg;

    //是否开启银行卡四要素验证
    @Value("${bankVerifyFlag}")
    private  String bankVerifyFlag;

    //信用认证有效期
    @Value("${auth.effective.time}")
    private  String authEffectiveTime;

    //评估有效期
    @Value("${assess.effective.time}")
    private  String assessEffectiveTime;
    //验证码图形验证url
    @Value("${luosimao.verify.url}")
    private String luosimaoVerifyUrl;

    //图形验证apikey
    @Value("${luosimao.api.key}")
    private String luosimaoKey;

    //图形验证开关
    @Value("${luosimao.verify.switch}")
    private String luosimaoVerifySwitch;
    
    //给你花引流公钥
    @Value("${gnh.pubkey}")
    private String pubkeyGnh;
    
    //绑卡请求地址
    @Value("${user.bind.card}")
    private String userBindCardUri;
    //绑卡请求地址
    @Value("${user.bind.card.confirm}")
    private String userBindCardConfirmUri;
    //绑卡请求地址
    @Value("${user.bind.card.query}")
    private String userBindCardAQueryUri;
    
    //宝街联合注册公钥
    @Value("${baojie.uniteregister.publickey}")
    private String bJUniteRegisterPublicKey;
    
    @Value("${shanft.uniteregister.publickey}")
    private String shanFTUniteRegisterPublicKey;
    
    //查询银行卡列表请求地址
    @Value("${user.card.list.query}")
    private String userCardListUri;
    
   //公共支付服务ip
    @Value("${common.pay.ip}")
    private String commonPayIp;

    //公共服务port
    @Value("${common.pay.port}")
    private String commonPayPort;
    
    //提交退款id
    @Value("${thirdparty.admin.fmis.ip}")
    private String commonRefundSubmitIp;
    
    //提交退款port
    @Value("${thirdparty.admin.fmis.port}")
    private String commonRefundSubmitPort;
    
    //查询代扣记录
    @Value("${user.query.all.withhold.order}")
    private String queryAllWithholdOrder;

    //查询代扣记录
    @Value("${user.query.feature.rinse.userReRun}")
    private String queryFeatureRinseUserReRun;
    
}
