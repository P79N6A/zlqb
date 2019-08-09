package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dengw on 2017/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDetailInfo implements Serializable {
    //用户ID
    private String userId;
    //身份证姓名
    private String realName;
    //身份证号码
    private String idNumber;
    //身份证详细地址
    private String idAddress;
    //签发机关
    private String signOrg;
    //身份证生效日期
    private String effectTime;
    //居住详细地址
    private String livingAddress;
    //居住地省code
    private String livingProvince;
    //居住地市code
    private String livingCity;
    //居住地区code
    private String livingDistrict;
    //婚姻状况
    private String maritalStatus;
    //最高学历
    private String highestDegree;
    //人脸识别结果
    private String faceRecognition;
    //人脸识别相似度
    private BigDecimal faceRecognitionSimilarity;
    //qq
    private String qq;
    //微信
    private String weChat;
    //电子邮箱
    private String mailBox;
    
}
