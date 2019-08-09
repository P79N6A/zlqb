package com.nyd.user.model.enums;

/**
 * Created by Dengw on 2017/11/17
 */
public enum MediaType {
    Image("1","Image"),
    PDF("2","PDF");
    private String type;
    private String description;



    MediaType(String type, String description) {
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
