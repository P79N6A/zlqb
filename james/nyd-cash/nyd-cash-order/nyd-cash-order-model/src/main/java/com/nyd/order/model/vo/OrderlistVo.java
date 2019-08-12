package com.nyd.order.model.vo;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * Created by  on 2019/07/17
 * 借款返回对象
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OrderlistVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//订单编号
	private String orderNo;
    //订单状态
    private String orderStatus;
    //产品期数
    private String productPeriods;
    //用户姓名
    private String custName;
    //用户手机号
    private String mobile;
    //注册来源
    private String source;
    //申请时间
    private String createTime;
    //产品名称
    private String procuctName;
    //借款用途
    private String loanPurpose;
    //借款用途
    private String userId;
}
