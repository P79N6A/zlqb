package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * 绑卡信息
 * @author zhangdk
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_user_bind")
public class UserBind implements Serializable{

    /**
	 * 
	 */

	private Integer Id;

    //用户ID
    private String userId;

    //用户姓名
    private String userName;
    
    //签要平台
    private String platform;

    //银行卡号
    private String cardNo;

    //身份证
    private String idNumber;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;
    //签要状态
    private String signStatus;
    //获取验证码请求单号
    private String requestNo;
    //银行卡号
    private String bankCode;
    //银行名称
    private String bankName;
    //手机号码
    private String phone;

    // 渠道code
    private String channelCode;

    //短信编号 迅捷
    private String smsSendNo;

    //商户请求流水号
    private String merOrderId;
}

