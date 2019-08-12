package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerServiceLogDto implements Serializable {

    //用户Id
    private String userId;

    //用户姓名
    private String realName;

    //内容
    private String remark;

    //客服操作人员
    private String operationPerson;


}
