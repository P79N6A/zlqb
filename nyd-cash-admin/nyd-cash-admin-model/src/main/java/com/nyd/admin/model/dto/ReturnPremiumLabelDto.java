package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReturnPremiumLabelDto implements Serializable {

    //客户编号
    private String userId;

    //客户姓名
    private String userName;

    //手机号
    private String mobile;

    //备注
    private String remark;

    //操作人
    private String operatePerson;

}
