package com.nyd.capital.model.vo;

import com.nyd.capital.model.annotation.RequireField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/12/13
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckSendSmsVo implements Serializable{
    @RequireField
    private String mobile;//电话
    @RequireField
    private String accNo; //银行账户
    @RequireField
    private String userId; //用户id
}
