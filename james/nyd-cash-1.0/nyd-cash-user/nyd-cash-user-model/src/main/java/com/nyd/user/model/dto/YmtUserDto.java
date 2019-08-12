package com.nyd.user.model.dto;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 银码头用户数据同步到侬要贷所传参数对象
 */
@Data
public class YmtUserDto implements Serializable{
    //user(用户基本信息)所需字段
//    private String userId;
    //关联银马头用户id
    private String iBankUserId;
    //姓名
    private String realName;
    //身份证号码
    private String idNumber;
    //证件类型
    private String certificateType;
    //性别
    private String gender;
    //民族
    private String nation;
    //生日
    private String birth;

    //t_account(账号表)所需字段
    //账号类型
    private Integer account_type;
    //账号号码
    private String accountNumber;
    //信用额度
    private BigDecimal limitCredit;
    //可用额度
    private BigDecimal usableCredit;
    //已用额度
    private BigDecimal usedCredit;
    //产品线
    private String productCode;
    //账号状态
    private String status;
    //用户来源
    private String source;

    //t_account_password(账号密码表)所需字段
    //密码类型
    private Integer passwordType;
    //密码
    private String password;

    //t_user_bank(用户银行卡信息)所需字段
    //用户
    private String accountName;
    //银行名称
    private String bankName;
    //卡号
    private String bankAccount;
    //账户类型借记卡、信用卡
    private String accountType;
    //预留手机号
    private String reservedPhone;

    //t_user_contact(联系人信息)所需字段
    //直接联系人关系
    private String directContactRelation;
    //直接联系人姓名
    private String directContactName;
    //直接联系人电话
    private String directContactMobile;
    //重要联系人关系
    private String majorContactRelation;
    //重要联系人姓名
    private String majorContactName;
    //重要联系人电话
    private String majorContactMobile;
    //设备Id
    private String deviceId;
    //账号
//    private String accountNumber;

    //t_user_detail(用户详细信息)所需字段
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

    //t_user_job(用户工作信息)所需字段
    //行业
    private String industry;
    //职业
    private String profession;
    //公司名称
    private String company;
    //公司详细地址
    private String companyAddress;
    //省份
    private String companyProvince;
    //城市
    private String companyCity;
    //地区
    private String companyDistrict;
    //职位
    private String position;
    //薪资
    private String salary;
    //公司电话区号
    private String telDistrictNo;
    //公司电话
    private String telephone;
    //分机号
    private String telExtNo;

    //t_user_step(用户信息填写情况)所需字段
    //身份证信息是否填写
    private String identityFlag;
    //身份证信息权重
    private Integer identityWeight;
    //工作信息是否填写
    private String jobFlag;
    //工作信息权重
    private Integer jobWeight;
    //联系人信息是否填写
    private String contactFlag;
    //联系人信息权重
    private Integer contactWeight;
    //银行卡信息是否填写
    private String bankFlag;
    //银行卡信息权重
    private Integer bankWeight;
    //信用认证
    private String authFlag;
    //芝麻分是否认证
    private String zmxyFlag;
    //芝麻分权重
    private Integer zmxyWeight;
    //手机是否认证
    private String mobileFlag;
    //手机认证权重
    private Integer mobileWeight;
    //淘宝是否认证
    private String tbFlag;
    //淘宝认证权重
    private Integer tbWeight;
    //淘宝认证时间
    private String tbTime;
    //网银是否认证
    private String onlineBankFlag;
    //网银认证权重
    private Integer onlineBankWeight;
    //公信宝认证
    private String gxbFlag;
    //公信宝认证时间
    private String gxbTime;

    //身份证正面照
    private String idCardFrontPhoto;
    //身份证背面照
    private String idCardBackPhoto;

}
