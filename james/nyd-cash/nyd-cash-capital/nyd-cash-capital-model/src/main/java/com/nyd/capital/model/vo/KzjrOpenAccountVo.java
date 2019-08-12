package com.nyd.capital.model.vo;

import com.nyd.capital.model.annotation.RequireField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2018/2/25
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KzjrOpenAccountVo implements Serializable{
    @RequireField
    private Integer idType; //证件类型  1 身份证
    @RequireField
    private String idNo;  // 证件号码
    @RequireField
    private String name;  // 姓名
    @RequireField
    private String mobile; // 手机号

    private String cardNo; // 银行卡号
    @RequireField
    private String userId;

    private String returnUrl;
}
