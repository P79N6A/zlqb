package com.nyd.admin.model.Vo;

import lombok.Data;

import java.io.Serializable;


@Data
public class CustomerServiceLogVo implements Serializable {

    //备注
    private String remark;

    //客服操作人员
    private String operationPerson;

    //添加时间
    private String operationTime;
}
