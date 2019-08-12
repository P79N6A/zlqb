package com.nyd.user.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/11/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IdentityVo implements Serializable {
    //身份证姓名
    private String realName;
    //身份证号码
    private String idNumber;
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
}
