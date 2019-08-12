package com.nyd.user.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewStepInfo  implements Serializable{
	//用户ID
    private String userId;
	//联系人信息标识
    private String contactFlag;   
    //刷脸认证标识
    private String faceFlag;
     //是否有借款
    private String firstLoanFlag;
    //姓名
    private String userName;
    //身份证号
    private String idNumber;
    //工作信息标识
    private String jobFlag;
    //身份认证标识
    private String identityFlag;
    //绑定银行卡认证标识
    private String bindCardFlag;
    //运营商认证标识
    private String mobileFlag;
    //个人信息认证标识
    private String personInfoFlag;
    //是否可以借款
    private String whetherLoan;
    //是否借款话术
    private String whetherLoanMsg;
    //完整度是否满足借款
    private String whetherScore;
    //完整度是否满足借款话术
    private String whetherScoreMsg;

    private String orderFlag; // 1 审批中 2 待提现 3还款中 4 拒单
    private String billStatus;// B001-还款中 B002-逾期中 B003-已结清

}
