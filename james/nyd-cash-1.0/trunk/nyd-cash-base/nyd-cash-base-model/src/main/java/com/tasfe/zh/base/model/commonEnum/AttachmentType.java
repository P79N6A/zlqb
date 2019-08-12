package com.tasfe.zh.base.model.commonEnum;

/**
 * Created by hwei on 2017/11/4.
 * 附件类型
 */
public enum AttachmentType {
    IdCardFrontPhoto("1","身份证正面照"),
    IdCardBackPhoto("2","身份证反面照"),
    FacePhoto("3","头像照"),
    E_Contract("4","电子合同");
    private String type;
    private String description;



    AttachmentType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
