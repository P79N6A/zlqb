package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 17/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankUser implements Serializable {
	private static final long serialVersionUID = 1L;
    //姓名
    private String accountName;
    //银行卡号
    private String bankAccount;
    //预留手机号
    private String reservedPhone;
    //业务订单号
	private String protocolNo;
	//合利宝user_id
	private String hlbUserId;
	//身份证号码
	private String idNumber;
}
