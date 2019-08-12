package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_phone_region")
public class PhoneRegion {

    @Id
    private Integer id;

    //手机号前7位
    private  String phoneFlag;

    /**
     * 手机号
     */
//    private  String phone;

    // 手机号所在的省份
    private String province;

    // 手机号所在的城市
    private String city;

    private String cityCode;

    private String provinceCode;

    private String areaCode;

    private String provinceShortCode;

    //1-移动, 2-联通, 3-电信, 4-移动虚拟运营商, 5-联通虚拟运营商, 6-电信虚拟运营商, 7-虚拟运营商, 9-其他
    private String   operator;

    private String   operatorCode;

}
