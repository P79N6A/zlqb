package com.nyd.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/11/24
 * 活体识别图片
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LivenessModel implements Serializable{
    //用户ID
    private String userId;
    //活体最好的图片
    private String bestImage;
    //活体照片
    private String imageEnv;
    //身份证抠图
    private String imageRef1;
}
