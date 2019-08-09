package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactInfos implements Serializable {
    //用户ID
    private String userId;
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
    private String accountNumber;
    //联系人信息
   private String contactFlag;

}
