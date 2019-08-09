package com.nyd.capital.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 借款表
 * 
 * @author zhangdk
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_user_dld_loan")
public class UserDldLoan implements Serializable {

	@Id
	private Long id;
	private String merOrderNo;
	private String userId;
	private String channalCode;
	private String card;
	private String custom;
	private String mobile;
	private String certifyId;
	private String loanAmt;
	private String loanDays;
	private String purpose;
    private Integer stage;
	private String periodization;
	private String customerId;
	private Date createTime;
    private Date updateTime;
    private String updateBy;
    private String contractUrl;
    //是否已删除
    private Integer deleteFlag;
}
