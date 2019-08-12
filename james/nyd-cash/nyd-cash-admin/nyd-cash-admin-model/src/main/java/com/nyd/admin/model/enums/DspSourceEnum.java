package com.nyd.admin.model.enums;


import com.nyd.admin.model.EnumModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: hwei

 */
public enum DspSourceEnum {
    PyFraud("py_antiFraud","鹏元反欺诈黑名单"),
    SZRBlack1("szr_blacklistx","神州融黑名单1"),
    SZRBlack2("szr_sybadinfo","神州融黑名单2"),
    SZRBlack4("szr_breport","神州融黑名单4"),
    SZRBlack7("szr_pblacklist","神州融黑名单7"),
    SZREdu("szr_edu","神州融学历"),
    SZRBankThree("szr_fcardthreebind","神州融银行卡三要素"),
    SZRBankFour("szr_fcardfourbind","神州融银行卡四要素"),
    ZCXYIdcard("zcxy_idcard_validate","中诚信源人证一致"),
    TDbodyGuard("td_bodyguard","同盾信贷保镖"),
    TyHaixinMinshu("ty_antifraud","天御海鑫溟数反欺诈"),
    JoThreeAndOnline("jo_threeAndOnline","集奥三要素及在网时长验证"),
    JoTwoVerify("jo_twoVerify","集奥二要素验证"),
    JoBankThree("jo_bankthree","集奥银行卡三要素"),
    JoBankFour("jo_bankfour","集奥银行卡四要素"),
    HxmsMobileOnline("hxms_online","海鑫溟数手机在网时长"),
    HxmsMobileThree("hxms_mobileThree","海鑫溟数手机三要素"),
    HxmsBankThree("hxms_bankThree","海鑫溟数银行卡三要素"),
    HxmsBankFour("hxms_bankFour","海鑫溟数银行卡四要素"),
    FushuBlack("fd_blacklist","富数黑名单"),
    JdBlacklist("jd_blacklist","京东黑名单手机号身份证号姓名"),
    JdBlackMobile("jd_blacklistmobile","京东黑名单手机号"),
    JdFraudScore("jd_fraudscore","京东金融欺诈评分"),
    Jdxbcredit("jd_xbcredit","京东小白信用"),
    KaolaRiskReport("kaola_pRiskReport","考拉个人风险报告"),
    YuanJinIdName("yj_idName","爰金人证一致验证"),
    YuanJinBankThree("yj_bankThree","爰金银行卡三要素"),
    YuanJinBankFour("yj_bankFour","爰金银行卡四要素"),
    WsmTianpingScore("wsm_score","微神马天平分"),
    GxbToken("gxb_token","公信宝token授权"),
    MoxieCarrier("moxie_carrier","魔蝎运营商授权"),
    MoxieTaobao("moxie_taobao","魔蝎淘宝授权"),
    ZXBBlacklist("zxb_starBlacklist","星辰尊享宝黑名单查询"),
    ChuanglanWoolCheck("chuanglan_woolCheck","创蓝羊毛党检测"),
    FDFund("fd_fund_original","富数公积金"),
    FDSocialSecurity("fd_social_security_original","富数社保"),
    HuifengWatchList("huifeng_watchList","惠风行业关注信息"),
    HuifengZmScoreNormal("huifeng_zm_score_normal","惠风芝麻分普惠版"),
    YjPhotoVerify("yj_photoVerify","爰金活体人像比对"),
    FacePlusPhotoVerify("facePlus_photoVerify","Face++活体人像比对"),
    XinYanAssessmentRadar("xinYan_assessmentRadar","新颜雷达"),
    XinYanPushBlack("xinYan_assessmentRadar","新颜黑名单"),
    YoubenMobileThree("youben_mobileThree","有本手机三要素"),
    EnweiMobileThree("enwei_mobileThree","恩唯手机三要素"),
    EnweiBankFour("enwei_bankFour","恩唯银行卡四要素"),
    EnweiBankFThree("enwei_bankThree","恩唯银行卡三要素"),
    EnweiOnline("enwei_online","恩唯手机在网时长"),

    ;



    private String code;
    private String name;

    DspSourceEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static List<EnumModel> toList() {
        List<EnumModel> list = new ArrayList<>();
        for (DspSourceEnum type : DspSourceEnum.values()) {
            EnumModel enumModel = new EnumModel();
            enumModel.setCode(type.getCode());
            enumModel.setName(type.getName());
            list.add(enumModel);

        }
        return list;
    }
    public static DspSourceEnum toEnum(String code) {
        List<EnumModel> list = new ArrayList<>();
        for (DspSourceEnum type : DspSourceEnum.values()) {
           if(type.getCode().equals(code)){
               return type;
           }

        }
        return null;
    }
    public static String getFundSourceCode(String name) {
        List<EnumModel> list = new ArrayList<>();
        for (DspSourceEnum type : DspSourceEnum.values()) {
            if(type.getName().equals(name)){
                return type.getCode();
            }

        }
        return null;
    }

    public static String getName(String code) {
        List<EnumModel> list = new ArrayList<>();
        for (DspSourceEnum type : DspSourceEnum.values()) {
            if(type.getCode().equals(code)){
                return type.getName();
            }
        }
        return null;
    }

    public static Map<String,String> enumMap() {
        Map<String,String> map = new HashMap<>();

        for (DspSourceEnum type : DspSourceEnum.values()) {

            map.put(type.getCode(),type.getName());

        }
        return map;
    }

}
