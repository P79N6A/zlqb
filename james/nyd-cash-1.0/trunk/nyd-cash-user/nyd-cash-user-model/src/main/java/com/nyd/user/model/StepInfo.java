package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 17/11/4.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StepInfo implements Serializable {
    //用户ID
    private String userId;
    //身份证信息
    private String identityFlag;
    //工作信息
    private String jobFlag;
    //联系人信息
    private String contactFlag;
    //信用认证
    private String authFlag;
    //芝麻信用
    private String zmxyFlag;
    //手机认证
    private String mobileFlag;
    //淘宝是否认证
    private String tbFlag;
    //网银认证
    private String onlineBankFlag;
    //公信宝认证
    private String gxbFlag;
    //资料完整度
    private String stepScore;
    //完整度是否满足借款
    private String whetherScore;
    //完整度是否满足借款话术
    private String whetherScoreMsg;
    //是否可以借款
    private String whetherLoan;
    //是否借款话术
    private String whetherLoanMsg;
    //设备Id
    private String deviceId;
    //账号
    private String accountNumber;
    //风控是否审核0：否 1 是
    private String preAuditFlag;
    //风控审核结果等级
    private String preAuditLevel;
    //姓名
    private String userName;
    //身份证号
    private String idNumber;
    //手机认证时间
    private String mobileTime;
    //刷脸认证
    private String faceFlag;
    //身份认证
    private String newIdentityFlag;
    //运营商认证完成
    private String operatorFlag;
    //个人信息认证
    private String personInfoFlag;
    //是否有借款
    private String firstLoanFlag;
    private String appName;
}
