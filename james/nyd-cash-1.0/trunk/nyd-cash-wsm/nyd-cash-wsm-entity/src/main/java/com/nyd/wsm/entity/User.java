package com.nyd.wsm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by zhujx on 2017/12/8.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    //用户ID
    private String userId;
    //民族
    private String nation;
    //账号号码
    private String accountNumber;
    //预留手机号
    private String reservedPhone;
    //银行名称
    private String bankName;
    //卡号
    private String bankAccount;
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
    //居住地省
    private String livingProvince;
    //居住地市
    private String livingCity;
    //居住地区
    private String livingDistrict;
    //婚姻状况
    private String maritalStatus;
    //最高学历
    private String highestDegree;
    //行业
    private String industry;
    //职业
    private String profession;
    //公司名称
    private String company;
    //公司电话
    private String telephone;
    //公司详细地址
    private String companyAddress;
    //公司省份
    private String companyProvince;
    //公司城市
    private String companyCity;
    //公司地区
    private String companyDistrict;
    //与直接联系人关系
    private String directContactRelation;
    //直接联系人姓名
    private String directContactName;
    //直接联系人电话
    private String directContactMobile;
    //与重要联系人关系
    private String majorContactRelation;
    //重要联系人姓名
    private String majorContactName;
    //重要联系人电话
    private String majorContactMobile;

}
