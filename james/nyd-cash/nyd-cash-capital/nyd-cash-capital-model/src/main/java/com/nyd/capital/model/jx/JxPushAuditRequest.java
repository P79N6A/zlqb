/**
 * Project Name:nyd-cash-capital-model
 * File Name:JxPushAuditRequest.java
 * Package Name:com.nyd.capital.model.jx
 * Date:2018年7月30日上午11:18:05
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.capital.model.jx;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * ClassName:JxPushAuditRequest <br/>
 * Function: 推单外审请求参数
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年7月30日 上午11:18:05 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class JxPushAuditRequest extends JxCommonRequest implements Serializable {
	/**
	 * serialVersionUID:序列化ID.
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -3282328965585316281L;
	//手机号
	private String mobile;
	//姓名
	private String realName;
	//身份证号
	private String idCardNumber;
	//借款期限
	private Integer phaseCount;
	//渠道批次编码(可以不传)
	private String batchCode;
	//借款金额
	private BigDecimal amount;
	//身份证正面(七牛云地址)
	private String idCardPictureFront;
	//身份证反面(七牛云地址)
	private String idCardPictureBack;
	//活体认证地址(家庭地址)
	private String liveIdentification;

	/**
	 * 以下字段在渠道为：中网国投：zwgt时必填
	 */
	//婚姻状态
	private String marriageState;
	//教育程度
	private String education;
	//职业
	private String jobOffice;
	//工作城市
	private String jobCity;
	//出借人手机号(可不填)
	private String lenderPhone;
	
	/**以下字段为非必填字段*/
	
	//收入
	private BigDecimal income;
	//收入单位
	private String incomeUnit;
	//负债
	private String debt;
	//借款用途
	private String borrowUse;
	//评级
	private String creditRate;
	//征信
	private String creditInvestigation;
	//经销商名称
	private String agentName;
	//车辆状态
	private String vehicleState;
	//担保方式
	private String guaranteeType;
	//车牌
	private String vehicleNumber;
	//发动机号
	private String engineNumber;
	//车辆型号
	private String vehicleModel;
	
	//1.其他联系人姓名;关系;联系电话
	private String contact1Name;
	private String contact1Relation;
	private String contact1Phone;
	//2.其他联系人姓名;关系;联系电话
	private String contact2Name;
	private String contact2Relation;
	private String contact2Phone;
	//3.其他联系人姓名;关系;联系电话
	private String contact3Name;
	private String contact3Relation;
	private String contact3Phone;
	//出生日期(yyyy-MM-dd)
	private String birthDate;
	//户籍地址
	private String birthPlace;
	
	//子女状况
	private String childrenState;
	//配偶姓名
	private String spouseName;
	//配偶身份证号
	private String spouseIdCardNo;
	//单位电话
	private String workPhone;
	//家庭固话
	private String homePhone;
	//住宅地址
	private String address;
	//性别
	private String sex;
	//民族
	private String folk;
	//申请地点
	private String applyLocation;
	//行业
	private String industry;
	//还款来源
	private String repaymentSource;
	//标的简介
	private String loanDescription;
	//用户征信授权协议 
	private String creditInvestigationAgreement;
	//资产安全评级
	private String assertSaftyRating;
	//贷后管理情况
	private String postLoanManagement;
	//证件所在地
	private String idLoacation;
	//催收记录
	private String collectionRecord;
	//统一社会信用代码、开户许可证
	private String unifiedSocialCreditCode;
	
	//证照附件
	private Object attatchments;
	//通知地址
	private String notificationUrl;
	//借款手续费率,百分比	如:7%则传 7
	private BigDecimal feeRate;
	//分账手续费率
	private BigDecimal shareFeeRate;
	//受托支付帐号
	private Long entrustReceiverId;
	//保证金收取期数
	private Integer marginChargePhase;
	//扩展字段（3000字符内）
	private String extension;
	//加密值
	private String sign;
}

