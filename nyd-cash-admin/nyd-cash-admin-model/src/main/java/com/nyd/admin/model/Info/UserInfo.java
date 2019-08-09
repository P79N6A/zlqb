package com.nyd.admin.model.Info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: wucx
 * @Date: 2018/10/16 21:18
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements Serializable {

    //姓名
    private String realName;

    //性别
    private String gender;
}
