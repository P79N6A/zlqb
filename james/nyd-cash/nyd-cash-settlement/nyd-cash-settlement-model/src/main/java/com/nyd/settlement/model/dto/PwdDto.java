package com.nyd.settlement.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2018/2/1
 **/
@Data
public class PwdDto implements Serializable{
    private String pwd;
    private Integer type;
}
