package com.tasfe.zh.base.model.commonEnum;

/**
 * Created by hwei on 2017/11/4.
 * 文件类型
 */
public enum FileType {
    Image("1","Image"),
    PDF("2","PDF");
    private String type;
    private String description;



    FileType(String type, String description) {
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
