package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wucx
 * @Date: 2018/11/1 9:32
 */
@Data
public class IdNumberDto implements Serializable {

    //身份证号
    private String idNumber;
    //代扣状态
    private Integer state;
}
