package com.nyd.pay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;
import java.util.Date;

/**
 * 银生宝用户信息
 * chaiming
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_user_ysb")
public class YsbUser {

    private Integer fId;

    //用户ID
    private String userId;

    //用户姓名
    private String name;

    //银行卡号
    private String cardNo;

    //身份证
    private String idNumber;

    //子协议号
    private String subContractId;

    //子协议开始时间
//    private String startDate;

    //子协议结束时间
//    private String endDate;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;

}
