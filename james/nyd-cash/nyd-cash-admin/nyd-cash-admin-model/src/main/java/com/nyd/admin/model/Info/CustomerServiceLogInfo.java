package com.nyd.admin.model.Info;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CustomerServiceLogInfo implements Serializable {

    //客户id
    private String userId;

    //客户姓名
    private String userName;

    //备注
    private String remark;

    //客服操作人员
    private String operationPerson;

    //添加时间
    private Date createTime;
}
