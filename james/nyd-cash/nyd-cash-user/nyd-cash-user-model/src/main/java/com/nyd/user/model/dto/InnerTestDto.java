package com.nyd.user.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hwei on 2018/11/26.
 */
@Data
@ToString
public class InnerTestDto implements Serializable{
    //手机号
    private String mobile;
    //身份证号
    private String idNumber;
}
