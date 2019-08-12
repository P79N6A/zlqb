package com.nyd.capital.model.vo;

import com.nyd.capital.model.annotation.RequireField;
import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2018/3/2
 **/
@Data
public class CheckStatusVo implements Serializable{

//

    @RequireField
    private String mobile;//电话
    @RequireField
    private String accNo; //银行账户
    @RequireField
    private String userId; //用户id

    private Boolean isFirst;//是否属于第一次查询


    private Integer channel;//0 - 侬要贷, 1 - 银码头
    private String loanAmount;
    private Integer borrowTime;
}
