package com.nyd.capital.api.enums;

/**
 * Cong Yuxiang
 * 2017/11/13
 **/
public enum ErrorCode {
    MYC_WTG("1000","商户密钥串校验未通过"),
    SING_WTG("1001","数字签名校验未通过"),
    BD_WTG("1002","绑定校验未通过"),
    ZFMX_WTG("1003","支付明细校验未通过"),
    BDFW_WTG("1004","绑定范围校验未通过"),
    GKH_FK_WTG("1005","对公开户行相关信息非空校验未通过"),
    JM_WTG("1006","解密校验未通过"),
    BT_WTG("1007","必填校验未通过"),
    ZDGZ_WTG("1008","字段规则校验未通过"),
    RQ_WTG("1009","日期校验未通过"),
    LX_WTG("1010","类型校验未通过"),
    YL_WTG("1011","依赖必填校验未通过"),
    ZDCD_WTG("1012","字段长度校验未通过"),
    YYSJ_WTG("1013","营业时间校验未通过"),
    AHMD_WTG("1014","A类黑名单校验未通过"),
    YHK_WTG("1015","银行卡支持校验未通过(暂时停用)"),
    DDHCF_WTG("1016","商户订单号重复校验未通过"),
    DL_WTG("1017","单量校验未通过"),
    ZJF_WTG("1018","资金方匹配失败"),
    HKJH_SB("1019","还款计划生成失败"),
    QX_SB("1020","签约失败"),
    ZF_SB("1021","支付失败"),
    DDHBCZ("1022","商户订单号不存在"),
    CLZ("1023","处理中"),
    AGE_WTG("1024","借贷人年龄校验未通过"),
    IP_WTG("1025","资产ip地址不符合设置的ip地址"),
    KHHBH_WTG("1026","开户行编号不在系统可用银行列表中"),
    YH_ZFSB("1027","第三方支付成功后银行支付处理失败"),
    DDCX_GP("1028","订单查询过频"),
    BZJ_WTG("1029","保证金金额校验未通过"),
    DRJE_WTG("1030","当日金额校验未通过"),

    OBJ_NULL("2001","对象为空"),
    PARAM_NULL("2002","参数为空或长度不够");

    private String code;
    private String msg;

    private ErrorCode(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCodeInt(){
        return Integer.valueOf(code);
    }
    public String getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }


}
