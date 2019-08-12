package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改备注入参
 * @Author: wucx
 * @Date: 2018/9/12 14:41
 */
@Data
public class RemarkDto implements Serializable {

    //唯一标识
    private String premiumId;

    //备注
    private String remark;
}
