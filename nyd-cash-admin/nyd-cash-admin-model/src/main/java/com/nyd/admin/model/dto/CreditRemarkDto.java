package com.nyd.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: wucx
 * @Date: 2018/10/16 21:24
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreditRemarkDto implements Serializable {

    //用户Id
    private String userId;

    //手机号
    private String accountNumber;

    //app名字
    private String appName;

    //os系统
    private String os;

    //操作人
    private String operatorPerson;

    //操作
    private String remark;
}
