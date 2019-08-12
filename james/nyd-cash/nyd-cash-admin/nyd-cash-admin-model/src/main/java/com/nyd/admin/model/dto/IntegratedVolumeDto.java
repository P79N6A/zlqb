package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wucx
 * @Date: 2018/9/6 17:31
 */
@Data
public class IntegratedVolumeDto implements Serializable {

    //手机号
    private String mobile;

    //发放状态
    private Integer state;

    //开始时间
    private String startTime;

    //结束时间
    private String endTime;

    //券类型(1:小银券)
    private Integer refundTicketType;

    //起始页
    private Integer pageNum;

    //每页和数
    private Integer pageSize;
}
