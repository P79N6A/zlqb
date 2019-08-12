package com.nyd.settlement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2018/1/16
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelDto implements Serializable{
    //账号
    private String orderNo;
    //客户姓名
    private String realName;
    //手机号
    private String mobile;
    //备注
    private String remark;
    //是否取消成功
    private String status;
    //取消失败原因
    private String failReason;
    //更改人
    private String updateBy;
}
