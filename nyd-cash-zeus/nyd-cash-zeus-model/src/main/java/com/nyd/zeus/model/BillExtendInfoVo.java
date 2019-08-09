package com.nyd.zeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

/**
 * Created by zhujx on 2017/11/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BillExtendInfoVo implements Serializable{


    //主键id
    private Long id;
    // 客户姓名
    private String userName;
    // 用户手机
    private String userMobile;
    // 用户身份证号
    private String userIc;
    // 订单no
    private String orderNo;
    // 信审用户id
    private String creditTrialUserId;
    // 信审用户名称
    private String creditTrialUserName;
    // 创建时间
    private Date createTime;
    // 注册渠道
    private String source; 
    // 订单申请日期
    private Date applyTime;
    // 订单放款日期
    private Date loanTime;
    // 借款次数
    private Integer loanNum;
}
