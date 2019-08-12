package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wucx
 * @Date: 2018/11/1 17:53
 */
@Data
public class SalesPlatformDto implements Serializable {

    //起始时间
    private String startTime;

    //结束时间
    private String endTime;

    //手机号
    private String accountNumber;

    //身份证号
    private String idNumber;

    //页数
    private Integer pageNum;

    //每页个数
    private Integer pageSize;
}
