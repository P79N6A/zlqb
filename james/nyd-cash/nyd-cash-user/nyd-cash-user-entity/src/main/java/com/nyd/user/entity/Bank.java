package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;

/**
 * Created by Dengw on 17/10/31.
 * 用户银行卡信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="t_user_bank")
public class Bank{
    @Id
    private Long id;
    //用户ID
    private String userId;
    //用户
    private String accountName;
    //银行名称
    private String bankName;
    //卡号
    private String bankAccount;
    //账户类型借记卡、信用卡
    private String accountType;
    //预留手机号
    private String reservedPhone;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
	//银行卡类型 1 畅捷 2 合利宝
    private Integer soure;
    
  //渠道    changjie 常捷  xunlian 讯联
  	private String channelCode;

  	//绑卡协议号
  	private String protocolId;
}
