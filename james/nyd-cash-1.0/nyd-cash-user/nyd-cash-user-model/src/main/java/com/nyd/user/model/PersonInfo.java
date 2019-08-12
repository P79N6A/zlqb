package com.nyd.user.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonInfo implements Serializable{
	
	private String userId;
	//账号
    private String accountNumber;
	//最高学历
    private String highestDegree;
    //婚姻状况
    private String maritalStatus;
    //居住地省code
    private String livingProvince;
    //居住地市code
    private String livingCity;
    //居住地区code
    private String livingDistrict;
    //居住详细地址
    private String livingAddress;
    //qq
    private String qq;
    //微信
    private String weChat;
    //电子邮箱
    private String mailBox;
    //个人信息是否已填写标识(1是已填写 0是未填写)
    private String personFlag;
    
    //行业
    private String industry;
    //职业
    private String profession;
    //公司名称
    private String company;
    //省份
    private String companyProvince;
    //城市
    private String companyCity;
    //地区
    private String companyDistrict;
    //公司详细地址
    private String companyAddress;
    //公司电话区号
    private String telDistrictNo;
    //公司电话
    private String telephone;
    //分机号
    private String telExtNo;
    //薪资
    private String salary;
    
    private String jobFlag;
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
    //联系人信息标识
    private String contactFlag;  
     //设备Id
    private String deviceId;    
    
}
