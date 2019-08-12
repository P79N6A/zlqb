package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wucx
 * @Date: 2018/10/16 21:08
 */
@Data
public class CreditDto implements Serializable {
    //手机号
    private String accountNumber;

    //注册开始时间
    private String createBeginTime;

    //注册结束时间
    private String createEndTime;

    //操作系统
    private String os;

    //app名字
    private String appCode;

    //起始页
    private Integer pageNum;

    //每页查询数
    private Integer pageSize;
}
