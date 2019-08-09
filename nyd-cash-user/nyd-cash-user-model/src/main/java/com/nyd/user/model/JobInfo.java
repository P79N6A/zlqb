package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hwei on 2017/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JobInfo implements Serializable {
    //用户ID
    private String userId;
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

    //设备Id
    private String deviceId;
    //账号
    private String accountNumber;
    
    private String jobFlag;
}
