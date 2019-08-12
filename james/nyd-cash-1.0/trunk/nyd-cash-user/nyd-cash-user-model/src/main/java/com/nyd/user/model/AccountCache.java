package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hwei on 2017/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountCache implements Serializable{
    private Long id;
    //用户ID
    private String userId;
    //账号类型
    private Integer accountType;
    //账号号码
    private String accountNumber;
    //信用额度
    private BigDecimal limitCredit;
    //可用额度
    private BigDecimal usableCredit;
    //已用额度
    private BigDecimal usedCredit;
    //产品线
    private String productCode;
    //账号状态
    private String status;
    //用户来源
    private String source;
    //最后活跃时间
    private Date lastActiveTime;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
    //用户渠道
    private Integer channel;
    //关联银马头用户id
    private String iBankUserId;

    /**用户余额*/
    private BigDecimal balance;
    private String password;

    /**
     * 用户返回余额
     */
    private BigDecimal returnBalance;

}
