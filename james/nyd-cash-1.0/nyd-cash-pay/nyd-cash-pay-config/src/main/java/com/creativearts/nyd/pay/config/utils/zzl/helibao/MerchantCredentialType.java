package com.creativearts.nyd.pay.config.utils.zzl.helibao;

public enum MerchantCredentialType {

    ORG_CERTIFICATE("组织机构证", 1),

    TAX_REG_CERTIFICATE("税务登记证", 2),

    BUSINESS_LICENSE("营业执照", 3),

    FRONT_OF_ID_CARD("身份证正面", 4),

    BACK_OF_ID_CARD("身份证反面", 5),

    HANDHELD_OF_ID_CARD("手持身份证照", 6),

    ;

    private final String desc;
    private final Integer index;

    MerchantCredentialType(String desc, Integer index) {
        this.desc = desc;
        this.index = index;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getIndex() {
        return index;
    }

}
