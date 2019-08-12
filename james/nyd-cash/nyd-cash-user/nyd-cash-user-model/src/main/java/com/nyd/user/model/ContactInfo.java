package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/11/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactInfo implements Serializable {
    //用户ID
    private String userId;
    //联系人类型
    private String type;
    //联系人姓名
    private String name;
    //联系人手机号
    private String mobile;
    //与联系人的关系
    private String relationship;
}
