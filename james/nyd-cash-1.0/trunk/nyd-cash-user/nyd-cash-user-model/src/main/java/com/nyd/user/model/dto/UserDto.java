package com.nyd.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/11/24
 * 用于API查询身份证
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto implements Serializable {
    //姓名
    private String realName;
    //电话
    private String accountNumber;
    //居住地市
    private String livingCity;
    //居住地区
    private String livingDistrict;
    //身份证号
    private String idNumber;

}
