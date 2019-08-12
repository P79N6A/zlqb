package com.nyd.msg.service.code;

import org.apache.commons.lang3.StringUtils;

public enum DaHanResultEnum {

    SUCCESS("0","success"),
    FAILE_101("101","无此用户"),
    FAILE_102("102","密码错误"),
    FAILE_103("103","提交过快（提交速度超过流速限制）"),
    FAILE_104("104","系统忙（因平台侧原因，暂时无法处理提交的短信）"),
    FAILE_105("105","敏感短信（短信内容包含敏感词）"),
    FAILE_106("106","消息长度错（>700或<=0）"),
    FAILE_107("107","包含错误的手机号码"),
    FAILE_108("108","手机号码个数错（>50000或<=0）"),
    FAILE_109("109","无发送额度（该用户可用短信条数为0）"),
    FAILE_110("110","不在发送时间内"),
    FAILE_111("111","超出该账户当月发送额度限制"),
    FAILE_112("112","无此产品，用户没有订购该产品"),
    FAILE_113("113","extno格式错（非数字或者长度不对）"),
    FAILE_114("114","可用参数组个数错误（=0或>1000）"),
    FAILE_115("115","自动审核驳回"),
    FAILE_116("116","签名不合法，未带签名（用户必须带签名的前提下）"),
    FAILE_117("117","IP地址认证错,请求调用的IP地址不是系统登记的IP地址"),
    FAILE_118("118","用户没有相应的发送权限"),
    FAILE_119("119","用户已过期"),
    FAILE_120("120","内容不在白名单模板中");

    private DaHanResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    public static String getMsgByCode(String code){
        if(StringUtils.isBlank(code)){
            return null;
        }
        for (DaHanResultEnum bs:DaHanResultEnum.values()){
            if(bs.getCode().equals(code)){
                return bs.getMsg();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
