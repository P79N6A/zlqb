package com.nyd.user.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by hwei on 2017/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefundAppCountInfo extends BaseInfo implements Serializable{
    //app code
    private String appCode;
    //统计日期
    private Date countDate;
    //统计日期字符串
    private String countDateStr;
    //点击数
    private Integer clickCount;
    //点击数更新标识
    private int updateClikCount;
    //注册数
    private Integer registerCount;
    //注册数更新标识
    private int updateRegisterCount;

}
