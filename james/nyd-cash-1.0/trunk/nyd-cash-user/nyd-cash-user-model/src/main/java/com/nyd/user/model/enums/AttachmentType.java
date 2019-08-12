package com.nyd.user.model.enums;

public enum AttachmentType {
	IDCARD_FRONT_PHOTO("1", "身份证正面照"),
    IDCARD_BACK_PHOTO("2", "身份证反面照"),
    BESTIMAGE("3", "活体最好照片"),
    IMAGEENV("4", "活体照片"),
    IMAGEREF1("5", "身份证抠图"),
    IMAGIDDENTIFY("6", "手持身份证照片"),
    ACTION1("7", "眨眼"),
    ACTION2("8", "张嘴"),
    ACTION3("9", "左右摇头"),
    ACTION4("10", "缓慢点头"),
    EXTRA("11", "额外字段");
    private String type;

    private String description;

    private AttachmentType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return this.type;
    }

    public Integer getCodeInt() {
        return Integer.valueOf(this.type);
    }

    public String getDescription() {
        return this.description;
    }
}
