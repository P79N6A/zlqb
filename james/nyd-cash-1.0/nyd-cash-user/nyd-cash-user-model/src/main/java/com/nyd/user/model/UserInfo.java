package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by dengw on 2017/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfo implements Serializable {
    //用户ID
    private String userId;
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
    //用户手机号
    private String accountNumber;
    //用户来源
    private String source;
    /**'用户渠道（0 侬要贷 1银码头）'*/
    private Integer channel;
}
