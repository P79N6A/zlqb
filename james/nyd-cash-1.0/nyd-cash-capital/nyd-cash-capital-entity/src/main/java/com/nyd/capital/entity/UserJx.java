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
@Table(name = "t_user_jx")
public class UserJx implements Serializable {
    @Id
    private Long id;

    private String loanId;

    private String userId;

    private String memberId;

    private String mobile;
    private String password;
    /**
     * 即信用户进度(0-未开户,1-已开户,2-已过审,3-已放款,4-已提现,5-提现失败)
     */
    private Integer stage;
    private String withDrawUrl;
    private BigDecimal Amount;
    private Date createTime;
    private Date updateTime;
    private String updateBy;
}
