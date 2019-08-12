package com.nyd.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/11/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BannerModel implements Serializable {
    //图片跳转url
    private String linkUrl;
    //图片url
    private String fileUrl;
    //图片type
    private String type;
    //url标题
    private String title;
    //app简称
    private String appName;
}
