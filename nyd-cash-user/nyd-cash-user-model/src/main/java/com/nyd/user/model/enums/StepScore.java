package com.nyd.user.model.enums;

/**
 * Created by Dengw on 2017/11/20
 * 资料完整度及认证权重信息
 */
public enum StepScore {
    IDENTITY_WEIGHT(20,"身份证权重分"),
    JOB_WEIGHT(15,"工作权重分"),
    CONTACT_WEIGHT(20,"联系人权重分"),
    MOBILE_WEIGHT(25,"运营商认证权重分"),
    TAOBAO_WEIGHT(10,"淘宝认证权重分"),
    ZMXY_WEIGHT(0,"芝麻认证权重分"),
    ONLINE_BANK_WEIGHT(0,"网银认证权重分"),
    GXB_WEIGHT(10,"公信宝认证权重分");

    private Integer score;

    private String description;

    private StepScore(Integer score, String description){
        this.score = score;
        this.description = description;
    }

    public Integer getScore(){
        return this.score;
    }

    public String getDescription(){
        return this.description;
    }
}
