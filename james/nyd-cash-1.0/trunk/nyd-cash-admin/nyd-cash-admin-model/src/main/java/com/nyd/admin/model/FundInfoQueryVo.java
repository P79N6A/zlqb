package com.nyd.admin.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/11/30
 **/
@Data
public class FundInfoQueryVo implements Serializable{
    private String fundCode;
    private Integer isInUse;
}
