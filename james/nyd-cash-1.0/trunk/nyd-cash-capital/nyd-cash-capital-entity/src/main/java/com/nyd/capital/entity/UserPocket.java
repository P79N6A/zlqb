package com.nyd.capital.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuqiu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_user_pocket")
public class UserPocket implements Serializable {

    @Id
    private Long id;

    private String userId;

    private String memberId;

    private String mobile;
    private String password;
    /**
     * 口袋理财用户状态(0-未开户,1-已开户,2-已接受,3-已提现,4-已完成,5-推单失败)
     */
    private Integer stage;
    private String withDrawUrl;
    private BigDecimal Amount;
    private Date createTime;
    private Date updateTime;
    private String updateBy;
}
