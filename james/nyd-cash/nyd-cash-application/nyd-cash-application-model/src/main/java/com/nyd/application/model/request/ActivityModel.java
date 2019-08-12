package com.nyd.application.model.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ActivityModel implements Serializable{
	
	//图片地址
    private String imgUrl;
    //活动跳转地址
    private String linkUrl;
    //活动标题
    private String title;
    //按钮名称
    private String btnName;
    //是否展示
    private Integer display;
    //app名称
    private String appName;

}
