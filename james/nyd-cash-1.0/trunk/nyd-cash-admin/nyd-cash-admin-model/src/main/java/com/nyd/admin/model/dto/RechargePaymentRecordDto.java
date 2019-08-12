package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wucx
 * @Date: 2018/9/5 13:53
 */
@Data
public class RechargePaymentRecordDto  implements Serializable {

    //用户编号
    private String userId;
    //页数
    private Integer pageNum;
    //每页个数
    private Integer pageSize;

}
