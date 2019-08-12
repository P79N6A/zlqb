package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Dengw on 2017/11/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IdentityInfo implements Serializable {
    //用户userId
    private String userId;
    //账号
    private String accountNumber;
    //证件类型
    private String certificateType;
    //姓名
    private String realName;
    //身份证号码
    private String idNumber;
    //性别
    private String gender;
    //最高学历
    private String highestDegree;
    //婚姻状况
    private String maritalStatus;
    //出生年月
    private String birth;
    //民族
    private String nation;
    //身份证地址
    private String idAddress;
    //签发机关
    private String signOrg;
    //身份证有效期
    private String effectTime;
    //居住地省code
    private String livingProvince;
    //居住地市code
    private String livingCity;
    //居住地区code
    private String livingDistrict;
    //居住详细地址
    private String livingAddress;
    //身份证正面照
    private String idCardFrontPhoto;
    //身份证背面照
    private String idCardBackPhoto;
    //人脸识别结果
    private String faceRecognition;
    //人脸识别相似度
    private BigDecimal faceRecognitionSimilarity;

    //设备Id
    private String deviceId;
    //照片key List
    private String fileJson;
    //前端额外传输参数，不参与后端业务
    private String jsonString;
    //face++ 图片标识字段
    private String delta;
}
